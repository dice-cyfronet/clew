package pl.cyfronet.coin.clew.client.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.ApplianceSetRequestResponse;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.ApplianceSetService;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.ApplianceSetsResponse;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.NewApplianceSet;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.NewApplianceSet.Type;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.NewApplianceSetRequest;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypeRequestResponse;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypeService;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypesResponse;
import pl.cyfronet.coin.clew.client.controller.cf.appliancevm.ApplianceVm;
import pl.cyfronet.coin.clew.client.controller.cf.appliancevm.ApplianceVmService;
import pl.cyfronet.coin.clew.client.controller.cf.appliancevm.ApplianceVmsResponse;
import pl.cyfronet.coin.clew.client.controller.cf.computesite.ComputeSite;
import pl.cyfronet.coin.clew.client.controller.cf.computesite.ComputeSiteRequestResponse;
import pl.cyfronet.coin.clew.client.controller.cf.computesite.ComputeSiteService;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.Endpoint;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.EndpointService;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.EndpointsResponse;
import pl.cyfronet.coin.clew.client.controller.cf.httpmapping.HttpMapping;
import pl.cyfronet.coin.clew.client.controller.cf.httpmapping.HttpMappingResponse;
import pl.cyfronet.coin.clew.client.controller.cf.httpmapping.HttpMappingService;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate.PortMappingTemplate;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate.PortMappingTemplateService;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate.PortMappingTemplatesResponse;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.NewUserKey;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.NewUserKeyRequest;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.UserKey;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.UserKeyRequestResponse;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.UserKeyService;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.UserKeysResponse;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CloudFacadeController {
	public abstract static class ApplianceInstancesCallback {
		public abstract void processApplianceInstances(List<ApplianceInstance> applianceInstances);
		
		protected void onError(Throwable e) {
			popupErrorHandler.displayError(e.getMessage());
		}
	}
	
	public interface ApplianceTypesCallback {
		void processApplianceTypes(List<ApplianceType> applianceTypes);
	}
	
	public interface ApplianceTypeCallback {
		void processApplianceType(ApplianceType applianceType);
	}
	
	public interface ApplianceSetCallback {
		void processApplianceSet(ApplianceSet applianceSet);
	}
	
	public interface ApplianceSetsCallback {
		void processApplianceSet(List<ApplianceSet> applianceSets);
	}
	
	public interface ApplianceConfigurationsCallback {
		void processApplianceConfigurations(List<ApplianceConfiguration> applianceConfigurations);
	}
	
	public interface ApplianceConfigurationCallback {
		void processApplianceConfiguration(ApplianceConfiguration applianceConfiguration);
	}
	
	public interface ApplianceVmsCallback {
		void processApplianceVms(List<ApplianceVm> applianceVms);
	}
	
	public interface ComputeSiteCallback {
		void processComputeSite(ComputeSite computeSite);
	}
	
	public interface PortMappingTemplatesCallback {
		void processPortMappingTemplates(List<PortMappingTemplate> portMappingTemplates);
	}
	
	public interface HttpMappingsCallback {
		void processHttpMappings(List<HttpMapping> httpMappings);
	}
	
	public interface EndpointsCallback {
		void processEndpoints(List<Endpoint> endpoints);
	}
	
	public interface UserKeysCallback {
		void processUserKeys(List<UserKey> userKeys);
	}
	
	public interface KeyUploadCallback {
		void onSuccess(UserKey userKey);
		void onError(String errorMessage);
	}
	
	private ApplianceTypeService applianceTypesService;
	private ApplianceInstanceService applianceInstancesService;
	private ApplianceSetService applianceSetService;
	private ApplianceConfigurationService applianceConfigurationService;
	private static PopupErrorHandler popupErrorHandler;
	private ApplianceVmService applianceVmsService;
	private ComputeSiteService computeSitesService;
	private PortMappingTemplateService portMappingTemplateService;
	private HttpMappingService httpMappingService;
	private EndpointService endpointService;
	private UserKeyService userKeyService;
	
	@Inject
	public CloudFacadeController(ApplianceTypeService applianceTypesService, ApplianceInstanceService applianceInstancesService,
			ApplianceSetService applianceSetService, ApplianceConfigurationService applianceConfigurationService,
			ApplianceVmService applianceVmsService, ComputeSiteService computeSitesService,
			PortMappingTemplateService portMappingTemplateService, HttpMappingService httpMappingService,
			EndpointService endpointService, UserKeyService userKeyService, PopupErrorHandler popupErrorHandler) {
		this.applianceTypesService = applianceTypesService;
		this.applianceInstancesService = applianceInstancesService;
		this.applianceSetService = applianceSetService;
		this.applianceConfigurationService = applianceConfigurationService;
		this.applianceVmsService = applianceVmsService;
		this.computeSitesService = computeSitesService;
		this.portMappingTemplateService = portMappingTemplateService;
		this.httpMappingService = httpMappingService;
		this.endpointService = endpointService;
		this.userKeyService = userKeyService;
		CloudFacadeController.popupErrorHandler = popupErrorHandler;
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
				popupErrorHandler.displayError(exception.getMessage());
			}
		});
	}
	
	public void getApplianceType(String applianceTypeId, final ApplianceTypeCallback applianceTypeCallback) {
		applianceTypesService.getApplianceType(applianceTypeId, new MethodCallback<ApplianceTypeRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, ApplianceTypeRequestResponse response) {
				if (applianceTypeCallback != null) {
					applianceTypeCallback.processApplianceType(response.getApplianceType());
				}
			}
		});
	}

	public void startApplianceTypes(final List<String> configurationTemplateIds, final Command command) {
		ensurePortalApplianceSet(new ApplianceSetCallback() {
			@Override
			public void processApplianceSet(ApplianceSet applianceSet) {
				final List<String> started = new ArrayList<String>();
				final List<String> failed = new ArrayList<String>();
				
				for (final String configurationTemplateId : configurationTemplateIds) {
					NewApplianceInstance applianceInstance = new NewApplianceInstance();
					applianceInstance.setApplianceSetId(applianceSet.getId());
					applianceInstance.setConfigurationTemplateId(configurationTemplateId);
					NewApplianceInstanceRequest applianceInstanceRequest = new NewApplianceInstanceRequest();
					applianceInstanceRequest.setApplianceInstance(applianceInstance);
					applianceInstancesService.addApplianceInstance(applianceInstanceRequest, new MethodCallback<ApplianceInstanceRequestResponse>() {
						@Override
						public void onFailure(Method method, Throwable exception) {
							failed.add(configurationTemplateId);
							popupErrorHandler.displayError(exception.getMessage());
							checkReturn();
						}

						@Override
						public void onSuccess(Method method, ApplianceInstanceRequestResponse response) {
							started.add(configurationTemplateId);
							checkReturn();
						}
						
						private void checkReturn() {
							if (started.size() + failed.size() == configurationTemplateIds.size() && command != null) {
								command.execute();
							}
						}
					});
				}
			}
		});
	}

	public void getApplianceInstances(final ApplianceInstancesCallback applianceInstancesCallback) {
		applianceInstancesService.getApplianceInstances(new MethodCallback<ApplianceInstancesResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				if (applianceInstancesCallback != null) {
					applianceInstancesCallback.onError(exception);
				}
			}

			@Override
			public void onSuccess(Method method, ApplianceInstancesResponse response) {
				if (applianceInstancesCallback != null) {
					applianceInstancesCallback.processApplianceInstances(response.getApplianceInstances());
				}
			}
		});
	}

	public void shutdownApplianceInstance(String applianceInstanceId, final Command afterShutdown) {
		applianceInstancesService.deleteApplianceInstance(applianceInstanceId, new MethodCallback<Void>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, Void response) {
				if (afterShutdown != null) {
					afterShutdown.execute();
				}
			}
		});
	}
	
	private void ensurePortalApplianceSet(final ApplianceSetCallback applianceSetCallback) {
		applianceSetService.getApplianceSets(new MethodCallback<ApplianceSetsResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, ApplianceSetsResponse response) {
				for (ApplianceSet applianceSet : response.getApplianceSets()) {
					if (applianceSet.getType() == Type.portal && applianceSetCallback != null) {
						applianceSetCallback.processApplianceSet(applianceSet);
						
						return;
					}
				}
				
				NewApplianceSet applianceSet = new NewApplianceSet();
				applianceSet.setName("Portal set");
				applianceSet.setPriority("50");
				applianceSet.setType(Type.portal);
				
				NewApplianceSetRequest applianceSetRequest = new NewApplianceSetRequest();
				applianceSetRequest.setApplianceSet(applianceSet);
				applianceSetService.addApplianceSet(applianceSetRequest, new MethodCallback<ApplianceSetRequestResponse>() {
					@Override
					public void onFailure(Method method, Throwable exception) {
						Window.alert(exception.getMessage());
					}

					@Override
					public void onSuccess(Method method, ApplianceSetRequestResponse response) {
						if (applianceSetCallback != null) {
							applianceSetCallback.processApplianceSet(response.getApplianceSet());
						}
					}
				});
			}
		});
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

	public void getInstanceVms(String applianceInstanceId, final ApplianceVmsCallback applianceVmsCallback) {
		applianceVmsService.getApplianceVms(new MethodCallback<ApplianceVmsResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, ApplianceVmsResponse response) {
				if (applianceVmsCallback != null) {
					applianceVmsCallback.processApplianceVms(response.getApplianceVms());
				}
			}
		});
	}

	public void getComputeSite(String computeSiteId, final ComputeSiteCallback computeSiteCallback) {
		computeSitesService.getComputeSite(computeSiteId, new MethodCallback<ComputeSiteRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, ComputeSiteRequestResponse response) {
				if (computeSiteCallback != null) {
					computeSiteCallback.processComputeSite(response.getComputeSite());
				}
			}
		});
	}

	public void getApplianceSets(Type type, final ApplianceSetsCallback applianceSetsCallback) {
		applianceSetService.getApplianceSets(type, new MethodCallback<ApplianceSetsResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, ApplianceSetsResponse response) {
				if (applianceSetsCallback != null) {
					applianceSetsCallback.processApplianceSet(response.getApplianceSets());
				}
			}
		});
	}

	public void getApplianceInstances(String applianceSetId, final ApplianceInstancesCallback applianceInstancesCallback) {
		applianceInstancesService.getApplianceInstances(applianceSetId, new MethodCallback<ApplianceInstancesResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, ApplianceInstancesResponse response) {
				if (applianceInstancesCallback != null) {
					applianceInstancesCallback.processApplianceInstances(response.getApplianceInstances());
				}
			}
		});
	}

	public void shutdownApplianceSet(String applianceSetId, final Command command) {
		applianceSetService.deleteApplianceSet(applianceSetId, new MethodCallback<Void>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, Void response) {
				if (command != null) {
					command.execute();
				}
			}
		});
	}

	public void getPortalApplianceInstances(final ApplianceInstancesCallback applianceInstancesCallback) {
		applianceSetService.getApplianceSets(Type.portal, new MethodCallback<ApplianceSetsResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, ApplianceSetsResponse response) {
				if (response.getApplianceSets().size() > 0) {
					ApplianceSet portalApplianceSet = response.getApplianceSets().get(0);
					applianceInstancesService.getApplianceInstances(portalApplianceSet.getId(), new MethodCallback<ApplianceInstancesResponse>() {
						@Override
						public void onFailure(Method method, Throwable exception) {
							popupErrorHandler.displayError(exception.getMessage());
						}

						@Override
						public void onSuccess(Method method, ApplianceInstancesResponse response) {
							if (applianceInstancesCallback != null) {
								applianceInstancesCallback.processApplianceInstances(response.getApplianceInstances());
							}
						}
					});
				} else {
					if (applianceInstancesCallback != null) {
						applianceInstancesCallback.processApplianceInstances(new ArrayList<ApplianceInstance>());
					}
				}
			}
		});
	}

	public void getPortMappingTemplates(String applianceTypeId, final PortMappingTemplatesCallback portMappingTemplatesCallback) {
		portMappingTemplateService.getPortMappingTemplates(applianceTypeId, new MethodCallback<PortMappingTemplatesResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, PortMappingTemplatesResponse response) {
				if (portMappingTemplatesCallback != null) {
					portMappingTemplatesCallback.processPortMappingTemplates(response.getPortMappingTemplates());
				}
			}
		});
	}

	public void getHttpMappings(String applianceInstanceId, final HttpMappingsCallback httpMappingsCallback) {
		httpMappingService.getHttpMappings(applianceInstanceId, new MethodCallback<HttpMappingResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, HttpMappingResponse response) {
				if (httpMappingsCallback != null) {
					httpMappingsCallback.processHttpMappings(response.getHttpMappings());
				}
			}
		});
	}

	public void getEndpoints(String portMappingTemplateId, final EndpointsCallback endpointsCallback) {
		endpointService.getEndpoints(portMappingTemplateId, new MethodCallback<EndpointsResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, EndpointsResponse response) {
				if (endpointsCallback != null) {
					endpointsCallback.processEndpoints(response.getEndpoints());
				}
			}
		});
	}

	public void getUserKeys(final UserKeysCallback userKeysCallback) {
		userKeyService.getUserKeys(new MethodCallback<UserKeysResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, UserKeysResponse response) {
				if (userKeysCallback != null) {
					userKeysCallback.processUserKeys(response.getUserKeys());
				}
			}
		});
	}

	public void uploadUserKey(String keyName, String keyContents, final KeyUploadCallback keyUploadCallback) {
		NewUserKey userKey = new NewUserKey();
		userKey.setName(keyName);
		userKey.setPublicKey(keyContents);
		
		NewUserKeyRequest keyRequest = new NewUserKeyRequest();
		keyRequest.setUserKey(userKey);
		userKeyService.addUserKey(keyRequest, new MethodCallback<UserKeyRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert("" + exception);
				if (keyUploadCallback != null) {
					keyUploadCallback.onError(exception.getMessage());
				}
			}

			@Override
			public void onSuccess(Method method, UserKeyRequestResponse response) {
				if (keyUploadCallback != null) {
					keyUploadCallback.onSuccess(response.getUserKey());
				}
			}
		});
	}

	public void removeUserKey(String userKeyId, final Command after) {
		userKeyService.deleteUserKey(userKeyId, new MethodCallback<Void>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, Void response) {
				if (after != null) {
					after.execute();
				}
			}
		});
	}

	public void removeInitialConfiguration(String initialConfigurationId, final Command command) {
		applianceConfigurationService.deleteApplianceConfiguration(initialConfigurationId, new MethodCallback<Void>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, Void response) {
				if (command != null) {
					command.execute();
				}
			}
		});
	}

	public void updateApplianceConfiguration(String configId,	String applianceTypeId, String configName,
			String configPayload, final ApplianceConfigurationCallback applianceConfigurationCallback) {
		ApplianceConfiguration applianceConfiguration = new ApplianceConfiguration();
		applianceConfiguration.setId(configId);
		applianceConfiguration.setName(configName);
		applianceConfiguration.setPayload(configPayload);
		applianceConfiguration.setApplianceTypeId(applianceTypeId);
		
		ApplianceConfigurationRequestResponse request = new ApplianceConfigurationRequestResponse();
		request.setApplianceConfiguration(applianceConfiguration);
		applianceConfigurationService.updateApplianceConfiguration(configId, request, new MethodCallback<ApplianceConfigurationRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, ApplianceConfigurationRequestResponse response) {
				if (applianceConfigurationCallback != null) {
					applianceConfigurationCallback.processApplianceConfiguration(response.getApplianceConfiguration());
				}
			}
		});
	}

	public void getInitialConfigurations(List<String> initialConfigurationIds, final ApplianceConfigurationsCallback applianceConfigurationsCallback) {
		String ids = join(initialConfigurationIds, ",");
		applianceConfigurationService.getApplianceConfigurationsForIds(ids, new MethodCallback<ApplianceConfigurationsResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, ApplianceConfigurationsResponse response) {
				if (applianceConfigurationsCallback != null) {
					applianceConfigurationsCallback.processApplianceConfigurations(response.getApplianceConfigurations());
				}
			}
		});
	}
	
	public void getApplianceTypes(List<String> applianceTypeIds, final ApplianceTypesCallback applianceTypesCallback) {
		applianceTypesService.getApplianceTypesForIds(join(applianceTypeIds, ","), new MethodCallback<ApplianceTypesResponse>() {
			@Override
			public void onSuccess(Method method, ApplianceTypesResponse response) {
				if (applianceTypesCallback != null) {
					applianceTypesCallback.processApplianceTypes(response.getApplianceTypes());
				}
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(exception.getMessage());
			}
		});
	}

	private String join(List<String> initialConfigurationIds, String delimiter) {
		StringBuilder builder = new StringBuilder();
		
		for (Iterator<String> i = initialConfigurationIds.iterator(); i.hasNext(); ) {
			builder.append(i.next().trim());
			
			if (i.hasNext()) {
				builder.append(delimiter);
			}
		}
		
		return builder.toString();
	}

	public void startApplianceTypes(final Map<String, Map<String, String>> parameterValues, final Command command) {
		ensurePortalApplianceSet(new ApplianceSetCallback() {
			@Override
			public void processApplianceSet(ApplianceSet applianceSet) {
				final List<String> started = new ArrayList<String>();
				final List<String> failed = new ArrayList<String>();
				
				for (final String configurationTemplateId : parameterValues.keySet()) {
					NewApplianceInstance applianceInstance = new NewApplianceInstance();
					applianceInstance.setApplianceSetId(applianceSet.getId());
					applianceInstance.setConfigurationTemplateId(configurationTemplateId);
					applianceInstance.setParams(parameterValues.get(configurationTemplateId));
					
					NewApplianceInstanceRequest applianceInstanceRequest = new NewApplianceInstanceRequest();
					applianceInstanceRequest.setApplianceInstance(applianceInstance);
					applianceInstancesService.addApplianceInstance(applianceInstanceRequest, new MethodCallback<ApplianceInstanceRequestResponse>() {
						@Override
						public void onFailure(Method method, Throwable exception) {
							failed.add(configurationTemplateId);
							popupErrorHandler.displayError(exception.getMessage());
							checkReturn();
						}

						@Override
						public void onSuccess(Method method, ApplianceInstanceRequestResponse response) {
							started.add(configurationTemplateId);
							checkReturn();
						}
						
						private void checkReturn() {
							if (started.size() + failed.size() == parameterValues.keySet().size() && command != null) {
								command.execute();
							}
						}
					});
				}
			}
		});
	}
}