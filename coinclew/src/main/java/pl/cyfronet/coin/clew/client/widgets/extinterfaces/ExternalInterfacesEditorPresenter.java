package pl.cyfronet.coin.clew.client.widgets.extinterfaces;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.UrlHelper;
import pl.cyfronet.coin.clew.client.auth.MiTicketReader;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.DevelopmentModePropertySetCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.EndpointCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.EndpointsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.HttpMappingsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.PortMappingTemplateCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.PortMappingTemplatePropertiesCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.PortMappingTemplatesCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.PortMappingsCallback;
import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeError;
import pl.cyfronet.coin.clew.client.controller.cf.devmodepropertyset.DevelopmentModePropertySet;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.Endpoint;
import pl.cyfronet.coin.clew.client.controller.cf.httpmapping.HttpMapping;
import pl.cyfronet.coin.clew.client.controller.cf.portmapping.PortMapping;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate.PortMappingTemplate;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplateproperty.PortMappingTemplateProperty;
import pl.cyfronet.coin.clew.client.widgets.extinterfaces.IExternalInterfacesView.IExternalInterfacesPresenter;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = ExternalInterfacesEditorView.class)
public class ExternalInterfacesEditorPresenter extends BasePresenter<IExternalInterfacesView, MainEventBus> implements IExternalInterfacesPresenter {
	private static final String PROXY_READ_TIMEOUT = "proxy_read_timeout";
	private static final String PROXY_SEND_TIMEOUT = "proxy_send_timeout";
	
	private CloudFacadeController cloudFacadeController;
	private String applianceInstanceId;
	private Map<PortMappingTemplate, IsWidget> mappings;
	private String developmentModePropertySetId;
	private Map<Endpoint, IsWidget> endpoints;
	private boolean endpointsExist;
	private String applianceTypeId;
	private MiTicketReader ticketReader;
	private boolean editMappingMode;
	private boolean editEndpointMode;
	private Map<String, Map<String, PortMappingTemplateProperty>> mappingProperties;
	private String editMappingId;
	private String editEndpointId;

	@Inject
	public ExternalInterfacesEditorPresenter(CloudFacadeController cloudFacadeController, MiTicketReader ticketReader) {
		this.cloudFacadeController = cloudFacadeController;
		this.ticketReader = ticketReader;
		mappings = new HashMap<PortMappingTemplate, IsWidget>();
		endpoints = new HashMap<Endpoint, IsWidget>();
		mappingProperties = new HashMap<String, Map<String, PortMappingTemplateProperty>>();
	}
	
	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onShowExternalInterfacesEditor(String applianceInstanceId) {
		this.applianceInstanceId = applianceInstanceId;
		applianceTypeId = null;
		developmentModePropertySetId = null;
		view.switchToMappingsTab();
		clearEndpointForm();
		clearExternalInterfaceForm();
		view.showModal(true);
		loadExternalInterfacesAndEndpoints();
	}
	
	public void onShowExternalInterfacesEditorForApplianceType(String applianceTypeId) {
		this.applianceTypeId = applianceTypeId;
		applianceInstanceId = null;
		developmentModePropertySetId = null;
		clearEndpointForm();
		clearExternalInterfaceForm();
		view.showModal(true);
		loadExternalInterfacesAndEndpoints();
	}

