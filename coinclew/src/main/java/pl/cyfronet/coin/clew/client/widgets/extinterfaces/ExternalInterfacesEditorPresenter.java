package pl.cyfronet.coin.clew.client.widgets.extinterfaces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.DevelopmentModePropertySetCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.EndpointCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.EndpointsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.HttpMappingsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.PortMappingTemplateCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.PortMappingTemplatePropertiesCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.PortMappingTemplatesCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.PortMappingsCallback;
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
	private CloudFacadeController cloudFacadeController;
	private String applianceInstanceId;
	private Map<PortMappingTemplate, IsWidget> mappings;
	private String developmentModePropertySetId;
	private Map<Endpoint, IsWidget> endpoints;
	private boolean endpointsExist;
	private String applianceTypeId;

	@Inject
	public ExternalInterfacesEditorPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
		mappings = new HashMap<PortMappingTemplate, IsWidget>();
		endpoints = new HashMap<Endpoint, IsWidget>();
	}
	
	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onShowExternalInterfacesEditor(String applianceInstanceId) {
		this.applianceInstanceId = applianceInstanceId;
		applianceTypeId = null;
		developmentModePropertySetId = null;
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
							view.addHttpMappingEndpointOption(portMappingTemplate.getId(), portMappingTemplate.getServiceName(), portMappingTemplate.getTargetPort());
							view.showEndpointTargetPortHelpBlock(false);
							cloudFacadeController.getHttpMappingsForPortMappingTemplateId(portMappingTemplate.getId(), new HttpMappingsCallback() {
								@Override
								public void processHttpMappings(final List<HttpMapping> httpMappings) {
									String httpUrl = Arrays.asList(new String[] {"http", "http_https"}).contains(portMappingTemplate.getApplicationProtocol()) ? "" : null;
									String httpsUrl = Arrays.asList(new String[] {"https", "http_https"}).contains(portMappingTemplate.getApplicationProtocol()) ? "" : null;;
									
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
											
											for (PortMappingTemplateProperty property : portMappingTemplateProperties) {
												properties.put(property.getKey(), property.getValue());
											}
											
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
													
													IsWidget widget = view.addEndpoint(getEndpointPosition(endpoint.getName()), endpoint.getId(), endpoint.getName(),
															joinUrl(httpUrl, endpoint.getInvocationPath()),
															joinUrl(httpsUrl, endpoint.getInvocationPath()));
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
							
							cloudFacadeController.getPortMappingsForPortMappingTemplateId(portMappingTemplate.getId(), new PortMappingsCallback() {
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

	private String joinUrl(String url, String path) {
		if (url == null) {
			return null;
		}
		
		if (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		
		if (path != null && path.startsWith("/")) {
			path = path.substring(1, path.length());
		}
		
		return url + "/" + path;
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
	public void onAddExternalInterface() {
		String name = view.getExternalInterfaceName().getText().trim();
		String port = view.getExternalInterfacePort().getText().trim();
		String applicationProtocol = view.getApplicationProtocol().getValue();
		String transportProtocol = view.getTransportProtocol().getValue();
		final String proxySendTimeout = view.getProxySendTimeout().getValue().trim();
		final String proxyReadTimeout = view.getProxyReadTimeout().getValue().trim();
		
		if (name.isEmpty() || port.isEmpty()) {
			view.displayNameOrPortEmptyMessage();
		} else if (!proxySendTimeout.isEmpty() && (!isValidNumber(proxySendTimeout) || applicationProtocol.equals("none"))) {
			view.displayWorngProxySendTimeoutMessage();
		} else if (!proxyReadTimeout.isEmpty() && (!isValidNumber(proxyReadTimeout) || applicationProtocol.equals("none"))) {
			view.displayWorngProxyReadTimeoutMessage();
		} else {
			int portNumber = -1;
			
			try {
				portNumber = Integer.valueOf(port);
			} catch (NumberFormatException e) {
				view.displayWrongPortFormatMessages();
			}
			
			if (portNumber > -1) {
				view.setAddExternalInterfaceBusyState(true);
				view.clearErrorMessages();
				addPortMapping(name, portNumber, transportProtocol, applicationProtocol, new PortMappingTemplateCallback() {
					@Override
					public void processPortMappingTemplate(PortMappingTemplate portMappingTemplate) {
						clearExternalInterfaceForm();
						
						if (!proxySendTimeout.isEmpty()) {
							cloudFacadeController.addPortMappingProperty(portMappingTemplate.getId(), "proxy_sent_timeout", proxySendTimeout, new Command() {
								@Override
								public void execute() {
									if (proxyReadTimeout.isEmpty()) {
										loadExternalInterfacesAndEndpoints();
										
										if (applianceInstanceId != null) {
											eventBus.externalInterfacesChanged(applianceInstanceId);
										}
									}
								}
							});
						}
						
						if (!proxyReadTimeout.isEmpty()) {
							cloudFacadeController.addPortMappingProperty(portMappingTemplate.getId(), "proxy_read_timeout", proxyReadTimeout, new Command() {
								@Override
								public void execute() {
									loadExternalInterfacesAndEndpoints();
									
									if (applianceInstanceId != null) {
										eventBus.externalInterfacesChanged(applianceInstanceId);
									}
								}
							});
						}
						
						if (proxySendTimeout.isEmpty() && proxyReadTimeout.isEmpty()) {
							loadExternalInterfacesAndEndpoints();
							
							if (applianceInstanceId != null) {
								eventBus.externalInterfacesChanged(applianceInstanceId);
							}
						}
					}});
			}
		}
	}
	
	private void clearExternalInterfaceForm() {
		view.setAddExternalInterfaceBusyState(false);
		view.getExternalInterfaceName().setText("");
		view.getExternalInterfacePort().setText("");
		view.getTransportProtocol().setValue("tcp");
		view.getApplicationProtocol().setValue("none");
		view.setApplicationProtocolEnabled(true);
		view.getProxySendTimeout().setValue("");
		view.getProxyReadTimeout().setValue("");
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

	private void addPortMapping(String name, int portNumber, String transportProtocol, String applicationProtocol, final PortMappingTemplateCallback portMappingTemplateCallback) {
		if (applianceInstanceId != null) {
			cloudFacadeController.addPortMappingTemplateForDevelopmentModePropertySet(name, portNumber, transportProtocol, applicationProtocol, developmentModePropertySetId, new PortMappingTemplateCallback() {
				@Override
				public void processPortMappingTemplate(PortMappingTemplate portMappingTemplate) {
					if (portMappingTemplateCallback != null) {
						portMappingTemplateCallback.processPortMappingTemplate(portMappingTemplate);
					}
				}
			});
		} else if (applianceTypeId != null) {
			cloudFacadeController.addPortMappingTemplateForApplianceType(name, portNumber, transportProtocol, applicationProtocol, applianceTypeId, new PortMappingTemplateCallback() {
				@Override
				public void processPortMappingTemplate(PortMappingTemplate portMappingTemplate) {
					if (portMappingTemplateCallback != null) {
						portMappingTemplateCallback.processPortMappingTemplate(portMappingTemplate);
					}
				}
			});
		}
	}

	@Override
	public void onAddEndpoint() {
		String name = view.getEndpointName().getText().trim();
		String invocationPath = view.getInvocationPath().getText().trim();
		String endpointType = view.getEndpointType().getValue();
		final String portMappingTemplateId = view.getTargetPort().getValue();
		String description = view.getEndpointDescription().getText().trim();
		String descriptor = view.getEndpointDescriptor().getText().trim();
		
		if (name.isEmpty() || invocationPath.isEmpty() || portMappingTemplateId.isEmpty()) {
			view.displayEndpointNameInvocationPathOrPortMappingIdEmptyMessage();
		} else {
			cloudFacadeController.addEndpoint(name, invocationPath, endpointType, portMappingTemplateId, description, descriptor, new EndpointCallback() {
				@Override
				public void processEndpoint(final Endpoint endpoint) {
					view.showNoEndpointsLabel(false);
					cloudFacadeController.getHttpMappingsForPortMappingTemplateId(portMappingTemplateId, new HttpMappingsCallback() {
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
							
							IsWidget widget = view.addEndpoint(getEndpointPosition(endpoint.getName()), endpoint.getId(), endpoint.getName(), httpUrl, httpsUrl);
							endpoints.put(endpoint, widget);
							
							if (applianceInstanceId != null) {
								eventBus.endpointsChanged(applianceInstanceId);
							}
						}});
				}});
		}
	}
	
	private void clearEndpointForm() {
		view.getEndpointName().setText("");
		view.getInvocationPath().setText("");
		view.getEndpointType().setValue("webapp");
		view.selectFirstTargetPort();
		view.getEndpointDescription().setText("");
		view.getEndpointDescriptor().setText("");
	}

	@Override
	public void onRemoveEndpoint(final String endpointId) {
		if (view.confirmEndpointRemoval()) {
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
}