package pl.cyfronet.coin.clew.client.controller;

import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfigurationRequestResponse;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfigurationService;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfigurationsResponse;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.NewApplianceConfiguration;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.NewApplianceConfigurationRequest;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstanceRequestResponse;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstanceService;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstancesResponse;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.NewApplianceInstance;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.NewApplianceInstanceRequest;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.ApplianceSet;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.ApplianceSetService;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypeService;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypesResponse;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.NewApplianceType;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

public class CloudFacadeController {
	public interface ApplianceInstancesCallback {
		void processApplianceInstances(List<ApplianceInstance> applianceInstances);
	}
	
	public interface ApplianceTypesCallback {
		void processApplianceTypes(List<ApplianceType> applianceTypes);
	}
	
	public interface ApplianceSetCallback {
		void processApplianceSet(ApplianceSet applianceSet);
	}
	
	public interface ApplianceConfigurationsCallback {
		void processApplianceConfigurations(List<ApplianceConfiguration> applianceConfigrations);
	}
	
	public interface ApplianceConfigurationCallback {
		void processApplianceConfiguration(ApplianceConfiguration applianceConfiguration);
	}
	
	private ApplianceTypeService applianceTypesService;
	private ApplianceInstanceService applianceInstancesService;
	private ApplianceSetService applianceSetService;
	private ApplianceConfigurationService applianceConfigurationService;
	
	@Inject
	public CloudFacadeController(ApplianceTypeService applianceTypesService, ApplianceInstanceService applianceInstancesService,
			ApplianceSetService applianceSetService, ApplianceConfigurationService applianceConfigurationService) {
		this.applianceTypesService = applianceTypesService;
		this.applianceInstancesService = applianceInstancesService;
		this.applianceSetService = applianceSetService;
		this.applianceConfigurationService = applianceConfigurationService;
	}
	
	public void getApplianceTypes(final ApplianceTypesCallback applianceTypesCallback) {
		applianceTypesService.getApplianceTypes(new MethodCallback<ApplianceTypesResponse>() {
			@Override
			public void onSuccess(Method method, ApplianceTypesResponse response) {
				if (applianceTypesCallback != null) {
					applianceTypesCallback.processApplianceTypes(response.getApplianceTypes());
				}
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert(exception.getMessage());
			}
		});
	}

	public void startApplianceTypes(List<String> startIds, final Command command) {
		ensurePortalApplianceSet(new ApplianceSetCallback() {
			@Override
			public void processApplianceSet(ApplianceSet applianceSet) {
				NewApplianceInstance applianceInstance = new NewApplianceInstance();
				applianceInstance.setApplianceSetId(applianceSet.getId());
				NewApplianceInstanceRequest applianceInstanceRequest = new NewApplianceInstanceRequest();
				
				applianceInstancesService.addApplianceInstance(applianceInstanceRequest, new MethodCallback<ApplianceInstanceRequestResponse>() {
					@Override
					public void onFailure(Method method, Throwable exception) {
						Window.alert(exception.getMessage());
					}

					@Override
					public void onSuccess(Method method, ApplianceInstanceRequestResponse response) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
	}

	public void getApplianceInstances(final ApplianceInstancesCallback applianceInstancesCallback) {
		applianceInstancesService.getApplianceInstances(new MethodCallback<ApplianceInstancesResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, ApplianceInstancesResponse response) {
				if (applianceInstancesCallback != null) {
					applianceInstancesCallback.processApplianceInstances(response.getApplianceInstances());
				}
			}
		});
	}

	public void shutdownApplianceInstance(Command afterShutdown) {
		//TODO(DH): handle shutdown
		afterShutdown.execute();
	}

	public void addApplianceType(NewApplianceType newApplianceType, final Command after) {
		
	}
	
	private void ensurePortalApplianceSet(ApplianceSetCallback applianceSetCallback) {
		
	}

	public void getInitialConfigurations(String applianceTypeId, final ApplianceConfigurationsCallback applianceConfigurationsCallback) {
		applianceConfigurationService.getApplianceConfigurations(applianceTypeId, new MethodCallback<ApplianceConfigurationsResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, ApplianceConfigurationsResponse response) {
				if (applianceConfigurationsCallback != null) {
					applianceConfigurationsCallback.processApplianceConfigurations(response.getApplianceConfigurations());
				}
			}
		});
	}

	public void addApplianceConfiguration(String applianceTypeId, String name, String payload, final ApplianceConfigurationCallback applianceConfigurationCallback) {
		NewApplianceConfigurationRequest newApplianceConfigurationRequest = new NewApplianceConfigurationRequest();
		NewApplianceConfiguration applianceConfiguration = new ApplianceConfiguration();
		applianceConfiguration.setApplianceTypeId(applianceTypeId);
		applianceConfiguration.setName(name);
		applianceConfiguration.setPayload(payload);
		newApplianceConfigurationRequest.setApplianceConfiguration(applianceConfiguration);
		applianceConfigurationService.addApplianceConfiguration(newApplianceConfigurationRequest, new MethodCallback<ApplianceConfigurationRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, ApplianceConfigurationRequestResponse response) {
				if (applianceConfigurationCallback != null) {
					applianceConfigurationCallback.processApplianceConfiguration(response.getApplianceConfiguration());
				}
			}
		});
	}
}