	private void loadExternalInterfacesAndEndpoints() {
		view.getExternalInterfaceContainer().clear();
		view.showNoExternalInterfacesLabel(false);
		view.showExternalInterfacesLoadingIndicator(true);
		view.getEndpointsContainer().clear();
		view.showNoEndpointsLabel(false);
		view.showEndpointsLoadingIndicator(true);
		endpointsExist = false;
		view.clearEndpointTargetPorts();
		view.showEndpointTargetPortHelpBlock(true);
		mappings.clear();
		endpoints.clear();
		mappingProperties.clear();
		view.enableEndpoints(false);
		editMappingMode = false;
		editEndpointMode = false;
		editMappingId = null;
		editEndpointId = null;
		
		retrievePortMappingTemplates(new PortMappingTemplatesCallback() {
			@Override
			public void processPortMappingTemplates(List<PortMappingTemplate> portMappingTemplates) {
				if (portMappingTemplates.size() == 0) {
					view.showExternalInterfacesLoadingIndicator(false);
					view.showNoExternalInterfacesLabel(true);
					view.showEndpointsLoadingIndicator(false);
					view.showNoEndpointsLabel(true);
				} else {
					view.showExternalInterfacesLoadingIndicator(false);
					view.showEndpointsLoadingIndicator(false);

					for (final PortMappingTemplate portMappingTemplate : portMappingTemplates) {
						if (Arrays.asList(new String[] {"http", "https", "http_https"}).contains(portMappingTemplate.getApplicationProtocol())) {
							//http mapping
							view.enableEndpoints(true);
							view.addHttpMappingEndpointOption(portMappingTemplate.getId(), portMappingTemplate.getServiceName(), portMappingTemplate.getTargetPort());
							view.showEndpointTargetPortHelpBlock(false);
							cloudFacadeController.getHttpMappingsForPortMappingTemplateIdOrInstanceId(portMappingTemplate.getId(), null, new HttpMappingsCallback() {
								@Override
								public void processHttpMappings(final List<HttpMapping> httpMappings) {
									String httpUrl = asList("http", "http_https").contains(portMappingTemplate.getApplicationProtocol()) ? "" : null;
									String httpsUrl = asList("https", "http_https").contains(portMappingTemplate.getApplicationProtocol()) ? "" : null;
									
									for (HttpMapping httpMapping : httpMappings) {
										if (httpMapping.getApplicationProtocol().equals("http")) {
											httpUrl = httpMapping.getUrl();
										} else if (httpMapping.getApplicationProtocol().equals("https")) {
											httpsUrl = httpMapping.getUrl();
										}
									}
									
									final String httpUrlValue = httpUrl;
									final String httpsUrlValue = httpsUrl;
									
									cloudFacadeController.getPortMappingTemplateProperties(portMappingTemplate.getId(), new PortMappingTemplatePropertiesCallback() {
										@Override
										public void processPortMappingTemplateProperties(List<PortMappingTemplateProperty> portMappingTemplateProperties) {
											Map<String, String> properties = new HashMap<String, String>();
											Map<String, PortMappingTemplateProperty> propertyBeans = new HashMap<String, PortMappingTemplateProperty>();
											
											for (PortMappingTemplateProperty property : portMappingTemplateProperties) {
												properties.put(property.getKey(), property.getValue());
												propertyBeans.put(property.getKey(), property);
											}
											
											mappingProperties.put(portMappingTemplate.getId(), propertyBeans);
											
											IsWidget widget = view.addMapping(getMappingPosition(portMappingTemplate.getServiceName()), portMappingTemplate.getId(), portMappingTemplate.getServiceName(),
													portMappingTemplate.getTargetPort(), portMappingTemplate.getTransportProtocol(),
													httpUrlValue, httpsUrlValue, null, null, properties);
											mappings.put(portMappingTemplate, widget);
										}});
									
									//for http mappings there could be endpoints defined
									cloudFacadeController.getEndpoints(portMappingTemplate.getId(), new EndpointsCallback() {
										@Override
										public void processEndpoints(List<Endpoint> endpoints) {
											if (endpoints.size() == 0) {
												if (!endpointsExist) {
													view.showNoEndpointsLabel(true);
												}
											} else {
												view.showNoEndpointsLabel(false);
												endpointsExist = true;
												
												for (Endpoint endpoint : endpoints) {
													String httpUrl = null;
													String httpsUrl = null;
													
													for (HttpMapping httpMapping : httpMappings) {
														if (httpMapping.getApplicationProtocol().equals("http")) {
															httpUrl = httpMapping.getUrl();
														} else if (httpMapping.getApplicationProtocol().equals("https")) {
															httpsUrl = httpMapping.getUrl();
														}
													}
													
													IsWidget widget = view.addEndpoint(getEndpointPosition(endpoint.getName()), endpoint.getId(), endpoint.getName(), endpoint.isSecured(),
															UrlHelper.joinUrl(httpUrl, endpoint.getInvocationPath(),
																	endpoint.isSecured() ? ticketReader.getUserLogin() : null, endpoint.isSecured() ? ticketReader.getTicket() : null),
															UrlHelper.joinUrl(httpsUrl, endpoint.getInvocationPath(),
																	endpoint.isSecured() ? ticketReader.getUserLogin() : null, endpoint.isSecured() ? ticketReader.getTicket() : null));
													ExternalInterfacesEditorPresenter.this.endpoints.put(endpoint, widget);
												}
											}
										}
									});
								}
							});
						} else {
							//tcp or udp mapping
							if (!endpointsExist) {
								view.showNoEndpointsLabel(true);
							}
							
							cloudFacadeController.getPortMappingsForPortMappingTemplateIdOrVirtualMachineId(portMappingTemplate.getId(), null, new PortMappingsCallback() {
								@Override
								public void processPortMappings(List<PortMapping> portMappings) {
									String publicIp = "";
									String sourcePort = "";
									
									if (portMappings.size() > 0) {
										PortMapping portMapping = portMappings.get(0);
										publicIp = portMapping.getPublicIp();
										sourcePort = portMapping.getSourcePort();
									}
									
									IsWidget widget = view.addMapping(getMappingPosition(portMappingTemplate.getServiceName()), portMappingTemplate.getId(),
											portMappingTemplate.getServiceName(), portMappingTemplate.getTargetPort(),
											portMappingTemplate.getTransportProtocol(), null, null, publicIp, sourcePort, null);
									mappings.put(portMappingTemplate, widget);
								}});
						}
					}
				}
			}
		});
	}
	
