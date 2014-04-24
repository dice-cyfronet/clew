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
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.FlavorsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.UserKeysCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.flavor.Flavor;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.UserKey;
import pl.cyfronet.coin.clew.client.widgets.appliancedetails.IApplianceDetailsView.IApplianceDetailsPresenter;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
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
	private Map<String, HasWidgets> flavorContainers;
	private Map<String, String> applianceTypeIdToInitialConfigIdMapping;
	private Map<String, List<String>> computeSiteIds;

	@Inject
	public ApplianceDetailsPresenter(CloudFacadeController cloudFacadeController, ClewProperties properties) {
		this.cloudFacadeController = cloudFacadeController;
		this.properties = properties;
		keys = new HashMap<String, HasValue<Boolean>>();
		names = new HashMap<String, HasText>();
		cores = new HashMap<String, HasValue<String>>();
		rams = new HashMap<String, HasValue<String>>();
		disks = new HashMap<String, HasValue<String>>();
		flavorContainers = new HashMap<String, HasWidgets>();
		applianceTypeIdToInitialConfigIdMapping = new HashMap<String, String>();
	}
	
	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onShowApplianceStartDetailsEditorForConfigIds(List<String> initialConfigurationIds, Map<String, List<String>> computeSiteIds) {
		this.computeSiteIds = computeSiteIds;
		parameterValues = new HashMap<String, Map<String, String>>();
		
		for (String initialConfigurationId : initialConfigurationIds) {
			parameterValues.put(initialConfigurationId, new HashMap<String, String>());
		}
		
		view.showModal(true);
		loadKeysAndNames(initialConfigurationIds);
	}

	public void onShowApplianceStartDetailsEditorForConfigParams(Map<String, Map<String, String>> parameterValues, Map<String, List<String>> computeSiteIds) {
		this.parameterValues = parameterValues;
		this.computeSiteIds = computeSiteIds;
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
							cores.put(initialConfigId, view.addCores(getOptions(properties.coreOptions()), applianceType.getPreferenceCpu(), applianceType.getId()));
							rams.put(initialConfigId, view.addRam(getOptions(properties.ramOptions()), applianceType.getPreferenceMemory(), applianceType.getId()));
							disks.put(initialConfigId, view.addDisk(getOptions(properties.diskOptions()), applianceType.getPreferenceDisk(), applianceType.getId()));
							flavorContainers.put(applianceType.getId(), view.addFlavorContainer());
							applianceTypeIdToInitialConfigIdMapping.put(applianceType.getId(), initialConfigId);
							updateFlavorDetails(applianceType.getId(), applianceType.getPreferenceCpu(), applianceType.getPreferenceMemory(), applianceType.getPreferenceDisk());
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
	
	private void updateFlavorDetails(final String applianceTypeId, String cpu, String ram, String disk) {
		view.showFlavorProgress(flavorContainers.get(applianceTypeId), true);
		cloudFacadeController.getFlavors(applianceTypeId, cpu == null ? "0" : cpu,
				ram == null ? "0" : ram, disk == null ? "0" : disk, null, new FlavorsCallback() {
					@Override
					public void processFlavors(List<Flavor> flavors) {
						view.showFlavorProgress(flavorContainers.get(applianceTypeId), false);
						
						Flavor flavor = getCheapest(flavors);
						
						if(flavor != null) {
							view.showFlavorInformation(flavorContainers.get(applianceTypeId), flavor.getName(), flavor.getHourlyCost());
						} else {
							view.showFlavorError(flavorContainers.get(applianceTypeId));
						}
					}
				});
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
		cloudFacadeController.startApplianceTypesInDevelopment(overrideNames, keyId, parameterValues, cores, rams, disks, computeSiteIds, new Command() {
			@Override
			public void execute() {
				view.setStartBusyState(false);
				view.showModal(false);
				eventBus.refreshDevelopmentInstanceList();
			}
		});
	}
	
	@Override
	public void onPreferenceChanged(String applianceTypeId) {
		String initialConfigId = applianceTypeIdToInitialConfigIdMapping.get(applianceTypeId);
		updateFlavorDetails(applianceTypeId, cores.get(initialConfigId).getValue(), rams.get(initialConfigId).getValue(),
				disks.get(initialConfigId).getValue());
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