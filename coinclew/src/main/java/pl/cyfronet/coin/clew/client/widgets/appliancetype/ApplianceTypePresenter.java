package pl.cyfronet.coin.clew.client.widgets.appliancetype;

import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.FlavorsCallback;
import pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliancetype.AggregateApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.controller.cf.computesite.ComputeSite;
import pl.cyfronet.coin.clew.client.controller.cf.flavor.Flavor;
import pl.cyfronet.coin.clew.client.widgets.appliancetype.IApplianceTypeView.IApplianceTypePresenter;

@Presenter(view = ApplianceTypeView.class, multiple = true)
public class ApplianceTypePresenter extends BasePresenter<IApplianceTypeView, MainEventBus> implements IApplianceTypePresenter {
	private AggregateApplianceType applianceType;
	private CloudFacadeController cloudFacadeController;
	private boolean developmentMode;

	@Inject
	public ApplianceTypePresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
	}

	public void setApplianceType(AggregateApplianceType applianceType, boolean developmentMode) {
		this.applianceType = applianceType;
		this.developmentMode = developmentMode;
		view.getName().setText(applianceType.getName());

		if (applianceType.getDescription().trim().isEmpty()) {
			view.setEmptyDescription();
		} else {
			view.getDescription().setText(applianceType.getDescription());
		}

		view.clearInitialConfigsContainer();
		view.addInitialConfigsProgressIndicator();
		view.clearInitialConfigsContainer();

		if (applianceType.getInitialConfigurations().size() == 0) {
			view.addNoInitialConfigsLabel();
		} else {
			view.showInitialConfigs();

			for(ApplianceConfiguration config : applianceType.getInitialConfigurations()) {
				view.addInitialConfigValue(config.getId(), config.getName());
			}

			if(!developmentMode) {
				if(applianceType.getFlavor() != null) {
					view.showFlavorInformation(applianceType.getFlavor().getName(),
							applianceType.getFlavor().getHourlyCost());
				} else {
					view.showNoFlavorInformation();
				}
			}

			if(applianceType.getComputeSiteIds() != null &&
					applianceType.getComputeSiteIds().size() > 0) {
				view.enableControls(true);

				if(applianceType.getComputeSites().size() > 1) {
					view.showComputeSiteProgressIndicator(false);
					view.showComputeSiteSelector();
					view.addComputeSite("0", view.getAnyComputeSiteLabel());

					for(ComputeSite computeSite : applianceType.getComputeSites().values()) {
						view.addComputeSite(computeSite.getId(), computeSite.getName());
					}
				} else {
					view.showSingleComputeSiteLabel(applianceType.getComputeSites().values().iterator().next().getName());
				}
			} else {
				view.showComputeSiteProgressIndicator(false);
				view.showNoComputeSitesMessage();
			}
		}
	}

	@Override
	public void onStartApplianceType() {
		view.enableStartButton(false);
		String initialConfigurationId = view.getInitialConfigs().getValue();
		eventBus.hideStartInstanceModal();
		eventBus.startApplications(asList(initialConfigurationId), collectComputeSiteIds(),
				developmentMode);
	}

	public String getSelectedInitialConfigId() {
		if(view.getChecked().getValue()) {
			return view.getInitialConfigs().getValue();
		}

		return null;
	}

	public boolean matchesFilter(String filterText) {
		return applianceType.getName() != null && applianceType.getName().toLowerCase().contains(filterText.toLowerCase()) ||
				applianceType.getDescription() != null && applianceType.getDescription().toLowerCase().contains(filterText.toLowerCase());
	}

	public String getSelectedComputeSiteId() {
		return view.getComputeSites().getValue();
	}

	@Override
	public void onComputeSiteChanged() {
		String computeSiteId = view.getComputeSites().getValue();

		if(!computeSiteId.equals("0")) {
			updateFlavorInformation(ApplianceTypePresenter.this.applianceType.getId(),
					ApplianceTypePresenter.this.applianceType.getPreferenceCpu(),
					ApplianceTypePresenter.this.applianceType.getPreferenceMemory(),
					ApplianceTypePresenter.this.applianceType.getPreferenceDisk(), computeSiteId);
		} else {
			updateFlavorInformation(ApplianceTypePresenter.this.applianceType.getId(),
				ApplianceTypePresenter.this.applianceType.getPreferenceCpu(),
				ApplianceTypePresenter.this.applianceType.getPreferenceMemory(),
				ApplianceTypePresenter.this.applianceType.getPreferenceDisk(), null);
		}
	}

	private void updateFlavorInformation(String applianceTypeId, String cpu, String ram,
			String disk, String computeSiteId) {
		view.showFlavorProgress(true);
		cloudFacadeController.getFlavors(applianceTypeId, cpu == null ? "0" : cpu, ram == null ? "0" : ram, disk == null ? "0" : disk, computeSiteId, new FlavorsCallback() {
					@Override
					public void processFlavors(List<Flavor> flavors) {
						view.showFlavorProgress(false);

						Flavor flavor = getCheapest(flavors);

						if(flavor != null) {
							view.showFlavorInformation(flavor.getName(), flavor.getHourlyCost());
						} else {
							view.showFlavorError();
						}
					}
				});
	}

	private Map<String, List<String>> collectComputeSiteIds() {
		Map<String, List<String>> computeSiteIds = new HashMap<>();

		if(applianceType.getComputeSiteIds() != null && applianceType.getComputeSiteIds().size() > 0) {
			if(applianceType.getComputeSiteIds().size() > 1) {
				if(view.getComputeSites().getValue().equals("0")) {
					computeSiteIds.put(view.getInitialConfigs().getValue(), applianceType.getComputeSiteIds());
				} else {
					computeSiteIds.put(view.getInitialConfigs().getValue(), asList(view.getComputeSites().getValue()));
				}
			} else {
				computeSiteIds.put(view.getInitialConfigs().getValue(), applianceType.getComputeSiteIds());
			}
		} else {
			return null;
		}

		return computeSiteIds;
	}

	private Flavor getCheapest(List<Flavor> flavors) {
		Flavor result = null;

		for(Flavor flavor : flavors) {
			if(result == null || flavor.getHourlyCost() < result.getHourlyCost()) {
				result = flavor;
			}
		}

		return result;
	}
}
