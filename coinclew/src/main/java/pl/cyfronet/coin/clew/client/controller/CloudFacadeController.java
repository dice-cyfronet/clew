package pl.cyfronet.coin.clew.client.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import pl.cyfronet.coin.clew.client.controller.cf.CfErrorReader;
import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeError;
import pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliance.AggregateAppliance;
import pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliance.AggregateApplianceService;
import pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliance.AggregateAppliancesResponse;
import pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliancetype.AggregateApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliancetype.AggregateApplianceTypeService;
import pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliancetype.AggregateApplianceTypesResponse;
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
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.RebootRequest;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.ApplianceSet;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.ApplianceSetRequestResponse;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.ApplianceSetService;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.ApplianceSetsResponse;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.NewApplianceSet;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.NewApplianceSet.Type;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.NewApplianceSetRequest;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypeRequestResponse;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypeSaveInPlace;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypeSaveInPlaceRequest;
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
import pl.cyfronet.coin.clew.client.controller.cf.computesite.ComputeSitesResponse;
import pl.cyfronet.coin.clew.client.controller.cf.devmodepropertyset.DevelopmentModePropertySet;
import pl.cyfronet.coin.clew.client.controller.cf.devmodepropertyset.DevelopmentModePropertySetService;
import pl.cyfronet.coin.clew.client.controller.cf.devmodepropertyset.DevelopmentModePropertySetsResponse;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.Endpoint;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.EndpointRequestResponse;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.EndpointService;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.EndpointsResponse;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.NewEndpoint;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.NewEndpointRequest;
import pl.cyfronet.coin.clew.client.controller.cf.flavor.Flavor;
import pl.cyfronet.coin.clew.client.controller.cf.flavor.FlavorService;
import pl.cyfronet.coin.clew.client.controller.cf.flavor.FlavorsResponse;
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
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CloudFacadeController {
	public interface ErrorCallback {
		void onError(CloudFacadeError error);
	}
	
	public interface ApplianceInstancesCallback extends ErrorCallback {
		public abstract void processApplianceInstances(List<ApplianceInstance> applianceInstances);
	}
	
	public interface ApplianceTypesCallback {
		void processApplianceTypes(List<ApplianceType> applianceTypes);
	}
	
	public interface ApplianceTypeCallback extends ErrorCallback {
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
	
	public interface PortMappingTemplateCallback extends ErrorCallback {
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
	
	public interface KeyUploadCallback extends ErrorCallback {
		void onSuccess(UserKey userKey);
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
	
	public interface GenericErrorCallback {
		void onError(int statusCode, String message);
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
	
	public interface UserKeyRemovalCallback extends GenericErrorCallback {
		void onRemoved();
	}
	
	public interface FlavorsCallback {
		void processFlavors(List<Flavor> flavors);
	}
	
	public interface ComputeSitesCallback {
		void processComputeSites(List<ComputeSite> computeSites);
	}
	
	public interface AggregateApplianceCallback extends ErrorCallback {
		void processAppliances(List<AggregateAppliance> appliances);
	}
	
	public interface AggregateApplianceTypesCallback extends ErrorCallback {
		void processApplianceTypes(List<AggregateApplianceType> applianceTypes);
	}
	
	public interface RemoveApplianceTypeCallback extends ErrorCallback {
		void onApplianceTypeRemoved();
	}
	
	public interface ApplianceInstanceCallback extends ErrorCallback {
		public abstract void processApplianceInstance(ApplianceInstance applianceInstance);
	}
	
	private ApplianceTypeService applianceTypesService;
	private ApplianceInstanceService applianceInstancesService;
	private ApplianceSetService applianceSetService;
	private ApplianceConfigurationService applianceConfigurationService;
	private PopupErrorHandler popupErrorHandler;
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
	private CfErrorReader errorReader;
	private FlavorService flavorService;
	private AggregateApplianceService aggregateService;
	private AggregateApplianceTypeService aggregateApplianceTypeService;
	
	@Inject
	public CloudFacadeController(ApplianceTypeService applianceTypesService, ApplianceInstanceService applianceInstancesService,
			ApplianceSetService applianceSetService, ApplianceConfigurationService applianceConfigurationService,
			ApplianceVmService applianceVmsService, ComputeSiteService computeSitesService,
			PortMappingTemplateService portMappingTemplateService, HttpMappingService httpMappingService,
			EndpointService endpointService, UserKeyService userKeyService,
			DevelopmentModePropertySetService developmentModePropertySetService,
			PortMappingService portMappingService, UserService userService,
			PortMappingTemplatePropertyService portMappingTemplatePropertyService, FlavorService flavorService,
			AggregateApplianceService aggregateService, AggregateApplianceTypeService aggregateApplianceTypeService, 
			PopupErrorHandler popupErrorHandler, CfErrorReader errorReader) {
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
		this.flavorService = flavorService;
		this.aggregateService = aggregateService;
		this.aggregateApplianceTypeService = aggregateApplianceTypeService;
		this.popupErrorHandler = popupErrorHandler;
		this.errorReader = errorReader;
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
			}
		});
	}
	
	public void getApplianceType(String applianceTypeId, final ApplianceTypeCallback applianceTypeCallback) {
		applianceTypesService.getApplianceType(applianceTypeId, new MethodCallback<ApplianceTypeRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
			}

			@Override
			public void onSuccess(Method method, ApplianceTypeRequestResponse response) {
				if (applianceTypeCallback != null) {
					applianceTypeCallback.processApplianceType(response.getApplianceType());
				}
			}
		});
	}

	public void startApplianceTypes(final List<String> configurationTemplateIds, final Map<String, List<String>> computeSiteIds, final Command command) {
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
					
					if(computeSiteIds != null && computeSiteIds.get(configurationTemplateId) != null) {
						applianceInstance.setComputeSiteIds(computeSiteIds.get(configurationTemplateId));
					}
					
					applianceInstancesService.addApplianceInstance(applianceInstanceRequest, new MethodCallback<ApplianceInstanceRequestResponse>() {
						@Override
						public void onFailure(Method method, Throwable exception) {
							failed.add(configurationTemplateId);
							popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
					applianceInstancesCallback.onError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
						popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
		NewApplianceConfiguration applianceConfiguration = new NewApplianceConfiguration();
		applianceConfiguration.setApplianceTypeId(applianceTypeId);
		applianceConfiguration.setName(name);
		applianceConfiguration.setPayload(payload);
		newApplianceConfigurationRequest.setApplianceConfiguration(applianceConfiguration);
		applianceConfigurationService.addApplianceConfiguration(newApplianceConfigurationRequest, new MethodCallback<ApplianceConfigurationRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
			}

			@Override
			public void onSuccess(Method method, ApplianceSetsResponse response) {
				if (response.getApplianceSets().size() > 0) {
					ApplianceSet portalApplianceSet = response.getApplianceSets().get(0);
					applianceInstancesService.getApplianceInstances(portalApplianceSet.getId(), new MethodCallback<ApplianceInstancesResponse>() {
						@Override
						public void onFailure(Method method, Throwable exception) {
							popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
			}

			@Override
			public void onSuccess(Method method, PortMappingTemplatesResponse response) {
				if (portMappingTemplatesCallback != null) {
					portMappingTemplatesCallback.processPortMappingTemplates(response.getPortMappingTemplates());
				}
			}
		});
	}

	public void getEndpoints(String portMappingTemplateId, final EndpointsCallback endpointsCallback) {
		endpointService.getEndpoints(portMappingTemplateId, new MethodCallback<EndpointsResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				if(keyUploadCallback != null) {
					keyUploadCallback.onError(errorReader.decodeError(method.getResponse().getText()));
				}
			}

			@Override
			public void onSuccess(Method method, UserKeyRequestResponse response) {
				if(keyUploadCallback != null) {
					keyUploadCallback.onSuccess(response.getUserKey());
				}
			}
		});
	}

	public void removeUserKey(String userKeyId, final UserKeyRemovalCallback callback) {
		userKeyService.deleteUserKey(userKeyId, new MethodCallback<Void>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				if(callback != null) {
					callback.onError(0, exception.getMessage());
				} else {
					popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
				}
			}

			@Override
			public void onSuccess(Method method, Void response) {
				if (callback != null) {
					callback.onRemoved();
				}
			}
		});
	}

	public void removeInitialConfiguration(String initialConfigurationId, final Command command) {
		applianceConfigurationService.deleteApplianceConfiguration(initialConfigurationId, new MethodCallback<Void>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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

	public void startApplianceTypes(final Map<String, Map<String, String>> parameterValues, final Map<String, List<String>> computeSiteIds, final Command command) {
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
					
					if(computeSiteIds != null && computeSiteIds.get(configurationTemplateId) != null) {
						applianceInstance.setComputeSiteIds(computeSiteIds.get(configurationTemplateId));
					}
					
					NewApplianceInstanceRequest applianceInstanceRequest = new NewApplianceInstanceRequest();
					applianceInstanceRequest.setApplianceInstance(applianceInstance);
					applianceInstancesService.addApplianceInstance(applianceInstanceRequest, new MethodCallback<ApplianceInstanceRequestResponse>() {
						@Override
						public void onFailure(Method method, Throwable exception) {
							failed.add(configurationTemplateId);
							popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
		applianceTypesService.getApplianceTypesForModeAndActiveFlag("development", true, new MethodCallback<ApplianceTypesResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
		applianceTypesService.getApplianceTypesForModeAndActiveFlag("production", true, new MethodCallback<ApplianceTypesResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
			}

			@Override
			public void onSuccess(Method method, ApplianceTypesResponse response) {
				if (applianceTypesCallback != null) {
					applianceTypesCallback.processApplianceTypes(response.getApplianceTypes());
				}
			}
		});
	}

	public void startApplianceTypesInDevelopment(final Map<String, String> overrideNames, final String keyId, final Map<String, Map<String, String>> parameterValues,
			final Map<String, String> cores, final Map<String, String> rams, final Map<String, String> disks, final Map<String, List<String>> computeSiteIds, final Command command) {
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
					applianceInstance.setDevelopmentProperties(new HashMap<String, String>());
					applianceInstance.getDevelopmentProperties().put("preference_cpu", cores.get(configurationTemplateId));
					applianceInstance.getDevelopmentProperties().put("preference_memory", rams.get(configurationTemplateId));
					applianceInstance.getDevelopmentProperties().put("preference_disk", disks.get(configurationTemplateId));
					
					if (overrideNames != null && overrideNames.containsKey(configurationTemplateId)) {
						applianceInstance.setName(overrideNames.get(configurationTemplateId));
					}
					
					if(computeSiteIds != null && computeSiteIds.get(configurationTemplateId) != null) {
						applianceInstance.setComputeSiteIds(computeSiteIds.get(configurationTemplateId));
					}
					
					NewApplianceInstanceRequest applianceInstanceRequest = new NewApplianceInstanceRequest();
					applianceInstanceRequest.setApplianceInstance(applianceInstance);
					applianceInstancesService.addApplianceInstance(applianceInstanceRequest, new MethodCallback<ApplianceInstanceRequestResponse>() {
						@Override
						public void onFailure(Method method, Throwable exception) {
							failed.add(configurationTemplateId);
							popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
						popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
		portMappingService.getPortMappingsForVirtualMachineId(applianceInstanceId, new MethodCallback<PortMappingResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
		NewPortMappingTemplateRequest request = createNewPortMappingTemplateRequest(name, portNumber, transportProtocol, applicationProtocol, developmentModePropertySetId, null);
		portMappingTemplateService.addPortMappingTemplate(request, new MethodCallback<PortMappingTemplateRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				if(portMappingTemplateCallback != null) {
					portMappingTemplateCallback.onError(errorReader.decodeError(method.getResponse().getText()));
				}
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
		NewPortMappingTemplateRequest request = createNewPortMappingTemplateRequest(name, portNumber, transportProtocol, applicationProtocol, null, applianceTypeId);
		portMappingTemplateService.addPortMappingTemplate(request, new MethodCallback<PortMappingTemplateRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				if(portMappingTemplateCallback != null) {
					portMappingTemplateCallback.onError(errorReader.decodeError(method.getResponse().getText()));
				}
			}

			@Override
			public void onSuccess(Method method, PortMappingTemplateRequestResponse response) {
				if (portMappingTemplateCallback != null) {
					portMappingTemplateCallback.processPortMappingTemplate(response.getPortMappingTemplate());
				}
			}
		});
	}
	
	private NewPortMappingTemplateRequest createNewPortMappingTemplateRequest(String name, int portNumber, String transportProtocol, final String applicationProtocol,
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
			}

			@Override
			public void onSuccess(Method method, PortMappingTemplatesResponse response) {
				if (portMappingTemplatesCallback != null) {
					portMappingTemplatesCallback.processPortMappingTemplates(response.getPortMappingTemplates());
				}
			}
		});
	}

	public void getPortMappingsForPortMappingTemplateIdOrVirtualMachineId(String portMappingTemplateId, String virtualMachineId, final PortMappingsCallback portMappingsCallback) {
		if(virtualMachineId == null) {
			portMappingService.getPortMappingsForPortMappingTemplateId(portMappingTemplateId, new MethodCallback<PortMappingResponse>() {
				@Override
				public void onFailure(Method method, Throwable exception) {
					popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
				}
	
				@Override
				public void onSuccess(Method method, PortMappingResponse response) {
					if (portMappingsCallback != null) {
						portMappingsCallback.processPortMappings(response.getPortMappings());
					}
				}
			});
		} else {
			portMappingService.getPortMappingsForVirtualMachineId(virtualMachineId, new MethodCallback<PortMappingResponse>() {
				@Override
				public void onFailure(Method method, Throwable exception) {
					popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
				}

				@Override
				public void onSuccess(Method method, PortMappingResponse response) {
					if (portMappingsCallback != null) {
						portMappingsCallback.processPortMappings(response.getPortMappings());
					}
				}
			});
		}
	}

	public void getHttpMappingsForPortMappingTemplateIdOrInstanceId(String portMappingTemplateId, String instanceId, final HttpMappingsCallback httpMappingsCallback) {
		if(instanceId == null) {
			httpMappingService.getHttpMappingsForPortMappingTemplateId(portMappingTemplateId, new MethodCallback<HttpMappingResponse>() {
				@Override
				public void onFailure(Method method, Throwable exception) {
					popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));	
				}
	
				@Override
				public void onSuccess(Method method, HttpMappingResponse response) {
					if (httpMappingsCallback != null) {
						httpMappingsCallback.processHttpMappings(response.getHttpMappings());
					}
				}
			});
		} else {
			httpMappingService.getHttpMappings(instanceId, new MethodCallback<HttpMappingResponse>() {
				@Override
				public void onFailure(Method method, Throwable exception) {
					popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
				}

				@Override
				public void onSuccess(Method method, HttpMappingResponse response) {
					if (httpMappingsCallback != null) {
						httpMappingsCallback.processHttpMappings(response.getHttpMappings());
					}
				}
			});
		}
	}

	public void removeEndpoint(String endpointId, final Command command) {
		endpointService.deleteEndpoint(endpointId, new MethodCallback<Void>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
			String description, String descriptor, boolean secured, final EndpointCallback endpointCallback) {
		NewEndpoint endpoint = new NewEndpoint();
		endpoint.setName(name);
		endpoint.setDescription(description);
		endpoint.setDescriptor(descriptor);
		endpoint.setEndpointType(endpointType);
		endpoint.setInvocationPath(invocationPath);
		endpoint.setPortMappingTemplateId(portMappingTemplateId);
		endpoint.setSecured(secured);
		
		NewEndpointRequest request = new NewEndpointRequest();
		request.setEndpoint(endpoint);
		endpointService.addEndpoint(request, new MethodCallback<EndpointRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
					errorCallback.onError(errorReader.decodeError(method.getResponse().getText()));
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
					errorCallback.onError(errorReader.decodeError(method.getResponse().getText()));
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
	
	public void getRedirectionsForAppliance(final ApplianceInstance instance, final RedirectionsCallback callback) {
		getPortMappingTemplates(instance.getApplianceTypeId(), new PortMappingTemplatesCallback() {
			@Override
			public void processPortMappingTemplates(List<PortMappingTemplate> portMappingTemplates) {
				buildRedirections(portMappingTemplates, instance.getId(), instance.getVirtualMachineIds().get(0), callback);
			}
		});
	}
	
	public void getRedirectionsForDevPropertySetId(String devPropertySetId, final RedirectionsCallback callback) {
		getPortMappingTemplatesForDevelopmentModePropertySetId(devPropertySetId, new PortMappingTemplatesCallback() {
			@Override
			public void processPortMappingTemplates(List<PortMappingTemplate> portMappingTemplates) {
				buildRedirections(portMappingTemplates, null, null, callback);
			}
		});
	}

	protected void buildRedirections(List<PortMappingTemplate> portMappingTemplates, String instanceId, String virtualMachineId, final RedirectionsCallback callback) {
		final List<Redirection> redirections = new ArrayList<Redirection>();
		
		if(portMappingTemplates.size() == 0 && callback != null) {
			callback.processRedirections(redirections);
		}
		
		final List<MutableBoolean> finishedCallbacks = new ArrayList<MutableBoolean>();
		
		for(PortMappingTemplate portMappingTemplate : portMappingTemplates) {
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
				getHttpMappingsForPortMappingTemplateIdOrInstanceId(portMappingTemplate.getId(), instanceId, new HttpMappingsCallback() {
					@Override
					public void processHttpMappings(List<HttpMapping> httpMappings) {
						for (HttpMapping httpMapping : httpMappings) {
							if ("http".equals(httpMapping.getApplicationProtocol())) {
								redirection.setHttpUrl(httpMapping.getUrl());
								redirection.setHttpUrlStatus(httpMapping.getStatus());
							} else if ("https".equals(httpMapping.getApplicationProtocol())) {
								redirection.setHttpsUrl(httpMapping.getUrl());
								redirection.setHttpsUrlStatus(httpMapping.getStatus());
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
				getPortMappingsForPortMappingTemplateIdOrVirtualMachineId(portMappingTemplate.getId(), virtualMachineId, new PortMappingsCallback() {
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
			}

			@Override
			public void onSuccess(Method method, UserRequestResponse response) {
				if (userCallback != null) {
					userCallback.processUser(response.getUser());
				}
			}
		});
	}

	public void getManagedApplianceTypes(String userLogin, final OwnedApplianceTypesCallback ownedApplianceTypesCallback) {
		userService.getUserForLogin(userLogin, new MethodCallback<UsersResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
			}

			@Override
			public void onSuccess(Method method, UsersResponse response) {
				final List<OwnedApplianceType> result = new ArrayList<OwnedApplianceType>();
				
				if (response.getUsers().size() > 0) {
					final User user = response.getUsers().get(0);
					applianceTypesService.getManagedApplianceTypes(new MethodCallback<ApplianceTypesResponse>() {
						@Override
						public void onFailure(Method method, Throwable exception) {
							popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
						}

						@Override
						public void onSuccess(Method method, ApplianceTypesResponse response) {
							for (ApplianceType applianceType : response.getApplianceTypes()) {
								OwnedApplianceType ownedApplianceType = new OwnedApplianceType();
								ownedApplianceType.setApplianceType(applianceType);
								ownedApplianceType.setUser(user);
								result.add(ownedApplianceType);
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

	public void removeApplianceType(String applianceTypeId, final RemoveApplianceTypeCallback callback) {
		applianceTypesService.deleteApplianceType(applianceTypeId, new MethodCallback<Void>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				CloudFacadeError error = errorReader.decodeError(method.getResponse().getText());
				
				if(callback != null) {
					callback.onError(error);
				}
				
				popupErrorHandler.displayError(error);
			}

			@Override
			public void onSuccess(Method method, Void response) {
				if(callback != null) {
					callback.onApplianceTypeRemoved();
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
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
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
			}

			@Override
			public void onSuccess(Method method, PortMappingTemplatePropertiesResponse response) {
				if (portMappingTemplatePropertiesCallback != null) {
					portMappingTemplatePropertiesCallback.processPortMappingTemplateProperties(response.getProperties());
				}
			}
		});
	}

	public void updatePortMappingTemplateForDevelopmentModePropertySet(String editMappingId, String name,
			int portNumber, String transportProtocol, final String applicationProtocol, String developmentModePropertySetId,
			final PortMappingTemplateCallback portMappingTemplateCallback) {
		NewPortMappingTemplateRequest request = createNewPortMappingTemplateRequest(name, portNumber, transportProtocol, applicationProtocol, developmentModePropertySetId, null);
		portMappingTemplateService.updatePortMappingTemplate(editMappingId, request, new MethodCallback<PortMappingTemplateRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				if(portMappingTemplateCallback != null) {
					portMappingTemplateCallback.onError(errorReader.decodeError(method.getResponse().getText()));
				}
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

	public void updatePortMappingTemplateForApplianceType(String mappingId, String name, int portNumber,
			String transportProtocol, String applicationProtocol, String applianceTypeId,
			final PortMappingTemplateCallback portMappingTemplateCallback) {
		NewPortMappingTemplateRequest request = createNewPortMappingTemplateRequest(name, portNumber, transportProtocol, applicationProtocol, null, applianceTypeId);
		portMappingTemplateService.updatePortMappingTemplate(mappingId, request, new MethodCallback<PortMappingTemplateRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				portMappingTemplateCallback.onError(errorReader.decodeError(method.getResponse().getText()));
			}

			@Override
			public void onSuccess(Method method, PortMappingTemplateRequestResponse response) {
				if (portMappingTemplateCallback != null) {
					portMappingTemplateCallback.processPortMappingTemplate(response.getPortMappingTemplate());
				}
			}
		});
	}

	public void updatePortMappingProperty(String portMappingTemplateId, String propertyId, String name, String value, final Command onFinish) {
		NewPortMappingTemplateProperty property = new NewPortMappingTemplateProperty();
		property.setPortMappingTemplateId(portMappingTemplateId);
		property.setKey(name);
		property.setValue(value);
		
		NewPortMappingTemplatePropertyRequest portMappingTemplatePropertyRequest = new NewPortMappingTemplatePropertyRequest();
		portMappingTemplatePropertyRequest.setPortMappingTemplateProperty(property);
		portMappingTemplatePropertyService.updatePortMappingTemplateProperty(propertyId, portMappingTemplatePropertyRequest, new MethodCallback<PortMappingTemplatePropertyRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
			}

			@Override
			public void onSuccess(Method method, PortMappingTemplatePropertyRequestResponse response) {
				if (onFinish != null) {
					onFinish.execute();
				}
			}
		});
	}

	public void removePortMappingProperty(String propertyId, final Command command) {
		portMappingTemplatePropertyService.removePortMappingTemplateProperety(propertyId, new MethodCallback<Void>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
			}

			@Override
			public void onSuccess(Method method, Void response) {
				if(command != null) {
					command.execute();
				}
			}
		});
	}

	public void updateEndpoint(String endpointId, String name, String invocationPath, String endpointType,
			String portMappingTemplateId, String description, String descriptor, boolean secured,
			final EndpointCallback endpointCallback) {
		NewEndpoint endpoint = new NewEndpoint();
		endpoint.setName(name);
		endpoint.setDescription(description);
		endpoint.setDescriptor(descriptor);
		endpoint.setEndpointType(endpointType);
		endpoint.setInvocationPath(invocationPath);
		endpoint.setPortMappingTemplateId(portMappingTemplateId);
		endpoint.setSecured(secured);
		
		NewEndpointRequest request = new NewEndpointRequest();
		request.setEndpoint(endpoint);
		endpointService.updateEndpoint(endpointId, request, new MethodCallback<EndpointRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
			}

			@Override
			public void onSuccess(Method method, EndpointRequestResponse response) {
				if (endpointCallback != null) {
					endpointCallback.processEndpoint(response.getEndpoint());
				}
			}
		});
	}

	public void getFlavors(String applianceTypeId, String cpu, String memory, String disk, String computeSiteId, final FlavorsCallback callback) {
		if(computeSiteId == null) {
			flavorService.getFlavors(applianceTypeId, cpu, memory, disk, new MethodCallback<FlavorsResponse>() {
				@Override
				public void onFailure(Method method, Throwable exception) {
					popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
				}
	
				@Override
				public void onSuccess(Method method, FlavorsResponse response) {
					if(callback != null) {
						callback.processFlavors(response.getFlavors());
					}
				}
			});
		} else {
			flavorService.getFlavors(applianceTypeId, cpu, memory, disk, computeSiteId, new MethodCallback<FlavorsResponse>() {
				@Override
				public void onFailure(Method method, Throwable exception) {
					popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
				}
	
				@Override
				public void onSuccess(Method method, FlavorsResponse response) {
					if(callback != null) {
						callback.processFlavors(response.getFlavors());
					}
				}
			});
		}
	}

	public void getComputeSites(List<String> computeSiteIds, final ComputeSitesCallback computeSitesCallback) {
		computeSitesService.getComputeSites(join(computeSiteIds, ","), new MethodCallback<ComputeSitesResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
			}

			@Override
			public void onSuccess(Method method, ComputeSitesResponse response) {
				if(computeSitesCallback != null) {
					computeSitesCallback.processComputeSites(response.getComputeSites());
				}
			}
		});
	}

	public void getUsers(final UsersCallback callback) {
		userService.getUsers(new MethodCallback<UsersResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
			}

			@Override
			public void onSuccess(Method method, UsersResponse response) {
				if(callback != null) {
					callback.processUsers(response.getUsers());
				}
			}
		});
	}
	
	public void getFlavors(List<String> flavorIds, final FlavorsCallback callback) {
		flavorService.getFlavors(join(flavorIds, ","), new MethodCallback<FlavorsResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
			}

			@Override
			public void onSuccess(Method method, FlavorsResponse response) {
				if(callback != null) {
					callback.processFlavors(response.getFlavors());
				}
			}
		});
	}

	public void rebootApplianceInstance(String instanceId, final Command command) {
		applianceInstancesService.reboot(instanceId, new RebootRequest(), new MethodCallback<Void>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
				
				if(command != null) {
					command.execute();
				}
			}

			@Override
			public void onSuccess(Method method, Void response) {
				if(command != null) {
					command.execute();
				}
			}
		});
	}
	
	public void aggregatedInstances(Type type, final AggregateApplianceCallback callback) {
		aggregateService.getAggregateAppliances(type.name(), new MethodCallback<AggregateAppliancesResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
			}

			@Override
			public void onSuccess(Method method, AggregateAppliancesResponse response) {
				callback.processAppliances(response.getAppliances().getAppliances());
			}
		});
	}
	
	public void aggregateApplianceTypes(String mode, final AggregateApplianceTypesCallback callback) {
		aggregateApplianceTypeService.getAggregateAppliances(mode, new MethodCallback<AggregateApplianceTypesResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				popupErrorHandler.displayError(errorReader.decodeError(method.getResponse().getText()));
			}

			@Override
			public void onSuccess(Method method, AggregateApplianceTypesResponse response) {
				List<AggregateApplianceType> applianceTypes = response.getApplianceTypes().getApplianceTypes();
				collectComputeSites(applianceTypes, response.getApplianceTypes().getComputeSites());
				callback.processApplianceTypes(applianceTypes);
			}
		});
	}
	
	public void getApplianceInstance(String applianceInstanceId, final ApplianceInstanceCallback callback) {
		applianceInstancesService.getApplianceInstance(applianceInstanceId, new MethodCallback<ApplianceInstancesResponse>() {
			@Override
			public void onSuccess(Method method, ApplianceInstancesResponse response) {
				if(response.getApplianceInstances() != null && response.getApplianceInstances().size() > 0) {
					callback.processApplianceInstance(response.getApplianceInstances().get(0));
				} else {
					callback.processApplianceInstance(null);
				}
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				CloudFacadeError error = errorReader.decodeError(method.getResponse().getText());
				popupErrorHandler.displayError(error);
				callback.onError(error);
			}
		});
	}
	
	private void collectComputeSites(List<AggregateApplianceType> applianceTypes, List<ComputeSite> computeSites) {
		for(AggregateApplianceType applianceType : applianceTypes) {
			applianceType.setComputeSites(new HashMap<String, ComputeSite>());
			
			for(String computeSiteId : applianceType.getComputeSiteIds()) {
				for (ComputeSite computeSite : computeSites) {
					if(computeSite.getId().equals(computeSiteId)) {
						applianceType.getComputeSites().put(computeSiteId, computeSite);
						
						break;
					}
				}
			}
		}
	}

	public void saveApplianceTypeInPlace(String applianceInstanceId, String applianceTypeId, final ApplianceTypeCallback callback) {
		ApplianceTypeSaveInPlace applianceType = new ApplianceTypeSaveInPlace();
		applianceType.setApplianceInstanceId(applianceInstanceId);
		
		ApplianceTypeSaveInPlaceRequest request = new ApplianceTypeSaveInPlaceRequest();
		request.setApplianceType(applianceType);
		applianceTypesService.saveInPlace(applianceTypeId, request, new MethodCallback<ApplianceTypeRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				CloudFacadeError error = errorReader.decodeError(method.getResponse().getText());
				popupErrorHandler.displayError(error);
				callback.onError(error);
			}

			@Override
			public void onSuccess(Method method, ApplianceTypeRequestResponse response) {
				callback.processApplianceType(response.getApplianceType());
			}
		});
	}
}