package pl.cyfronet.coin.clew.client.widgets.appliancetype;

import java.util.Arrays;
import java.util.List;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceConfigurationsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.FlavorsCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.flavor.Flavor;
import pl.cyfronet.coin.clew.client.widgets.appliancetype.IApplianceTypeView.IApplianceTypePresenter;

import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = ApplianceTypeView.class, multiple = true)
public class ApplianceTypePresenter extends BasePresenter<IApplianceTypeView, MainEventBus> implements IApplianceTypePresenter {
	private ApplianceType applianceType;
	private CloudFacadeController cloudFacadeController;
	private boolean developmentMode;
	
	@Inject
	public ApplianceTypePresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
	}

	public void setApplianceType(ApplianceType applianceType, boolean developmentMode) {
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
		cloudFacadeController.getInitialConfigurations(applianceType.getId(), new ApplianceConfigurationsCallback() {
			@Override
			public void processApplianceConfigurations(List<ApplianceConfiguration> applianceConfigurations) {
				view.clearInitialConfigsContainer();
				
				if (applianceConfigurations.size() == 0) {
					view.addNoInitialConfigsLabel();
				} else {
					view.showInitialConfigs();

					for (ApplianceConfiguration config : applianceConfigurations) {
						view.addInitialConfigValue(config.getId(), config.getName());
					}
					
					view.enableControls(true);
					
					if(!ApplianceTypePresenter.this.developmentMode) {
						updateFlavorInformation(ApplianceTypePresenter.this.applianceType.getId(),
								ApplianceTypePresenter.this.applianceType.getPreferenceCpu(),
								ApplianceTypePresenter.this.applianceType.getPreferenceMemory(),
								ApplianceTypePresenter.this.applianceType.getPreferenceDisk());
					}
				}
			}
		});
	}

	private void updateFlavorInformation(String applianceTypeId, String cpu, String ram,
			String disk) {
		view.showFlavorProgress(true);
		cloudFacadeController.getFlavors(applianceTypeId, cpu == null ? "0" : cpu, ram == null ? "0" : ram, disk == null ? "0" : disk, new FlavorsCallback() {
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

	@Override
	public void onStartApplianceType() {
		String initialConfigurationId = view.getInitialConfigs().getValue();
		eventBus.hideStartInstanceModal();
		eventBus.startApplications(Arrays.asList(new String[] {initialConfigurationId}), developmentMode);
	}

	public String getSelectedInitialConfigId() {
		if (view.getChecked().getValue()) {
			return view.getInitialConfigs().getValue();
		}
		
		return null;
	}

	public boolean matchesFilter(String filterText) {
		return applianceType.getName() != null && applianceType.getName().toLowerCase().contains(filterText.toLowerCase()) ||
				applianceType.getDescription() != null && applianceType.getDescription().toLowerCase().contains(filterText.toLowerCase());
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