	private boolean httpMappingExists() {
		for (PortMappingTemplate mappingTemplate : mappings.keySet()) {
			if (asList("http", "https", "http_https")
					.contains(mappingTemplate.getApplicationProtocol())) {
				return true;
			}
		}
		
		return false;
	}

	private int getMappingPosition(String serviceName) {
		int beforePosition = 0;
		
		if (mappings.size() > 0) {
			List<String> serviceNames = new ArrayList<String>();
			
			for (PortMappingTemplate mapping : mappings.keySet()) {
				serviceNames.add(mapping.getServiceName());
			}
			
			Collections.sort(serviceNames);

			while (beforePosition < mappings.size() && serviceName.toLowerCase().compareTo(serviceNames.get(beforePosition).toLowerCase()) > 0) {
				beforePosition++;
			}
		}
		
		return beforePosition;
	}
	
	private int getEndpointPosition(String endpointName) {
		int beforePosition = 0;
		
		if (endpoints.size() > 0) {
			List<String> endpointNames = new ArrayList<String>();
			
			for (Endpoint endpoint : endpoints.keySet()) {
				endpointNames.add(endpoint.getName());
			}
			
			Collections.sort(endpointNames);

			while (beforePosition < endpoints.size() && endpointName.toLowerCase().compareTo(endpointNames.get(beforePosition).toLowerCase()) > 0) {
				beforePosition++;
			}
		}
		
		return beforePosition;
	}
	
	private void retrievePortMappingTemplates(final PortMappingTemplatesCallback portMappingTemplatesCallback) {
		if (applianceInstanceId != null) {
			cloudFacadeController.getDevelopmentModePropertySet(applianceInstanceId, new DevelopmentModePropertySetCallback() {
				@Override
				public void processDeveopmentModePropertySet(DevelopmentModePropertySet developmentModePropertySet) {
					developmentModePropertySetId = developmentModePropertySet.getId();
					
					if (developmentModePropertySet.getPortMappingTemplateIds().size() > 0) {
						cloudFacadeController.getPortMappingTemplatesForDevelopmentModePropertySetId(developmentModePropertySet.getId(), new PortMappingTemplatesCallback() {
							@Override
							public void processPortMappingTemplates(List<PortMappingTemplate> portMappingTemplates) {
								if (portMappingTemplatesCallback != null) {
									portMappingTemplatesCallback.processPortMappingTemplates(portMappingTemplates);
								}
							}
						});
					} else {
						if (portMappingTemplatesCallback != null) {
							portMappingTemplatesCallback.processPortMappingTemplates(new ArrayList<PortMappingTemplate>());
						}
					}
				}
				
			});
		} else if (applianceTypeId != null) {
			cloudFacadeController.getPortMappingTemplates(applianceTypeId, new PortMappingTemplatesCallback() {
				@Override
				public void processPortMappingTemplates(List<PortMappingTemplate> portMappingTemplates) {
					if (portMappingTemplatesCallback != null) {
						portMappingTemplatesCallback.processPortMappingTemplates(portMappingTemplates);
					}
				}
			});
		}
	}

