package pl.cyfronet.coin.clew.client.widgets.appliancedetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.cyfronet.coin.clew.client.ClewProperties;
import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceConfigurationsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypesCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ComputeSitesCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.FlavorsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.UserKeysCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.computesite.ComputeSite;
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
	private Map<String, HasValue<String>> pickedComputeSites;

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
		pickedComputeSites = new HashMap<String, HasValue<String>>();
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
		
		loadKeysAndNamesAndShowModal(initialConfigurationIds);
	}

	public void onShowApplianceStartDetailsEditorForConfigParams(Map<String, Map<String, String>> parameterValues, Map<String, List<String>> computeSiteIds) {
		this.parameterValues = parameterValues;
		this.computeSiteIds = computeSiteIds;
		loadKeysAndNamesAndShowModal(new ArrayList<String>(parameterValues.keySet()));
	}

	private void loadKeysAndNamesAndShowModal(List<String> initialConfigurationIds) {
		view.getNameContainer().clear();
		names.clear();
		view.getKeyContainer().clear();
		view.showKeyProgress(true);
		view.showDetailsProgress(true);
		view.showModal(true);
		cloudFacadeController.getInitialConfigurations(initialConfigurationIds, new ApplianceConfigurationsCallback() {
			@Override
			public void processApplianceConfigurations(final List<ApplianceConfiguration> applianceConfigurations) {
				cloudFacadeController.getApplianceTypes(collectApplianceTypeIds(applianceConfigurations), new ApplianceTypesCallback() {
					@Override
					public void processApplianceTypes(final List<ApplianceType> applianceTypes) {
						if(computeSiteSelectionRequired(applianceTypes)) {
							cloudFacadeController.getComputeSites(collectComputeSiteIds(applianceTypes), new ComputeSitesCallback() {
								@Override
								public void processComputeSites(List<ComputeSite> computeSites) {
									view.showDetailsProgress(false);
									
									for (ApplianceType applianceType : applianceTypes) { 
										addDetails(applianceConfigurations, applianceType, computeSites);
									}
								}
							});
						} else {
							view.showDetailsProgress(false);
							
							for (ApplianceType applianceType : applianceTypes) { 
								addDetails(applianceConfigurations, applianceType, null);
							}
						}
					}
				});
			}});
		cloudFacadeController.getUserKeys(new UserKeysCallback() {
			@Override
			public void processUserKeys(List<UserKey> userKeys) {
				view.showKeyProgress(false);
				
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
	
	private void addDetails(List<ApplianceConfiguration> applianceConfigurations, ApplianceType applianceType, List<ComputeSite> computeSites) {
		String initialConfigId = getInitialConfigId(applianceType.getId(), applianceConfigurations);
		names.put(initialConfigId, view.addName(applianceType.getName()));
		cores.put(initialConfigId, view.addCores(getOptions(properties.coreOptions()),
				safeValue(applianceType.getPreferenceCpu()), applianceType.getId()));
		rams.put(initialConfigId, view.addRam(getOptions(properties.ramOptions()),
				safeValue(applianceType.getPreferenceMemory()), applianceType.getId()));
		disks.put(initialConfigId, view.addDisk(getOptions(properties.diskOptions()),
				safeValue(applianceType.getPreferenceDisk()), applianceType.getId()));
		flavorContainers.put(applianceType.getId(), view.addFlavorContainer());
		applianceTypeIdToInitialConfigIdMapping.put(applianceType.getId(), initialConfigId);
		updateFlavorDetails(applianceType.getId(), applianceType.getPreferenceCpu(), applianceType.getPreferenceMemory(), applianceType.getPreferenceDisk());
		
		if(applianceType.getComputeSiteIds() != null && applianceType.getComputeSiteIds().size() > 1) {
			String chosenComputeSite = null;
			
			if(computeSiteIds != null && computeSiteIds.get(initialConfigId) != null && computeSiteIds.get(initialConfigId).size() == 1) {
				chosenComputeSite = computeSiteIds.get(initialConfigId).get(0);
			}
			
			pickedComputeSites.put(initialConfigId, view.addComputeSites(labelComputeSites(computeSites), chosenComputeSite));
		}
	}
	
	private Map<String, String> labelComputeSites(List<ComputeSite> computeSites) {
		Map<String, String> result = new LinkedHashMap<String, String>();
		result.put("0", view.getAnyComputeSiteLabel());
		
		for(ComputeSite computeSite : computeSites) {
			result.put(computeSite.getId(), computeSite.getName());
		}
		
		return result;
	}

	private List<String> collectComputeSiteIds(List<ApplianceType> applianceTypes) {
		Set<String> result = new HashSet<String>();
		
		for(ApplianceType applianceType : applianceTypes) {
			result.addAll(applianceType.getComputeSiteIds());
		}
		
		return new ArrayList<String>(result);
	}
	
	private boolean computeSiteSelectionRequired(List<ApplianceType> applianceTypes) {
		for(ApplianceType applianceType : applianceTypes) {
			if(applianceType.getComputeSiteIds() != null && applianceType.getComputeSiteIds().size() > 1) {
				return true;
			}
		}
		
		return false;
	}
	
	private void updateFlavorDetails(final String applianceTypeId, String cpu, String ram, String disk) {
		if(!isValid(cpu) || !isValid(ram) || !isValid(disk)) {
			view.showPreferencesError(flavorContainers.get(applianceTypeId));
			
			return;
		}
		
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
	
	private boolean isValid(String amount) {
		if(amount != null) {
			try {
				if(Integer.valueOf(amount) < 0) {
					return false;
				}
			} catch(NumberFormatException e) {
				return false;
			}
		}
		
		return true;
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
		
		for(String id : keys.keySet()) {
			if(keys.get(id).getValue()) {
				keyId = id;
			}
		}
		
		Map<String, String> overrideNames = new HashMap<String, String>();
		
		for(String initialConfigId : names.keySet()) {
			if(!names.get(initialConfigId).getText().trim().isEmpty()) {
				overrideNames.put(initialConfigId, names.get(initialConfigId).getText().trim());
			}
		}
		
		Map<String, String> cores = createPreferenceMapping(this.cores);
		Map<String, String> rams = createPreferenceMapping(this.rams);
		Map<String, String> disks = createPreferenceMapping(this.disks);
		updateComputeSites();
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
	
	private void updateComputeSites() {
		if(computeSiteIds == null) {
			computeSiteIds = new HashMap<String, List<String>>();
		}
		
		for(String initialConfigID : pickedComputeSites.keySet()) {
			if(!pickedComputeSites.get(initialConfigID).getValue().equals("0")) {
				List<String> computeSiteId = new ArrayList<String>();
				computeSiteId.add(pickedComputeSites.get(initialConfigID).getValue());
				computeSiteIds.put(initialConfigID, computeSiteId );
			}
		}
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
	
	private String safeValue(String value) {
		if(value == null) {
			return "0";
		} else {
			return value;
		}
	}
}