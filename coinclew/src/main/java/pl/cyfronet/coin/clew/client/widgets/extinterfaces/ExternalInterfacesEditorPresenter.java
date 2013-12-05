package pl.cyfronet.coin.clew.client.widgets.extinterfaces;

import java.util.Arrays;
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
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.PortMappingTemplatesCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.PortMappingsCallback;
import pl.cyfronet.coin.clew.client.controller.cf.devmodepropertyset.DevelopmentModePropertySet;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.Endpoint;
import pl.cyfronet.coin.clew.client.controller.cf.httpmapping.HttpMapping;
import pl.cyfronet.coin.clew.client.controller.cf.portmapping.PortMapping;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate.PortMappingTemplate;
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
	private Map<String, IsWidget> mappings;
	private String developmentModePropertySetId;
	private Map<String, IsWidget> endpoints;
	private boolean endpointsExist;

	@Inject
	public ExternalInterfacesEditorPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
		mappings = new HashMap<String, IsWidget>();
		endpoints = new HashMap<String, IsWidget>();
	}
	
	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onShowExternalInterfacesEditor(String applianceInstanceId) {
		this.applianceInstanceId = applianceInstanceId;
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
		cloudFacadeController.getDevelopmentModePropertySet(applianceInstanceId, new DevelopmentModePropertySetCallback() {
			@Override
			public void processDeveopmentModePropertySet(DevelopmentModePropertySet developmentModePropertySet) {
				ExternalInterfacesEditorPresenter.this.developmentModePropertySetId = developmentModePropertySet.getId();
				
				if (developmentModePropertySet.getPortMappingTemplateIds().size() == 0) {
					view.showExternalInterfacesLoadingIndicator(false);
					view.showNoExternalInterfacesLabel(true);
					view.showEndpointsLoadingIndicator(false);
					view.showNoEndpointsLabel(true);
				} else {
					cloudFacadeController.getPortMappingTemplatesForDevelopmentModePropertySetId(developmentModePropertySet.getId(), new PortMappingTemplatesCallback() {
						@Override
						public void processPortMappingTemplates(List<PortMappingTemplate> portMappingTemplates) {
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
											String httpUrl = null;
											String httpsUrl = null;
											
											for (HttpMapping httpMapping : httpMappings) {
												if (httpMapping.getApplicationProtocol().equals("http")) {
													httpUrl = httpMapping.getUrl();
												} else if (httpMapping.getApplicationProtocol().equals("https")) {
													httpsUrl = httpMapping.getUrl();
												}
											}
											
											IsWidget widget = view.addMapping(portMappingTemplate.getId(), portMappingTemplate.getServiceName(),
													portMappingTemplate.getTargetPort(), portMappingTemplate.getTransportProtocol(),
													httpUrl, httpsUrl, null, null);
											mappings.put(portMappingTemplate.getId(), widget);
											
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
															
															IsWidget widget = view.addEndpoint(endpoint.getId(), endpoint.getName(), httpUrl, httpsUrl);
															ExternalInterfacesEditorPresenter.this.endpoints.put(endpoint.getId(), widget);
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
											//there should be only one port mapping
											if (portMappings.size() > 0) {
												PortMapping portMapping = portMappings.get(0);
												IsWidget widget = view.addMapping(portMappingTemplate.getId(), portMappingTemplate.getServiceName(), portMappingTemplate.getTargetPort(),
														portMappingTemplate.getTransportProtocol(), null, null,
														portMapping.getPublicIp(), portMapping.getSourcePort());
												mappings.put(portMappingTemplate.getId(), widget);
											}
										}});
								}
							}
						}});
					}
			}});
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
								view.removeMappingTemplate(mappings.remove(mappingId));
								
								if (mappings.size() == 0) {
									view.showNoExternalInterfacesLabel(true);
								}
								
								eventBus.externalInterfacesChanged(applianceInstanceId);
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
		
		if (name.isEmpty() || port.isEmpty()) {
			view.displayNameOrPortEmptyMessage();
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
				cloudFacadeController.addPortMappingTemplate(name, portNumber, transportProtocol, applicationProtocol, developmentModePropertySetId, new PortMappingTemplateCallback() {
					@Override
					public void processPortMappingTemplate(PortMappingTemplate portMappingTemplate) {
						view.setAddExternalInterfaceBusyState(false);
						view.getExternalInterfaceName().setText("");
						view.getExternalInterfacePort().setText("");
						view.getTransportProtocol().setValue("tcp");
						view.getApplicationProtocol().setValue("none");
						view.setApplicationProtocolEnabled(true);
						loadExternalInterfacesAndEndpoints();
						eventBus.externalInterfacesChanged(applianceInstanceId);
					}
				});
			}
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
							String httpUrl = null;
							String httpsUrl = null;
							
							for (HttpMapping httpMapping : httpMappings) {
								if (httpMapping.getApplicationProtocol().equals("http")) {
									httpUrl = addUrlPath(httpMapping.getUrl(), endpoint.getInvocationPath());
								} else if (httpMapping.getApplicationProtocol().equals("https")) {
									httpsUrl = addUrlPath(httpMapping.getUrl(), endpoint.getInvocationPath());
								}
							}
							
							IsWidget widget = view.addEndpoint(endpoint.getId(), endpoint.getName(), httpUrl, httpsUrl);
							endpoints.put(endpoint.getId(), widget);
							eventBus.endpointsChanged(applianceInstanceId);
						}
					});
				}});
		}
	}

	@Override
	public void onRemoveEndpoint(final String endpointId) {
		if (view.confirmEndpointRemoval()) {
			cloudFacadeController.removeEndpoint(endpointId, new Command() {
				@Override
				public void execute() {
					view.removeEndpoint(endpoints.remove(endpointId));
					
					if (endpoints.size() == 0) {
						view.showNoEndpointsLabel(true);
					}
					
					eventBus.externalInterfacesChanged(applianceInstanceId);
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