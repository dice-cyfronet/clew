package pl.cyfronet.coin.clew.client.widgets.initialconfigembed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceConfigurationsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypesCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.widgets.initialconfigembed.IInitialConfigEmbedView.IInitialConfigEmbedPresenter;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasText;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = InitialConfigEmbedView.class)
public class InitialConfigEmbedPresenter extends BasePresenter<IInitialConfigEmbedView, MainEventBus> implements IInitialConfigEmbedPresenter {
	private CloudFacadeController cloudFacadeController;
	private Map<String, Map<String, HasText>> params;
	private boolean developmentMode;
	private Map<String, List<String>> computeSiteIds;
	private Map<String, String> teams;

	@Inject
	public InitialConfigEmbedPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
		params = new HashMap<String, Map<String,HasText>>();
	}
	
	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onStartApplications(final List<String> initialConfigurationIds, final Map<String, List<String>> computeSiteIds, boolean developmentMode,
			final Map<String, String> teams) {
		this.developmentMode = developmentMode;
		this.computeSiteIds = computeSiteIds;
		this.teams = teams;
		eventBus.showStartApplicationProgress(true);
		cloudFacadeController.getInitialConfigurations(initialConfigurationIds, new ApplianceConfigurationsCallback() {
			@Override
			public void processApplianceConfigurations(final List<ApplianceConfiguration> applianceConfigurations) {
				boolean parametersPresent = false;
				
				for(ApplianceConfiguration config : applianceConfigurations) {
					if (config.getParameters().size() > 0) {
						parametersPresent = true;
						
						break;
					}
				}
				
				if(parametersPresent) {
					eventBus.showStartApplicationProgress(false);
					view.clearParameters();
					params.clear();
					view.showLoadingProgress(true);
					view.showModal(true);
					cloudFacadeController.getApplianceTypes(collectApplianceTypeIds(applianceConfigurations), new ApplianceTypesCallback() {
						@Override
						public void processApplianceTypes(List<ApplianceType> applianceTypes) {
							view.showLoadingProgress(false);
							
							for (ApplianceConfiguration config : applianceConfigurations) {
								ApplianceType applianceType = getAppliance(config.getApplianceTypeId(), applianceTypes);
								Map<String, HasText> paramValues = view.addParameters(applianceType.getName(), config.getName(), config.getParameters());
								params.put(config.getId(), paramValues);
							}
						}

						private ApplianceType getAppliance(String applianceTypeId, List<ApplianceType> applianceTypes) {
							for (ApplianceType applianceType : applianceTypes) {
								if (applianceType.getId().equals(applianceTypeId)) {
									return applianceType;
								}
							}
							
							return null;
						}
					});
				} else {
					if (InitialConfigEmbedPresenter.this.developmentMode) {
						eventBus.showStartApplicationProgress(false);
						eventBus.showApplianceStartDetailsEditorForConfigIds(initialConfigurationIds, computeSiteIds, teams);
					} else {
						cloudFacadeController.startApplianceTypes(initialConfigurationIds, computeSiteIds, teams, new Command() {
							@Override
							public void execute() {
								eventBus.showStartApplicationProgress(false);
								eventBus.refreshInstanceList();
							}
						});
					}
				}
			}});
	}
	
	@Override
	public void onStartApplications() {
		Map<String, Map<String, String>> parameterValues = new HashMap<String, Map<String, String>>();
		
		for (String configId : params.keySet()) {
			Map<String, String> values = new HashMap<String, String>();
			
			for (String parameterName : params.get(configId).keySet()) {
				values.put(parameterName, params.get(configId).get(parameterName).getText());
			}
			
			parameterValues.put(configId, values);
		}
		
		if (developmentMode) {
			view.showModal(false);
			eventBus.showApplianceStartDetailsEditorForConfigParams(parameterValues, computeSiteIds, teams);
		} else {
			view.setStartBusyState(true);
			cloudFacadeController.startApplianceTypes(parameterValues, computeSiteIds, new Command() {
				@Override
				public void execute() {
					view.setStartBusyState(false);
					view.showModal(false);
					eventBus.refreshInstanceList();
				}
			});
		}
	}
	
	private List<String> collectApplianceTypeIds(List<ApplianceConfiguration> applianceConfigurations) {
		List<String> result = new ArrayList<String>();
		
		for (ApplianceConfiguration config : applianceConfigurations) {
			result.add(config.getApplianceTypeId());
		}
		
		return result;
	}
}