	@Override
	public void onTransportProtocolChanged() {
		String transportProtocol = view.getTransportProtocol().getValue();
		
		if (transportProtocol.equals("tcp")) {
			view.setApplicationProtocolEnabled(true);
		} else {
			view.setApplicationProtocolEnabled(false);
		}
	}

	@Override
	public void onRemoveMapping(final String mappingId) {
		if (view.confirmMappingRemoval()) {
			if(editMappingMode) {
				editMappingMode = false;
				clearExternalInterfaceForm();
			}
			
			cloudFacadeController.getEndpoints(mappingId, new EndpointsCallback() {
				@Override
				public void processEndpoints(List<Endpoint> endpoints) {
					if (endpoints.size() > 0) {
						view.showCannotRemoveMappingMessage();
					} else {
						cloudFacadeController.removePortMapingTemplate(mappingId, new Command() {
							@Override
							public void execute() {
								PortMappingTemplate mapping = null;
								
								for (PortMappingTemplate m : mappings.keySet()) {
									if (m.getId().equals(mappingId)) {
										mapping = m;
										
										break;
									}
								}
								
								view.removeMappingTemplate(mappings.remove(mapping));
								view.removeHttpMappingEndpointOption(mappingId);
								
								if (!httpMappingExists()) {
									view.enableEndpoints(false);
								}
								
								if (mappings.size() == 0) {
									view.showNoExternalInterfacesLabel(true);
								}
								
								if (applianceInstanceId != null) {
									eventBus.externalInterfacesChanged(applianceInstanceId);
								}
							}
						});
					}
				}
			});
		}
	}

