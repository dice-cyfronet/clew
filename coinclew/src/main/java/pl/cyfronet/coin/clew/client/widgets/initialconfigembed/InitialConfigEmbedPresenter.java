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

	@Inject
	public InitialConfigEmbedPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
		params = new HashMap<String, Map<String,HasText>>();
	}
	
	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onStartApplications(final List<String> initialConfigurationIds) {
		cloudFacadeController.getInitialConfigurations(initialConfigurationIds, new ApplianceConfigurationsCallback() {
			@Override
			public void processApplianceConfigurations(final List<ApplianceConfiguration> applianceConfigurations) {
				boolean parametersPresent = false;
				
				for (ApplianceConfiguration config : applianceConfigurations) {
					if (config.getParameters().size() > 0) {
						parametersPresent = true;
						
						break;
					}
				}
				
				if (parametersPresent) {
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
					cloudFacadeController.startApplianceTypes(initialConfigurationIds, new Command() {
						@Override
						public void execute() {
							eventBus.refreshInstanceList();
						}
					});
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
		
		cloudFacadeController.startApplianceTypes(parameterValues, new Command() {
			@Override
			public void execute() {
				view.showModal(false);
				eventBus.refreshInstanceList();
			}
		});
	}
	
	private List<String> collectApplianceTypeIds(List<ApplianceConfiguration> applianceConfigurations) {
		List<String> result = new ArrayList<String>();
		
		for (ApplianceConfiguration config : applianceConfigurations) {
			result.add(config.getApplianceTypeId());
		}
		
		return result;
	}
}