package pl.cyfronet.coin.clew.client.widgets.dashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.clew.client.common.BasePresenter;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceConfigurationsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceInstancesCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypesCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance.Status;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;

public class DashboardPresenter extends BasePresenter implements Presenter {
	public interface View extends IsWidget {
		void showStartAppPopup();
		void clearAppsTable();
		void setAppsSpinnerVisible(boolean visible);
		void addStartButton(int i);
		HasValue<Boolean> addCheckButton(int i);
		void setAppVisibility(int i, boolean b);
		void setStartSelectedWidgetBusyState(boolean b);
		void hideStartAppPopup();
		void setStartAppWidgetBusyState(int i, boolean b);
		void addAppDescription(int i, String description);
		void addAppName(int i, String name);
		void setInstanceName(int i, String name);
		void setInstanceIp(int i, String ip);
		void setInstanceActionsAndDetails(int i);
		void setInstanceStatus(int i, Status status);
		void setInstanceSpec(int i, String spec);
		void setInstanceLocation(int i, String location);
		void confirmShutdown(Command command);
		void toggleInstanceDetails(int i);
		HasValue<Boolean> addInitialConfig(int row, String applianceTypeId, String configurationName);
		void enableStartButton(int j, boolean b);
		void enableCheckButton(int j, boolean b);
		void addNoConfigurationMessage(int j);
	}
	
	private final static Logger log = LoggerFactory.getLogger(DashboardPresenter.class);
	
	private View view;
	private CloudFacadeController cloudFacadeController;
	private List<ApplianceType> applianceTypes;
	private List<HasValue<Boolean>> appChecks;
	private List<ApplianceInstance> instances;
	private Map<Integer, Map<String, HasValue<Boolean>>> initialConfigurationRadios;
	
	@Inject
	public DashboardPresenter(View view, CloudFacadeController cloudFacadeController) {
		this.view = view;
		this.cloudFacadeController = cloudFacadeController;
		appChecks = new ArrayList<HasValue<Boolean>>();
		initialConfigurationRadios = new HashMap<Integer, Map<String, HasValue<Boolean>>>();
	}
	
	public void load() {
		cloudFacadeController.getApplianceInstances(new ApplianceInstancesCallback() {
			@Override
			public void processApplianceInstances(List<ApplianceInstance> applianceInstances) {
				instances = applianceInstances;
				int i = 0;
				
				for (ApplianceInstance asi : instances) {
					view.setInstanceName(i, asi.getId());
//					view.setInstanceName(i, asi.getName());
//					view.setInstanceIp(i, asi.getIp());
//					view.setInstanceLocation(i, asi.getLocation());
//					view.setInstanceSpec(i, asi.getSpec());
//					view.setInstanceStatus(i, asi.getStatus());
					view.setInstanceActionsAndDetails(i);
					i++;
				}
			}
		});
	}
	
	@Override
	public IsWidget getWidget() {
		return view;
	}

	@Override
	public void onShowStartAppModal() {
		view.clearAppsTable();
		view.setAppsSpinnerVisible(true);
		view.showStartAppPopup();
		cloudFacadeController.getApplianceTypes(new ApplianceTypesCallback() {
			@Override
			public void processApplianceTypes(List<ApplianceType> applianceTypes) {
				DashboardPresenter.this.applianceTypes = applianceTypes;
				view.setAppsSpinnerVisible(false);
				
				int i = 0;
				appChecks.clear();
				
				for(final ApplianceType applianceType : applianceTypes) {
					view.addStartButton(i);
					appChecks.add(view.addCheckButton(i));
					view.addAppName(i, applianceType.getName());
					view.addAppDescription(i, applianceType.getDescription());
					
					final int j = i;
					cloudFacadeController.getInitialConfigurations(applianceType.getId(), new ApplianceConfigurationsCallback() {
						@Override
						public void processApplianceConfigurations(List<ApplianceConfiguration> applianceConfigrations) {
							if (applianceConfigrations.size() == 0) {
								view.enableStartButton(j, false);
								view.enableCheckButton(j, false);
								view.addNoConfigurationMessage(j);
							} else {
								boolean firstChecked = false;
								Map<String, HasValue<Boolean>> configs = new HashMap<String, HasValue<Boolean>>();
								
								for (ApplianceConfiguration applianceConfiguration : applianceConfigrations) {
									HasValue<Boolean> check = view.addInitialConfig(j, applianceType.getId(), applianceConfiguration.getName());
									configs.put(applianceConfiguration.getId(), check);
									
									if (!firstChecked) {
										firstChecked = true;
										check.setValue(true);
									}
								}
								
								initialConfigurationRadios.put(j, configs);
							}
						}
					});
					i++;
				}
			}
		});
	}

	@Override
	public void onFilter(String text) {
		log.debug("Filtering apps for {}", text);
		int i = 0;
		
		for (ApplianceType applianceType : applianceTypes) {
			if (applianceType.getName() != null && applianceType.getName().contains(text) ||
					applianceType.getDescription() != null && applianceType.getDescription().contains(text)) {
				view.setAppVisibility(i, true);
			} else {
				view.setAppVisibility(i, false);
			}
			
			i++;
		}
	}

	@Override
	public void onStartSelected() {
		List<String> configurationTemplateIds = new ArrayList<String>();
		int i = 0;
		
		for (HasValue<Boolean> selected : appChecks) {
			if (selected.getValue()) {
				for (String initialConfigurationId : initialConfigurationRadios.get(i).keySet()) {
					if (initialConfigurationRadios.get(i).get(initialConfigurationId).getValue()) {
						configurationTemplateIds.add(applianceTypes.get(i).getId());
					}
				}
			}
			
			i++;
		}
		
		log.info("Starting appliance types with initial configuration ids {}", configurationTemplateIds);
		view.setStartSelectedWidgetBusyState(true);
		cloudFacadeController.startApplianceTypes(configurationTemplateIds, new Command() {
			@Override
			public void execute() {
				view.setStartSelectedWidgetBusyState(false);
				view.hideStartAppPopup();
			}
		});
	}

	@Override
	public void onStartSingle(final int i) {
		log.info("Starting single appliance type {}", applianceTypes.get(i).getId());
		view.setStartAppWidgetBusyState(i, true);
		log.info("Initial configuration: {}", initialConfigurationRadios);
		
		for (String initialConfigurationId : initialConfigurationRadios.get(i).keySet()) {
			if (initialConfigurationRadios.get(i).get(initialConfigurationId).getValue()) {
				cloudFacadeController.startApplianceTypes(
						Arrays.asList(initialConfigurationId), new Command() {
							@Override
							public void execute() {
								view.setStartAppWidgetBusyState(i, false);
								view.hideStartAppPopup();
							}
						});
				break;
			}
		}
	}

	@Override
	public void onInstanceDetailsShow(int i) {
		view.toggleInstanceDetails(i);
	}

	@Override
	public void onInstanceShutdown(final int instanceIndex) {
		view.confirmShutdown(new Command() {
			@Override
			public void execute() {
				cloudFacadeController.shutdownApplianceInstance(instances.get(instanceIndex).getId(), new Command() {
					@Override
					public void execute() {
						
					}
				});
			}});
	}
}