	@Override
	public void onUpdateExternalInterface() {
		String name = view.getExternalInterfaceName().getText().trim();
		String port = view.getExternalInterfacePort().getText().trim();
		String applicationProtocol = view.getApplicationProtocol().getValue();
		String transportProtocol = view.getTransportProtocol().getValue();
		final String proxySendTimeout = view.getProxySendTimeout().getValue().trim();
		final String proxyReadTimeout = view.getProxyReadTimeout().getValue().trim();
		
		if(name.isEmpty() || port.isEmpty()) {
			view.displayNameOrPortEmptyMessage();
		} else if(!proxySendTimeout.isEmpty() && (!isValidNumber(proxySendTimeout) || applicationProtocol.equals("none"))) {
			view.displayWorngProxySendTimeoutMessage();
		} else if(!proxyReadTimeout.isEmpty() && (!isValidNumber(proxyReadTimeout) || applicationProtocol.equals("none"))) {
			view.displayWorngProxyReadTimeoutMessage();
		} else {
			int portNumber = -1;
			
			try {
				portNumber = Integer.valueOf(port);
			} catch (NumberFormatException e) {
				view.displayWrongPortFormatMessages();
			}
			
			if(portNumber > -1) {
				view.setUpdateExternalInterfaceBusyState(true);
				view.clearErrorMessages();
				updatePortMapping(name, portNumber, transportProtocol, applicationProtocol, new PortMappingTemplateCallback() {
					@Override
					public void processPortMappingTemplate(PortMappingTemplate portMappingTemplate) {
						clearExternalInterfaceForm();
						
						//the list below is used to synchronize property requests so that the loadExternalInterfacesAndEndpoints() method is called once
						final List<String> ongoingRequests = new ArrayList<String>();
						
						if (!proxySendTimeout.isEmpty()) {
							ongoingRequests.add(PROXY_SEND_TIMEOUT);
							
							if(mappingProperties.get(portMappingTemplate.getId()) != null && mappingProperties.get(portMappingTemplate.getId()).get(PROXY_SEND_TIMEOUT) != null) {
								cloudFacadeController.updatePortMappingProperty(portMappingTemplate.getId(),
										mappingProperties.get(portMappingTemplate.getId()).get(PROXY_SEND_TIMEOUT).getId(),
										mappingProperties.get(portMappingTemplate.getId()).get(PROXY_SEND_TIMEOUT).getKey(), proxySendTimeout, new Command(){
											@Override
											public void execute() {
												if(ongoingRequests.size() == 1) {
													loadExternalInterfacesAndEndpoints();
													
													if (applianceInstanceId != null) {
														eventBus.externalInterfacesChanged(applianceInstanceId);
													}
												} else {
													ongoingRequests.remove(PROXY_SEND_TIMEOUT);
												}
											}});
							} else {
								cloudFacadeController.addPortMappingProperty(portMappingTemplate.getId(), PROXY_SEND_TIMEOUT, proxySendTimeout, new Command() {
									@Override
									public void execute() {
										if(ongoingRequests.size() == 1) {
											loadExternalInterfacesAndEndpoints();
											
											if (applianceInstanceId != null) {
												eventBus.externalInterfacesChanged(applianceInstanceId);
											}
										} else {
											ongoingRequests.remove(PROXY_SEND_TIMEOUT);
										}
									}
								});
							}
						} else if(mappingProperties.get(portMappingTemplate.getId()) != null &&
								mappingProperties.get(portMappingTemplate.getId()).get(PROXY_SEND_TIMEOUT) != null) {
							ongoingRequests.add(PROXY_SEND_TIMEOUT);
							cloudFacadeController.removePortMappingProperty(mappingProperties.get(portMappingTemplate.getId()).get(PROXY_SEND_TIMEOUT).getId(), new Command() {
								@Override
								public void execute() {
									if(ongoingRequests.size() == 1) {
										loadExternalInterfacesAndEndpoints();
										
										if (applianceInstanceId != null) {
											eventBus.externalInterfacesChanged(applianceInstanceId);
										} else {
											ongoingRequests.remove(PROXY_SEND_TIMEOUT);
										}
									}
								}});
						}
						
						if (!proxyReadTimeout.isEmpty()) {
							ongoingRequests.add(PROXY_READ_TIMEOUT);
							
							if(mappingProperties.get(portMappingTemplate.getId()) != null && mappingProperties.get(portMappingTemplate.getId()).get(PROXY_READ_TIMEOUT) != null) {
								cloudFacadeController.updatePortMappingProperty(portMappingTemplate.getId(),
										mappingProperties.get(portMappingTemplate.getId()).get(PROXY_READ_TIMEOUT).getId(),
										mappingProperties.get(portMappingTemplate.getId()).get(PROXY_READ_TIMEOUT).getKey(), proxyReadTimeout, new Command(){
											@Override
											public void execute() {
												if(ongoingRequests.size() == 1) {
													loadExternalInterfacesAndEndpoints();
													
													if (applianceInstanceId != null) {
														eventBus.externalInterfacesChanged(applianceInstanceId);
													}
												} else {
													ongoingRequests.remove(PROXY_READ_TIMEOUT);
												}
											}});
							} else {
								cloudFacadeController.addPortMappingProperty(portMappingTemplate.getId(), PROXY_READ_TIMEOUT, proxyReadTimeout, new Command() {
									@Override
									public void execute() {
										if(ongoingRequests.size() == 1) {
											loadExternalInterfacesAndEndpoints();
											
											if (applianceInstanceId != null) {
												eventBus.externalInterfacesChanged(applianceInstanceId);
											}
										} else {
											ongoingRequests.remove(PROXY_READ_TIMEOUT);
										}
									}
								});
							}
						} else if(mappingProperties.get(portMappingTemplate.getId()) != null &&
								mappingProperties.get(portMappingTemplate.getId()).get(PROXY_READ_TIMEOUT) != null) {
							ongoingRequests.add(PROXY_READ_TIMEOUT);
							cloudFacadeController.removePortMappingProperty(mappingProperties.get(portMappingTemplate.getId()).get(PROXY_READ_TIMEOUT).getId(), new Command() {
								@Override
								public void execute() {
									if(ongoingRequests.size() == 1) {
										loadExternalInterfacesAndEndpoints();
										
										if (applianceInstanceId != null) {
											eventBus.externalInterfacesChanged(applianceInstanceId);
										}
									} else {
										ongoingRequests.remove(PROXY_READ_TIMEOUT);
									}
								}});
						}
						
						if (ongoingRequests.size() == 0) {
							loadExternalInterfacesAndEndpoints();
							
							if (applianceInstanceId != null) {
								eventBus.externalInterfacesChanged(applianceInstanceId);
							}
						}
					}

					@Override
					public void onError(CloudFacadeError error) {
						view.setUpdateExternalInterfaceBusyState(false);
						view.displayGeneralExternalInterfaceUpdateErrorMessage();
					}});
			}
		}
	}
	
