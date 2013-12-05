package pl.cyfronet.coin.clew.client.widgets.appliancedetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceConfigurationsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypesCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.UserKeysCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.UserKey;
import pl.cyfronet.coin.clew.client.widgets.appliancedetails.IApplianceDetailsView.IApplianceDetailsPresenter;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = ApplianceDetailsView.class)
public class ApplianceDetailsPresenter extends BasePresenter<IApplianceDetailsView, MainEventBus> implements IApplianceDetailsPresenter {
	private CloudFacadeController cloudFacadeController;
	private Map<String, HasValue<Boolean>> keys;
	private Map<String, Map<String, String>> parameterValues;
	private Map<String, HasText> names;

	@Inject
	public ApplianceDetailsPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
		keys = new HashMap<String, HasValue<Boolean>>();
		names = new HashMap<String, HasText>();
	}
	
	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onShowApplianceStartDetailsEditorForConfigIds(List<String> initialConfigurationIds) {
		parameterValues = new HashMap<String, Map<String, String>>();
		
		for (String initialConfigurationId : initialConfigurationIds) {
			parameterValues.put(initialConfigurationId, new HashMap<String, String>());
		}
		
		view.showModal(true);
		loadKeysAndNames(initialConfigurationIds);
	}

	public void onShowApplianceStartDetailsEditorForConfigParams(Map<String, Map<String, String>> parameterValues) {
		this.parameterValues = parameterValues;
		view.showModal(true);
		loadKeysAndNames(new ArrayList<String>(parameterValues.keySet()));
	}

	private void loadKeysAndNames(List<String> initialConfigurationIds) {
		view.getContainer().clear();
		cloudFacadeController.getInitialConfigurations(initialConfigurationIds, new ApplianceConfigurationsCallback() {
			@Override
			public void processApplianceConfigurations(final List<ApplianceConfiguration> applianceConfigurations) {
				cloudFacadeController.getApplianceTypes(collectApplianceTypeIds(applianceConfigurations), new ApplianceTypesCallback() {
					@Override
					public void processApplianceTypes(List<ApplianceType> applianceTypes) {
						for (ApplianceType applianceType : applianceTypes) {
							names.put(getInitialConfigId(applianceType.getId(), applianceConfigurations),
									view.addName(applianceType.getName()));
						}
					}
				});
			}});
		cloudFacadeController.getUserKeys(new UserKeysCallback() {
			@Override
			public void processUserKeys(List<UserKey> userKeys) {
				boolean firstSet = false;
				
				for (UserKey userKey : userKeys) {
					keys.put(userKey.getId(), view.addKey(userKey.getId(), userKey.getName()));
					
					if (!firstSet) {
						keys.get(userKey.getId()).setValue(true);
						firstSet = true;
					}
				}
			}
		});
	}

	@Override
	public void onStartInstance() {
		String keyId = null;
		
		for (String id : keys.keySet()) {
			if (keys.get(id).getValue()) {
				keyId = id;
			}
		}
		
		Map<String, String> overrideNames = new HashMap<String, String>();
		
		for (String initialConfigId : names.keySet()) {
			if (!names.get(initialConfigId).getText().trim().isEmpty()) {
				overrideNames.put(initialConfigId, names.get(initialConfigId).getText().trim());
			}
		}
		
		view.setStartBusyState(true);
		cloudFacadeController.startApplianceTypesInDevelopment(overrideNames, keyId, parameterValues, new Command() {
			@Override
			public void execute() {
				view.setStartBusyState(false);
				view.showModal(false);
				eventBus.refreshDevelopmentInstanceList();
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
	
	private String getInitialConfigId(String applianceTypeId, List<ApplianceConfiguration> applianceConfigurations) {
		for (ApplianceConfiguration applianceConfiguration : applianceConfigurations) {
			if (applianceConfiguration.getApplianceTypeId().equals(applianceTypeId)) {
				return applianceConfiguration.getId();
			}
		}
		
		return null;
	}
}