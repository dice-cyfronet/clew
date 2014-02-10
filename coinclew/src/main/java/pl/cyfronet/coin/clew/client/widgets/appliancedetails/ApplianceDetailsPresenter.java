package pl.cyfronet.coin.clew.client.widgets.appliancedetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.ClewProperties;
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
	private ClewProperties properties;
	private Map<String, HasValue<String>> cores;
	private Map<String, HasValue<String>> rams;
	private Map<String, HasValue<String>> disks;

	@Inject
	public ApplianceDetailsPresenter(CloudFacadeController cloudFacadeController, ClewProperties properties) {
		this.cloudFacadeController = cloudFacadeController;
		this.properties = properties;
		keys = new HashMap<String, HasValue<Boolean>>();
		names = new HashMap<String, HasText>();
		cores = new HashMap<String, HasValue<String>>();
		rams = new HashMap<String, HasValue<String>>();
		disks = new HashMap<String, HasValue<String>>();
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
		view.getNameContainer().clear();
		names.clear();
		cloudFacadeController.getInitialConfigurations(initialConfigurationIds, new ApplianceConfigurationsCallback() {
			@Override
			public void processApplianceConfigurations(final List<ApplianceConfiguration> applianceConfigurations) {
				cloudFacadeController.getApplianceTypes(collectApplianceTypeIds(applianceConfigurations), new ApplianceTypesCallback() {
					@Override
					public void processApplianceTypes(List<ApplianceType> applianceTypes) {
						for (ApplianceType applianceType : applianceTypes) { 
							String initialConfigId = getInitialConfigId(applianceType.getId(), applianceConfigurations);
							names.put(initialConfigId, view.addName(applianceType.getName()));
							cores.put(initialConfigId, view.addCores(getOptions(properties.coreOptions())));
							rams.put(initialConfigId, view.addRam(getOptions(properties.ramOptions())));
							disks.put(initialConfigId, view.addDisk(getOptions(properties.diskOptions())));
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
		
		Map<String, String> cores = createPreferenceMapping(this.cores);
		Map<String, String> rams = createPreferenceMapping(this.rams);
		Map<String, String> disks = createPreferenceMapping(this.disks);
		
		view.setStartBusyState(true);
		cloudFacadeController.startApplianceTypesInDevelopment(overrideNames, keyId, parameterValues, cores, rams, disks, new Command() {
			@Override
			public void execute() {
				view.setStartBusyState(false);
				view.showModal(false);
				eventBus.refreshDevelopmentInstanceList();
			}
		});
	}
	
	private Map<String, String> createPreferenceMapping(Map<String, HasValue<String>> preferences) {
		Map<String, String> result = new HashMap<String, String>();
		
		for(String id : preferences.keySet()) {
			result.put(id, preferences.get(id).getValue());
		}
		
		return result;
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
	
	private Map<String, String> getOptions(String[] values) {
		Map<String, String> result = new LinkedHashMap<String, String>();
		
		for(String value : values) {
			result.put(value, value.equals("0") ? view.getDefaultValueLabel() : value);
		}
		
		return result;
	}
}