	private void clearExternalInterfaceForm() {
		view.setUpdateExternalInterfaceBusyState(false);
		view.getExternalInterfaceName().setText("");
		view.getExternalInterfacePort().setText("");
		view.getTransportProtocol().setValue("tcp");
		view.getApplicationProtocol().setValue("none");
		view.setApplicationProtocolEnabled(true);
		view.getProxySendTimeout().setValue("");
		view.getProxyReadTimeout().setValue("");
		view.setMappingEditLabel(false);
	}

	private boolean isValidNumber(String numberValue) {
		if (numberValue != null && !numberValue.isEmpty()) {
			try {
				Integer.parseInt(numberValue);
				
				return true;
			} catch (NumberFormatException e) {
				//ignoring
			}
		}
			
		return false;
	}

	private void updatePortMapping(String name, int portNumber, String transportProtocol, String applicationProtocol, final PortMappingTemplateCallback portMappingTemplateCallback) {
		if (applianceInstanceId != null) {
			if(editMappingMode) {
				cloudFacadeController.updatePortMappingTemplateForDevelopmentModePropertySet(editMappingId, name, portNumber, transportProtocol, applicationProtocol, developmentModePropertySetId, new PortMappingTemplateCallback() {
					@Override
					public void processPortMappingTemplate(PortMappingTemplate portMappingTemplate) {
						if (portMappingTemplateCallback != null) {
							portMappingTemplateCallback.processPortMappingTemplate(portMappingTemplate);
						}
					}

					@Override
					public void onError(CloudFacadeError error) {
						portMappingTemplateCallback.onError(error);
					}
				});
			} else {
				cloudFacadeController.addPortMappingTemplateForDevelopmentModePropertySet(name, portNumber, transportProtocol, applicationProtocol, developmentModePropertySetId, new PortMappingTemplateCallback() {
					@Override
					public void processPortMappingTemplate(PortMappingTemplate portMappingTemplate) {
						if (portMappingTemplateCallback != null) {
							portMappingTemplateCallback.processPortMappingTemplate(portMappingTemplate);
						}
					}

					@Override
					public void onError(CloudFacadeError error) {
						portMappingTemplateCallback.onError(error);
					}
				});
			}
		} else if (applianceTypeId != null) {
			if(editMappingMode) {
				cloudFacadeController.updatePortMappingTemplateForApplianceType(editMappingId, name, portNumber, transportProtocol, applicationProtocol, applianceTypeId, new PortMappingTemplateCallback() {
					@Override
					public void processPortMappingTemplate(PortMappingTemplate portMappingTemplate) {
						if (portMappingTemplateCallback != null) {
							portMappingTemplateCallback.processPortMappingTemplate(portMappingTemplate);
						}
					}

					@Override
					public void onError(CloudFacadeError error) {
						portMappingTemplateCallback.onError(error);
					}
				});
			} else {
				cloudFacadeController.addPortMappingTemplateForApplianceType(name, portNumber, transportProtocol, applicationProtocol, applianceTypeId, new PortMappingTemplateCallback() {
					@Override
					public void processPortMappingTemplate(PortMappingTemplate portMappingTemplate) {
						if (portMappingTemplateCallback != null) {
							portMappingTemplateCallback.processPortMappingTemplate(portMappingTemplate);
						}
					}

					@Override
					public void onError(CloudFacadeError error) {
						portMappingTemplateCallback.onError(error);
					}
				});
			}
		}
	}

