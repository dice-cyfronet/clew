package pl.cyfronet.coin.clew.client.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.fusesource.restygwt.client.FailedStatusCodeException;
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
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.SaveApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.SaveApplianceTypeRequest;
import pl.cyfronet.coin.clew.client.controller.cf.appliancevm.ApplianceVm;
import pl.cyfronet.coin.clew.client.controller.cf.appliancevm.ApplianceVmService;
import pl.cyfronet.coin.clew.client.controller.cf.appliancevm.ApplianceVmsResponse;
import pl.cyfronet.coin.clew.client.controller.cf.computesite.ComputeSite;
import pl.cyfronet.coin.clew.client.controller.cf.computesite.ComputeSiteRequestResponse;
import pl.cyfronet.coin.clew.client.controller.cf.computesite.ComputeSiteService;
import pl.cyfronet.coin.clew.client.controller.cf.devmodepropertyset.DevelopmentModePropertySet;
import pl.cyfronet.coin.clew.client.controller.cf.devmodepropertyset.DevelopmentModePropertySetService;
import pl.cyfronet.coin.clew.client.controller.cf.devmodepropertyset.DevelopmentModePropertySetsResponse;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.Endpoint;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.EndpointRequestResponse;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.EndpointService;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.EndpointsResponse;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.NewEndpoint;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.NewEndpointRequest;
import pl.cyfronet.coin.clew.client.controller.cf.httpmapping.HttpMapping;
import pl.cyfronet.coin.clew.client.controller.cf.httpmapping.HttpMappingResponse;
import pl.cyfronet.coin.clew.client.controller.cf.httpmapping.HttpMappingService;
import pl.cyfronet.coin.clew.client.controller.cf.portmapping.PortMapping;
import pl.cyfronet.coin.clew.client.controller.cf.portmapping.PortMappingResponse;
import pl.cyfronet.coin.clew.client.controller.cf.portmapping.PortMappingService;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate.NewPortMappingTemplate;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate.NewPortMappingTemplateRequest;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate.PortMappingTemplate;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate.PortMappingTemplateRequestResponse;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate.PortMappingTemplateService;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate.PortMappingTemplatesResponse;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplateproperty.NewPortMappingTemplateProperty;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplateproperty.NewPortMappingTemplatePropertyRequest;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplateproperty.PortMappingTemplatePropertiesResponse;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplateproperty.PortMappingTemplateProperty;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplateproperty.PortMappingTemplatePropertyRequestResponse;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplateproperty.PortMappingTemplatePropertyService;
import pl.cyfronet.coin.clew.client.controller.cf.user.User;
import pl.cyfronet.coin.clew.client.controller.cf.user.UserRequestResponse;
import pl.cyfronet.coin.clew.client.controller.cf.user.UserService;
import pl.cyfronet.coin.clew.client.controller.cf.user.UsersResponse;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.NewUserKey;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.NewUserKeyRequest;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.UserKey;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.UserKeyRequestResponse;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.UserKeyService;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.UserKeysResponse;
import pl.cyfronet.coin.clew.client.controller.overlay.MutableBoolean;
import pl.cyfronet.coin.clew.client.controller.overlay.OwnedApplianceType;
import pl.cyfronet.coin.clew.client.controller.overlay.Redirection;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CloudFacadeController {
	public abstract static class ApplianceInstancesCallback {
		public abstract void processApplianceInstances(List<ApplianceInstance> applianceInstances);
		
		protected void onError(Throwable e) {
			simpleErrorHandler.displayError(e.getMessage());
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
	
	public interface PortMappingTemplateCallback {
		void processPortMappingTemplate(PortMappingTemplate portMappingTemplate);
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
		void onError(CloudFacadeErrorCodes errorCodes);
	}
	
	public interface DevelopmentModePropertySetCallback {
		void processDeveopmentModePropertySet(DevelopmentModePropertySet developmentModePropertySet);
	}
	
	public interface PortMappingsCallback {
		void processPortMappings(List<PortMapping> portMappings);
	}
	
	public interface EndpointCallback {
		void processEndpoint(Endpoint endpoint);
	}
	
	public interface ErrorCallback {
		void onError(CloudFacadeErrorCodes errorCodes);
	}
	
	public interface RedirectionsCallback {
		void processRedirections(List<Redirection> redirections);
	}
	
	public interface UsersCallback {
		void processUsers(List<User> users);
	}
	
	public interface UserCallback {
		void processUser(User user);
	}
	
	public interface OwnedApplianceTypesCallback {
		void processOwnedApplianceTypes(List<OwnedApplianceType> ownedApplianceTypes);
	}
	
	public interface PortMappingTemplatePropertiesCallback {
		void processPortMappingTemplateProperties(List<PortMappingTemplateProperty> portMappingTemplateProperties);
	}
	
	private ApplianceTypeService applianceTypesService;
	private ApplianceInstanceService applianceInstancesService;
	private ApplianceSetService applianceSetService;
	private ApplianceConfigurationService applianceConfigurationService;
	private static SimpleErrorHandler simpleErrorHandler;
	private ApplianceVmService applianceVmsService;
	private ComputeSiteService computeSitesService;
	private PortMappingTemplateService portMappingTemplateService;
	private HttpMappingService httpMappingService;
	private EndpointService endpointService;
	private UserKeyService userKeyService;
	private DevelopmentModePropertySetService developmentModePropertySetService;
	private PortMappingService portMappingService;
	private UserService userService;
	private PortMappingTemplatePropertyService portMappingTemplatePropertyService;
	
	@Inject
	public CloudFacadeController(ApplianceTypeService applianceTypesService, ApplianceInstanceService applianceInstancesService,
			ApplianceSetService applianceSetService, ApplianceConfigurationService applianceConfigurationService,
			ApplianceVmService applianceVmsService, ComputeSiteService computeSitesService,
			PortMappingTemplateService portMappingTemplateService, HttpMappingService httpMappingService,
			EndpointService endpointService, UserKeyService userKeyService,
			DevelopmentModePropertySetService developmentModePropertySetService,
			PortMappingService portMappingService, UserService userService,
			PortMappingTemplatePropertyService portMappingTemplatePropertyService, SimpleErrorHandler simpleErrorHandler) {
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
		this.developmentModePropertySetService = developmentModePropertySetService;
		this.portMappingService = portMappingService;
		this.userService = userService;
		this.portMappingTemplatePropertyService = portMappingTemplatePropertyService;
		CloudFacadeController.simpleErrorHandler = simpleErrorHandler;
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
				simpleErrorHandler.displayError(exception.getMessage());
			}
		});
	}
	
	public void getApplianceType(String applianceTypeId, final ApplianceTypeCallback applianceTypeCallback) {
		applianceTypesService.getApplianceType(applianceTypeId, new MethodCallback<ApplianceTypeRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
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
		ensureApplianceSet(Type.portal, new ApplianceSetCallback() {
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
							simpleErrorHandler.displayError(exception.getMessage());
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
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, Void response) {
				if (afterShutdown != null) {
					afterShutdown.execute();
				}
			}
		});
	}
	
	private void ensureApplianceSet(final Type type, final ApplianceSetCallback applianceSetCallback) {
		applianceSetService.getApplianceSets(new MethodCallback<ApplianceSetsResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, ApplianceSetsResponse response) {
				for (ApplianceSet applianceSet : response.getApplianceSets()) {
					if (applianceSet.getType() == type && applianceSetCallback != null) {
						applianceSetCallback.processApplianceSet(applianceSet);
						
						return;
					}
				}
				
				NewApplianceSet applianceSet = new NewApplianceSet();
				applianceSet.setName("Portal set");
				applianceSet.setPriority("50");
				applianceSet.setType(type);
				
				NewApplianceSetRequest applianceSetRequest = new NewApplianceSetRequest();
				applianceSetRequest.setApplianceSet(applianceSet);
				applianceSetService.addApplianceSet(applianceSetRequest, new MethodCallback<ApplianceSetRequestResponse>() {
					@Override
					public void onFailure(Method method, Throwable exception) {
						simpleErrorHandler.displayError(exception.getMessage());
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
		applianceVmsService.getApplianceVms(applianceInstanceId, new MethodCallback<ApplianceVmsResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
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
				simpleErrorHandler.displayError(exception.getMessage());
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
				simpleErrorHandler.displayError(exception.getMessage());
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
				simpleErrorHandler.displayError(exception.getMessage());
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
				simpleErrorHandler.displayError(exception.getMessage());
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
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, ApplianceSetsResponse response) {
				if (response.getApplianceSets().size() > 0) {
					ApplianceSet portalApplianceSet = response.getApplianceSets().get(0);
					applianceInstancesService.getApplianceInstances(portalApplianceSet.getId(), new MethodCallback<ApplianceInstancesResponse>() {
						@Override
						public void onFailure(Method method, Throwable exception) {
							simpleErrorHandler.displayError(exception.getMessage());
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
				simpleErrorHandler.displayError(exception.getMessage());
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
				simpleErrorHandler.displayError(exception.getMessage());
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
				simpleErrorHandler.displayError(exception.getMessage());
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
				simpleErrorHandler.displayError(exception.getMessage());
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
				//TODO(DH): replace with a generic error handler in future
				if (exception instanceof FailedStatusCodeException) {
					if (method.getResponse().getText() != null && method.getResponse().getText().contains("public_key")) {
						if (keyUploadCallback != null) {
							keyUploadCallback.onError(CloudFacadeErrorCodes.UserKeyInvalid);
							
							return;
						}
					}
				}
				
				if (keyUploadCallback != null) {
					keyUploadCallback.onError(CloudFacadeErrorCodes.UnknownError);
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
				simpleErrorHandler.displayError(exception.getMessage());
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
				simpleErrorHandler.displayError(exception.getMessage());
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
				simpleErrorHandler.displayError(exception.getMessage());
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
				simpleErrorHandler.displayError(exception.getMessage());
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
				simpleErrorHandler.displayError(exception.getMessage());
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
		ensureApplianceSet(Type.portal, new ApplianceSetCallback() {
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
							simpleErrorHandler.displayError(exception.getMessage());
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

	public void getDevelopmentApplianceTypes(final ApplianceTypesCallback applianceTypesCallback) {
		applianceTypesService.getApplianceTypesForVisibilityAndActiveFlag(join(Arrays.asList(new String[] {"developer", "all", "owner"}), ","), true, new MethodCallback<ApplianceTypesResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, ApplianceTypesResponse response) {
				if (applianceTypesCallback != null) {
					applianceTypesCallback.processApplianceTypes(response.getApplianceTypes());
				}
			}
		});
	}

	public void getProductionApplianceTypes(final ApplianceTypesCallback applianceTypesCallback) {
		applianceTypesService.getApplianceTypesForVisibilityAndActiveFlag(join(Arrays.asList(new String[] {"all", "owner"}), ","), true, new MethodCallback<ApplianceTypesResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, ApplianceTypesResponse response) {
				if (applianceTypesCallback != null) {
					applianceTypesCallback.processApplianceTypes(response.getApplianceTypes());
				}
			}
		});
	}

	public void startApplianceTypesInDevelopment(final List<String> configurationTemplateIds, final Command command) {
		ensureApplianceSet(Type.development, new ApplianceSetCallback() {
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
							simpleErrorHandler.displayError(exception.getMessage());
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

	public void startApplianceTypesInDevelopment(final Map<String, String> overrideNames, final String keyId, final Map<String, Map<String, String>> parameterValues, final Command command) {
		ensureApplianceSet(Type.development, new ApplianceSetCallback() {
			@Override
			public void processApplianceSet(ApplianceSet applianceSet) {
				final List<String> started = new ArrayList<String>();
				final List<String> failed = new ArrayList<String>();
				
				for (final String configurationTemplateId : parameterValues.keySet()) {
					NewApplianceInstance applianceInstance = new NewApplianceInstance();
					applianceInstance.setApplianceSetId(applianceSet.getId());
					applianceInstance.setConfigurationTemplateId(configurationTemplateId);
					applianceInstance.setParams(parameterValues.get(configurationTemplateId));
					applianceInstance.setUserKeyId(keyId);
					
					if (overrideNames != null && overrideNames.containsKey(configurationTemplateId)) {
						applianceInstance.setName(overrideNames.get(configurationTemplateId));
					}
					
					NewApplianceInstanceRequest applianceInstanceRequest = new NewApplianceInstanceRequest();
					applianceInstanceRequest.setApplianceInstance(applianceInstance);
					applianceInstancesService.addApplianceInstance(applianceInstanceRequest, new MethodCallback<ApplianceInstanceRequestResponse>() {
						@Override
						public void onFailure(Method method, Throwable exception) {
							failed.add(configurationTemplateId);
							simpleErrorHandler.displayError(exception.getMessage());
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

	public void getDevelopmentApplianceInstances(final ApplianceInstancesCallback applianceInstancesCallback) {
		ensureApplianceSet(Type.development, new ApplianceSetCallback() {
			@Override
			public void processApplianceSet(ApplianceSet applianceSet) {
				applianceInstancesService.getApplianceInstances(applianceSet.getId(), new MethodCallback<ApplianceInstancesResponse>() {
					@Override
					public void onFailure(Method method, Throwable exception) {
						simpleErrorHandler.displayError(exception.getMessage());
					}

					@Override
					public void onSuccess(Method method, ApplianceInstancesResponse response) {
						if (applianceInstancesCallback != null) {
							applianceInstancesCallback.processApplianceInstances(response.getApplianceInstances());
						}
					}
				});
			}
		});
	}

	public void getDevelopmentModePropertySet(String applianceInstanceId, final DevelopmentModePropertySetCallback developmentModePropertySetCallback) {
		developmentModePropertySetService.getDevelopmentModePropertySet(applianceInstanceId, new MethodCallback<DevelopmentModePropertySetsResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, DevelopmentModePropertySetsResponse response) {
				if (developmentModePropertySetCallback != null) {
					if (response.getDevelopmentModePropertySets() != null && response.getDevelopmentModePropertySets().size() > 0) {
						developmentModePropertySetCallback.processDeveopmentModePropertySet(response.getDevelopmentModePropertySets().get(0));
					} else {
						developmentModePropertySetCallback.processDeveopmentModePropertySet(null);
					}
				}
			}});
	}

	public void getPortMappingTemplates(List<String> portMappingTemplateIds, final PortMappingTemplatesCallback portMappingTemplatesCallback) {
		portMappingTemplateService.getPortMappingTemplatesForIds(join(portMappingTemplateIds, ","), new MethodCallback<PortMappingTemplatesResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, PortMappingTemplatesResponse response) {
				if (portMappingTemplatesCallback != null) {
					portMappingTemplatesCallback.processPortMappingTemplates(response.getPortMappingTemplates());
				}
			}
		});
	}

	public void getPortMappings(String applianceInstanceId, final PortMappingsCallback portMappingsCallback) {
		portMappingService.getPortMappings(applianceInstanceId, new MethodCallback<PortMappingResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, PortMappingResponse response) {
				if (portMappingsCallback != null) {
					portMappingsCallback.processPortMappings(response.getPortMappings());
				}
			}
		});
	}

	public void removePortMapingTemplate(String mappingId, final Command command) {
		portMappingTemplateService.removePortMappingTemplate(mappingId, new MethodCallback<Void>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, Void response) {
				if (command != null) {
					command.execute();
				}
			}
		});
	}

	public void addPortMappingTemplateForDevelopmentModePropertySet(String name, int portNumber, String transportProtocol,
			final String applicationProtocol, String developmentModePropertySetId, final PortMappingTemplateCallback portMappingTemplateCallback) {
		NewPortMappingTemplateRequest request = createPortMappingTemplateRequest(name, portNumber, transportProtocol, applicationProtocol, developmentModePropertySetId, null);
		portMappingTemplateService.addPortMappingTemplate(request, new MethodCallback<PortMappingTemplateRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, PortMappingTemplateRequestResponse response) {
				//this operation requires a busy-waiting loop
				if (applicationProtocol.equals("none")) {
					//waiting for port mappings
					waitForPortMappings(response.getPortMappingTemplate(), portMappingTemplateCallback);
				} else {
					//waiting for http mappings
					waitForHttpMappings(response.getPortMappingTemplate(), portMappingTemplateCallback);
				}
			}
		});
	}
	
	public void addPortMappingTemplateForApplianceType(String name, int portNumber, String transportProtocol,
			final String applicationProtocol, String applianceTypeId, final PortMappingTemplateCallback portMappingTemplateCallback) {
		NewPortMappingTemplateRequest request = createPortMappingTemplateRequest(name, portNumber, transportProtocol, applicationProtocol, null, applianceTypeId);
		portMappingTemplateService.addPortMappingTemplate(request, new MethodCallback<PortMappingTemplateRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, PortMappingTemplateRequestResponse response) {
				if (portMappingTemplateCallback != null) {
					portMappingTemplateCallback.processPortMappingTemplate(response.getPortMappingTemplate());
				}
			}
		});
	}
	
	private NewPortMappingTemplateRequest createPortMappingTemplateRequest(String name, int portNumber, String transportProtocol, final String applicationProtocol,
			String developmentModePropertySetId, String applianceTypeId) {
		NewPortMappingTemplate portMappingTemplate = new NewPortMappingTemplate();
		portMappingTemplate.setServiceName(name);
		portMappingTemplate.setTargetPort(portNumber);
		portMappingTemplate.setTransportProtocol(transportProtocol);
		portMappingTemplate.setApplicationProtocol(applicationProtocol);
		portMappingTemplate.setDevelopmentModePropertySetId(developmentModePropertySetId);
		portMappingTemplate.setApplianceTypeId(applianceTypeId);
		
		NewPortMappingTemplateRequest request = new NewPortMappingTemplateRequest();
		request.setPortMapping(portMappingTemplate);
		
		return request;
	}

	private void waitForHttpMappings(final PortMappingTemplate portMappingTemplate, final PortMappingTemplateCallback portMappingTemplateCallback) {
		httpMappingService.getHttpMappingsForPortMappingTemplateId(portMappingTemplate.getId(), new MethodCallback<HttpMappingResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, HttpMappingResponse response) {
				if (response.getHttpMappings().size() == 0) {
					new Timer() {
						@Override
						public void run() {
							waitForHttpMappings(portMappingTemplate, portMappingTemplateCallback);
						}
					}.schedule(1000);
				} else {
					if (portMappingTemplateCallback != null) {
						portMappingTemplateCallback.processPortMappingTemplate(portMappingTemplate);
					}
				}
			}
		});
	}

	private void waitForPortMappings(final PortMappingTemplate portMappingTemplate, final PortMappingTemplateCallback portMappingTemplateCallback) {
		portMappingService.getPortMappingsForPortMappingTemplateId(portMappingTemplate.getId(), new MethodCallback<PortMappingResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, PortMappingResponse response) {
				if (response.getPortMappings().size() == 0) {
					new Timer() {
						@Override
						public void run() {
							waitForPortMappings(portMappingTemplate, portMappingTemplateCallback);
						}
					}.schedule(1000);
				} else {
					if (portMappingTemplateCallback != null) {
						portMappingTemplateCallback.processPortMappingTemplate(portMappingTemplate);
					}
				}
			}
		});
	}

	public void getPortMappingTemplatesForDevelopmentModePropertySetId(String developmentModePropertySetId, final PortMappingTemplatesCallback portMappingTemplatesCallback) {
		portMappingTemplateService.getPortMappingTemplatesForDevelopmentModePropertySetId(developmentModePropertySetId, new MethodCallback<PortMappingTemplatesResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, PortMappingTemplatesResponse response) {
				if (portMappingTemplatesCallback != null) {
					portMappingTemplatesCallback.processPortMappingTemplates(response.getPortMappingTemplates());
				}
			}
		});
	}

	public void getPortMappingsForPortMappingTemplateId(String portMappingTemplateId, final PortMappingsCallback portMappingsCallback) {
		portMappingService.getPortMappingsForPortMappingTemplateId(portMappingTemplateId, new MethodCallback<PortMappingResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, PortMappingResponse response) {
				if (portMappingsCallback != null) {
					portMappingsCallback.processPortMappings(response.getPortMappings());
				}
			}
		});
	}

	public void getHttpMappingsForPortMappingTemplateId(String portMappingTemplateId, final HttpMappingsCallback httpMappingsCallback) {
		httpMappingService.getHttpMappingsForPortMappingTemplateId(portMappingTemplateId, new MethodCallback<HttpMappingResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());	
			}

			@Override
			public void onSuccess(Method method, HttpMappingResponse response) {
				if (httpMappingsCallback != null) {
					httpMappingsCallback.processHttpMappings(response.getHttpMappings());
				}
			}
		});
	}

	public void removeEndpoint(String endpointId, final Command command) {
		endpointService.deleteEndpoint(endpointId, new MethodCallback<Void>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, Void response) {
				if (command != null) {
					command.execute();
				}
			}
		});
	}

	public void addEndpoint(String name, String invocationPath, String endpointType, String portMappingTemplateId,
			String description, String descriptor, final EndpointCallback endpointCallback) {
		NewEndpoint endpoint = new NewEndpoint();
		endpoint.setName(name);
		endpoint.setDescription(description);
		endpoint.setDescriptor(descriptor);
		endpoint.setEndpointType(endpointType);
		endpoint.setInvocationPath(invocationPath);
		endpoint.setPortMappingTemplateId(portMappingTemplateId);
		
		NewEndpointRequest request = new NewEndpointRequest();
		request.setEndpoint(endpoint);
		endpointService.addEndpoint(request, new MethodCallback<EndpointRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, EndpointRequestResponse response) {
				if (endpointCallback != null) {
					endpointCallback.processEndpoint(response.getEndpoint());
				}
			}
		});
	}

	public void updateApplianceType(String applianceTypeId, String name, String description,
			boolean shared, boolean scalable, String visibleFor, String cores,
			String ram, String disk, final ApplianceTypeCallback applianceTypeCallback, final ErrorCallback errorCallback) {
		ApplianceType applianceType = new ApplianceType();
		applianceType.setId(applianceTypeId);
		applianceType.setName(name);
		applianceType.setDescription(description);
		applianceType.setShared(shared);
		applianceType.setScalable(scalable);
		applianceType.setVisibleTo(visibleFor);
		applianceType.setPreferenceCpu(cores);
		applianceType.setPreferenceMemory(ram);
		applianceType.setPreferenceDisk(disk);
		
		ApplianceTypeRequestResponse request = new ApplianceTypeRequestResponse();
		request.setApplianceType(applianceType);
		applianceTypesService.updateApplianceType(applianceTypeId, request, new MethodCallback<ApplianceTypeRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				if (errorCallback != null) {
					errorCallback.onError(CloudFacadeErrorCodes.ApplianceTypeUpdateError);
				}
			}

			@Override
			public void onSuccess(Method method, ApplianceTypeRequestResponse response) {
				if (applianceTypeCallback != null) {
					applianceTypeCallback.processApplianceType(response.getApplianceType());
				}
			}
		});
	}

	public void saveApplianceType(String applianceId, String name,
			String description, boolean shared, boolean scalable,
			String visibleFor, String cores, String ram, String disk,
			final ApplianceTypeCallback applianceTypeCallback,
			final ErrorCallback errorCallback) {
		SaveApplianceType applianceType = new SaveApplianceType();
		applianceType.setApplianceId(applianceId);
		applianceType.setName(name);
		applianceType.setDescription(description);
		applianceType.setShared(shared);
		applianceType.setScalable(scalable);
		applianceType.setVisibleTo(visibleFor);
		applianceType.setPreferenceCpu(cores);
		applianceType.setPreferenceMemory(ram);
		applianceType.setPreferenceDisk(disk);
		
		SaveApplianceTypeRequest request = new SaveApplianceTypeRequest();
		request.setApplianceType(applianceType);
		
		applianceTypesService.addApplianceType(request, new MethodCallback<ApplianceTypeRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				if (errorCallback != null) {
					errorCallback.onError(CloudFacadeErrorCodes.ApplianceTypeSaveError);
				}
			}

			@Override
			public void onSuccess(Method method, ApplianceTypeRequestResponse response) {
				if (applianceTypeCallback != null) {
					applianceTypeCallback.processApplianceType(response.getApplianceType());
				}
			}
		});
	}
	
	public void getRedirectionsForApplianceType(String applianceTypeId, final RedirectionsCallback callback) {
		getPortMappingTemplates(applianceTypeId, new PortMappingTemplatesCallback() {
			@Override
			public void processPortMappingTemplates(List<PortMappingTemplate> portMappingTemplates) {
				buildRedirections(portMappingTemplates, callback);
			}
		});
	}
	
	public void getRedirectionsForDevPropertySetId(String devPropertySetId, final RedirectionsCallback callback) {
		getPortMappingTemplatesForDevelopmentModePropertySetId(devPropertySetId, new PortMappingTemplatesCallback() {
			@Override
			public void processPortMappingTemplates(List<PortMappingTemplate> portMappingTemplates) {
				buildRedirections(portMappingTemplates, callback);
			}
		});
	}

	protected void buildRedirections(List<PortMappingTemplate> portMappingTemplates, final RedirectionsCallback callback) {
		final List<Redirection> redirections = new ArrayList<Redirection>();
		
		if (portMappingTemplates.size() == 0 && callback != null) {
			callback.processRedirections(redirections);
		}
		
		final List<MutableBoolean> finishedCallbacks = new ArrayList<MutableBoolean>();
		
		for (PortMappingTemplate portMappingTemplate : portMappingTemplates) {
			final Redirection redirection = new Redirection();
			redirection.setId(portMappingTemplate.getId());
			redirection.setName(portMappingTemplate.getServiceName());
			redirection.setTargetPort(portMappingTemplate.getTargetPort());
			redirection.setProtocol(portMappingTemplate.getTransportProtocol());
			redirections.add(redirection);
			
			if (Arrays.asList((new String[] {"http", "https", "http_https"})).contains(portMappingTemplate.getApplicationProtocol())) {
				redirection.setHttp(true);
				//fetching http mappings ...
				final MutableBoolean httpMappingsFinished = new MutableBoolean();
				finishedCallbacks.add(httpMappingsFinished);
				getHttpMappingsForPortMappingTemplateId(portMappingTemplate.getId(), new HttpMappingsCallback() {
					@Override
					public void processHttpMappings(List<HttpMapping> httpMappings) {
						for (HttpMapping httpMapping : httpMappings) {
							if ("http".equals(httpMapping.getApplicationProtocol())) {
								redirection.setHttpUrl(httpMapping.getUrl());
							} else if ("https".equals(httpMapping.getApplicationProtocol())) {
								redirection.setHttpsUrl(httpMapping.getUrl());
							}
						}
						
						httpMappingsFinished.setValue(true);
						checkRedirectionsReturn(finishedCallbacks, redirections, callback);
					}
				});
				
				//...  and endpoints
				final MutableBoolean endpointsFinished = new MutableBoolean();
				finishedCallbacks.add(endpointsFinished);
				getEndpoints(portMappingTemplate.getId(), new EndpointsCallback() {
					@Override
					public void processEndpoints(List<Endpoint> endpoints) {
						redirection.setEndpoints(endpoints);
						endpointsFinished.setValue(true);
						checkRedirectionsReturn(finishedCallbacks, redirections, callback);
					}
				});
			} else {
				redirection.setHttp(false);
				//fetching port mappings
				final MutableBoolean portMappingsFinished = new MutableBoolean();
				finishedCallbacks.add(portMappingsFinished);
				getPortMappingsForPortMappingTemplateId(portMappingTemplate.getId(), new PortMappingsCallback() {
					@Override
					public void processPortMappings(List<PortMapping> portMappings) {
						redirection.setPortMappings(portMappings);
						portMappingsFinished.setValue(true);
						checkRedirectionsReturn(finishedCallbacks, redirections, callback);
					}
				});
			}
		}
	}

	private void checkRedirectionsReturn(List<MutableBoolean> finishedCallbacks, List<Redirection> redirections, RedirectionsCallback callback) {
		for (MutableBoolean mutableBoolean : finishedCallbacks) {
			if (!mutableBoolean.getValue()) {
				return;
			}
		}
		
		Collections.sort(redirections, new Comparator<Redirection>() {
			@Override
			public int compare(Redirection o1, Redirection o2) {
				if (o1 != null && o2 != null && o1.getName() != null && o2.getName() != null) {
					return o1.getName().compareToIgnoreCase(o2.getName());
				}
				
				return 0;
			}
		});
		
		if (callback != null) {
			callback.processRedirections(redirections);
		}
	}
	
	public void getUsers(List<String> userIds, final UsersCallback usersCallback) {
		userService.getUsers(join(userIds, ","), new MethodCallback<UsersResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, UsersResponse response) {
				if (usersCallback != null) {
					usersCallback.processUsers(response.getUsers());
				}
			}
		});
	}
	
	public void getOwnedApplianceTypes(final OwnedApplianceTypesCallback callback) {
		getApplianceTypes(new ApplianceTypesCallback() {
			@Override
			public void processApplianceTypes(final List<ApplianceType> applianceTypes) {
				List<String> userIds = collectUserIds(applianceTypes);
				getUsers(userIds, new UsersCallback() {
					@Override
					public void processUsers(List<User> users) {
						List<OwnedApplianceType> result = new ArrayList<OwnedApplianceType>();
						
						for (ApplianceType applianceType : applianceTypes) {
							OwnedApplianceType ownedApplianceType = new OwnedApplianceType();
							ownedApplianceType.setApplianceType(applianceType);
							ownedApplianceType.setUser(getUser(applianceType.getAuthorId(), users));
							result.add(ownedApplianceType);
						}
						
						if (callback != null) {
							callback.processOwnedApplianceTypes(result);
						}
					}

					private User getUser(String authorId, List<User> users) {
						for (User user : users) {
							if (user.getId().equals(authorId)) {
								return user;
							}
						}
						
						return null;
					}
				});
			}

			private List<String> collectUserIds(List<ApplianceType> applianceTypes) {
				List<String> userIds = new ArrayList<String>();
				
				for (ApplianceType applianceType : applianceTypes) {
					userIds.add(applianceType.getAuthorId());
				}
				
				return userIds;
			}
		});
	}

	public void getUser(String userId, final UserCallback userCallback) {
		userService.getUser(userId, new MethodCallback<UserRequestResponse>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, UserRequestResponse response) {
				if (userCallback != null) {
					userCallback.processUser(response.getUser());
				}
			}
		});
	}

	public void getOwnedApplianceTypesForUser(String userLogin, final OwnedApplianceTypesCallback ownedApplianceTypesCallback) {
		userService.getUserForLogin(userLogin, new MethodCallback<UsersResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, UsersResponse response) {
				final List<OwnedApplianceType> result = new ArrayList<OwnedApplianceType>();
				
				if (response.getUsers().size() > 0) {
					final User user = response.getUsers().get(0);
					applianceTypesService.getApplianceTypesForUserId(user.getId(), new MethodCallback<ApplianceTypesResponse>() {
						@Override
						public void onFailure(Method method, Throwable exception) {
							simpleErrorHandler.displayError(exception.getMessage());
						}

						@Override
						public void onSuccess(Method method, ApplianceTypesResponse response) {
							for (ApplianceType applianceType : response.getApplianceTypes()) {
								if (applianceType.getAuthorId().equals(user.getId())) {
									OwnedApplianceType ownedApplianceType = new OwnedApplianceType();
									ownedApplianceType.setApplianceType(applianceType);
									ownedApplianceType.setUser(user);
									result.add(ownedApplianceType);
								}
							}
							
							if (ownedApplianceTypesCallback != null) {
								ownedApplianceTypesCallback.processOwnedApplianceTypes(result);
							}
						}
					});
				} else {
					if (ownedApplianceTypesCallback != null) {
						ownedApplianceTypesCallback.processOwnedApplianceTypes(result);
					}
				}
			}
		});
	}

	public void removeApplianceType(String applianceTypeId, final Command command) {
		applianceTypesService.deleteApplianceType(applianceTypeId, new MethodCallback<Void>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, Void response) {
				if (command != null) {
					command.execute();
				}
			}
		});
	}

	public void addPortMappingProperty(String portMappingTemplateId, String propertyName, String propertyValue, final Command onFinish) {
		NewPortMappingTemplateProperty property = new NewPortMappingTemplateProperty();
		property.setPortMappingTemplateId(portMappingTemplateId);
		property.setKey(propertyName);
		property.setValue(propertyValue);
		
		NewPortMappingTemplatePropertyRequest portMappingTemplatePropertyRequest = new NewPortMappingTemplatePropertyRequest();
		portMappingTemplatePropertyRequest.setPortMappingTemplateProperty(property);
		portMappingTemplatePropertyService.addPortMappingTemplateProperty(portMappingTemplatePropertyRequest, new MethodCallback<PortMappingTemplatePropertyRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, PortMappingTemplatePropertyRequestResponse response) {
				if (onFinish != null) {
					onFinish.execute();
				}
			}
		});
	}

	public void getPortMappingTemplateProperties(String portMappingTemplatePropertyId, final PortMappingTemplatePropertiesCallback portMappingTemplatePropertiesCallback) {
		portMappingTemplatePropertyService.getPortMappings(portMappingTemplatePropertyId, new MethodCallback<PortMappingTemplatePropertiesResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				simpleErrorHandler.displayError(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, PortMappingTemplatePropertiesResponse response) {
				if (portMappingTemplatePropertiesCallback != null) {
					portMappingTemplatePropertiesCallback.processPortMappingTemplateProperties(response.getProperties());
				}
			}
		});
	}
}