	@Override
	public void onUpdateEndpoint() {
		String name = view.getEndpointName().getText().trim();
		String invocationPath = view.getInvocationPath().getText().trim();
		String endpointType = view.getEndpointType().getValue();
		final String portMappingTemplateId = view.getTargetPort().getValue();
		String description = view.getEndpointDescription().getText().trim();
		String descriptor = view.getEndpointDescriptor().getText().trim();
		boolean secured = view.getSecured().getValue();
		
		if (name.isEmpty() || invocationPath.isEmpty() || portMappingTemplateId.isEmpty()) {
			view.displayEndpointNameInvocationPathOrPortMappingIdEmptyMessage();
		} else {
			view.clearErrorMessages();
			
			if(editEndpointMode) {
				cloudFacadeController.updateEndpoint(editEndpointId, name, invocationPath, endpointType,
						portMappingTemplateId, description, descriptor, secured, new EndpointCallback() {
							@Override
							public void processEndpoint(Endpoint endpoint) {
								clearEndpointForm();
								loadExternalInterfacesAndEndpoints();
								
								if (applianceInstanceId != null) {
									eventBus.endpointsChanged(applianceInstanceId);
								}
							}});
			} else {
				cloudFacadeController.addEndpoint(name, invocationPath, endpointType, portMappingTemplateId, description, descriptor, secured, new EndpointCallback() {
					@Override
					public void processEndpoint(final Endpoint endpoint) {
						view.showNoEndpointsLabel(false);
						cloudFacadeController.getHttpMappingsForPortMappingTemplateIdOrInstanceId(portMappingTemplateId, null, new HttpMappingsCallback() {
							@Override
							public void processHttpMappings(List<HttpMapping> httpMappings) {
								clearEndpointForm();
								
								String httpUrl = null;
								String httpsUrl = null;
								
								for (HttpMapping httpMapping : httpMappings) {
									if (httpMapping.getApplicationProtocol().equals("http")) {
										httpUrl = addUrlPath(httpMapping.getUrl(), endpoint.getInvocationPath());
									} else if (httpMapping.getApplicationProtocol().equals("https")) {
										httpsUrl = addUrlPath(httpMapping.getUrl(), endpoint.getInvocationPath());
									}
								}
								
								IsWidget widget = view.addEndpoint(getEndpointPosition(endpoint.getName()), endpoint.getId(), endpoint.getName(), endpoint.isSecured(), httpUrl, httpsUrl);
								endpoints.put(endpoint, widget);
								
								if (applianceInstanceId != null) {
									eventBus.endpointsChanged(applianceInstanceId);
								}
							}});
					}});
			}
		}
	}
	
	private void clearEndpointForm() {
		view.getEndpointName().setText("");
		view.getInvocationPath().setText("");
		view.getEndpointType().setValue("webapp");
		view.selectFirstTargetPort();
		view.getEndpointDescription().setText("");
		view.getEndpointDescriptor().setText("");
		view.getSecured().setValue(false);
		view.setEndpointEditLabel(false);
	}

	@Override
	public void onRemoveEndpoint(final String endpointId) {
		if (view.confirmEndpointRemoval()) {
			if(editEndpointMode) {
				editEndpointMode = false;
				clearEndpointForm();
			}
			
			cloudFacadeController.removeEndpoint(endpointId, new Command() {
				@Override
				public void execute() {
					Endpoint endpoint = null;
					
					for (Endpoint e : endpoints.keySet()) {
						if (e.getId().equals(endpointId)) {
							endpoint = e;
							
							break;
						}
					}
					
					view.removeEndpoint(endpoints.remove(endpoint));
					
					if (endpoints.size() == 0) {
						view.showNoEndpointsLabel(true);
					}
					
					if (applianceInstanceId != null) {
						eventBus.externalInterfacesChanged(applianceInstanceId);
					}
				}
			});
		}
	}
	
	private String addUrlPath(String url, String path) {
		StringBuilder builder = new StringBuilder();
		builder.append(url);
		
		if (!url.endsWith("/") && !path.startsWith("/")) {
			builder.append("/");
		} else if (url.endsWith("/") && path.startsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		
		builder.append(path);
		
		return builder.toString();
	}

	@Override
	public void onEditMapping(String mappingId) {
		if(editMappingMode) {
			if(editMappingId.equals(mappingId)) {
				//disable editing
				editMappingMode = false;
				clearExternalInterfaceForm();
				editMappingId = null;
			} else {
				//another edit action
				view.removeMappingEditState(editMappingId);
				editMappingId = mappingId;
			}
		} else {
			editMappingMode = true;
			editMappingId = mappingId;
		}
		
		if(editMappingMode) {
			fillInExternalInterfaceForm(mappingId);
		}
	}

	private void fillInExternalInterfaceForm(String mappingId) {
		PortMappingTemplate mapping = null;
		
		for(PortMappingTemplate mappingTemplate : mappings.keySet()) {
			if(mappingTemplate.getId().equals(mappingId)) {
				mapping = mappingTemplate;
				
				break;
			}
		}
		
		if(mapping != null) {
			view.getExternalInterfaceName().setText(mapping.getServiceName());
			view.getExternalInterfacePort().setText(String.valueOf(mapping.getTargetPort()));
			view.getTransportProtocol().setValue(mapping.getTransportProtocol());
			view.getApplicationProtocol().setValue(mapping.getApplicationProtocol());
			
			if(mappingProperties.get(mappingId) != null) {
				if(mappingProperties.get(mappingId).get(PROXY_SEND_TIMEOUT) != null) {
					view.getProxySendTimeout().setValue(mappingProperties.get(mappingId).get(PROXY_SEND_TIMEOUT).getValue());
				}
				
				if(mappingProperties.get(mappingId).get(PROXY_READ_TIMEOUT) != null) {
					view.getProxyReadTimeout().setValue(mappingProperties.get(mappingId).get(PROXY_READ_TIMEOUT).getValue());
				}
			}
			
			view.setMappingEditLabel(true);
		}
	}

	@Override
	public void onEditEndpoint(String endpointId) {
		if(editEndpointMode) {
			if(editEndpointId.equals(endpointId)) {
				//disable editing
				editEndpointMode = false;
				clearEndpointForm();
				editEndpointId = null;
			} else {
				//another edit action
				view.removeEndpointEditState(editEndpointId);
				editEndpointId = endpointId;
			}
		} else {
			editEndpointMode = true;
			editEndpointId = endpointId;
		}
		
		if(editEndpointMode) {
			fillInEndpointForm(endpointId);
		}
	}

	private void fillInEndpointForm(String endpointId) {
		Endpoint endpoint = null;
		
		for(Endpoint e : endpoints.keySet()) {
			if(e.getId().equals(endpointId)) {
				endpoint = e;
				
				break;
			}
		}
		
		if(endpoint != null) {
			view.getEndpointName().setText(endpoint.getName());
			view.getEndpointType().setValue(endpoint.getEndpointType());
			view.getInvocationPath().setText(endpoint.getInvocationPath());
			view.getTargetPort().setValue(endpoint.getPortMappingTemplateId());
			view.getEndpointDescription().setText(endpoint.getDescription());
			view.getEndpointDescriptor().setText(endpoint.getDescriptor());
			view.getSecured().setValue(endpoint.isSecured());
			view.setEndpointEditLabel(true);
		}
	}
}
