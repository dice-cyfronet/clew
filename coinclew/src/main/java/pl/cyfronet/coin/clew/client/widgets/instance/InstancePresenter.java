package pl.cyfronet.coin.clew.client.widgets.instance;

import static java.util.Arrays.asList;
import static pl.cyfronet.coin.clew.client.UrlHelper.joinUrl;
import static pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance.STATE_NEW;
import static pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance.STATE_SATISFIED;
import static pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance.STATE_UNSATISFIED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.UrlHelper;
import pl.cyfronet.coin.clew.client.auth.MiTicketReader;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ActionCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceInstanceCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypeCallback;
import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeError;
import pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliance.AggregateAppliance;
import pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliance.AggregatePortMappingTemplate;
import pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliance.AggregateVm;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.Endpoint;
import pl.cyfronet.coin.clew.client.controller.cf.httpmapping.HttpMapping;
import pl.cyfronet.coin.clew.client.controller.cf.portmapping.PortMapping;
import pl.cyfronet.coin.clew.client.controller.overlay.Redirection;
import pl.cyfronet.coin.clew.client.widgets.httpmapping.HttpMappingPresenter;
import pl.cyfronet.coin.clew.client.widgets.instance.IInstanceView.IInstancePresenter;

@Presenter(view = InstanceView.class, multiple = true)
public class InstancePresenter extends BasePresenter<IInstanceView, MainEventBus> implements IInstancePresenter {
	private final static Logger log = LoggerFactory.getLogger(InstancePresenter.class); 
	
	private Map<String, HttpMappingPresenter> webapps;
	
	private Map<String, HttpMappingPresenter> services;
	
	private Map<String, IsWidget> otherServices;
	
	private boolean developmentMode;
	
	private AggregateAppliance applianceInstance;
	
	private MiTicketReader ticketReader;
	
	private CloudFacadeController cloudFacadeController;
	
	private boolean globalSaveInPlaceEnabled;

	private boolean suspendToggleActive;
	
	@Inject
	public InstancePresenter(CloudFacadeController cloudFacadeController, MiTicketReader ticketReader) {
		this.cloudFacadeController = cloudFacadeController;
		this.ticketReader = ticketReader;
		webapps = new HashMap<>();
		services = new HashMap<>();
		otherServices = new HashMap<>();
	}
	
	public void setInstance(AggregateAppliance applianceInstance, final boolean enableShutdown, final boolean developmentMode) {
		this.developmentMode = developmentMode;
		
		if(this.applianceInstance != null) {
			this.applianceInstance = applianceInstance;
			updateView(applianceInstance);
			
			return;
		}

		this.applianceInstance = applianceInstance;
		view.getName().setText(applianceInstance.getName());

		if(STATE_UNSATISFIED.equals(applianceInstance.getState())) {
			view.setUnsatisfiedState(applianceInstance.getStateExplanation());
		}
		
		view.getCost().setText(costIndicator(applianceInstance));
		
		if(!applianceInstance.getDescription().isEmpty()) {
			view.getDescription().setText(applianceInstance.getDescription());
		} else {
			view.setNoDescription();
		}
		
		if(developmentMode) {
			view.addPauseControl();
			view.addRebootControl();
		}
		
		if(enableShutdown) {
			view.addShutdownControl();
		}
		
		if(STATE_SATISFIED.equals(applianceInstance.getState())) {
			displayDetails();
		}
		
		if(developmentMode) {
			view.addExternalInterfacesControl();
			view.addSaveControl();
			view.addSaveInPlaceControl();
			
			if(STATE_UNSATISFIED.equals(applianceInstance.getState())) {
				view.enableExternalInterfaces(false);
				view.enableSave(false);
				view.enableSaveInPlace(false);
				view.enablePause(false);
			}
		}
		
		if(Arrays.asList(STATE_SATISFIED, STATE_NEW).contains(applianceInstance.getState())) {
			if(applianceInstance.getVirtualMachines() != null && applianceInstance.getVirtualMachines().size() > 0) {
				view.showDetailsPanel(true);
				view.showNoVmsLabel(false);
				
				AggregateVm applianceVm = applianceInstance.getVirtualMachines().get(0);
				view.getIp().setHTML(applianceVm.getIp() != null ? applianceVm.getIp() : "&nbsp;");
				updateStatus(applianceVm);
				view.getLocation().setText(applianceVm.getComputeSite().getName());
			} else {
				view.showDetailsPanel(false);
				view.showNoVmsLabel(true);
				view.getIp().setHTML("&nbsp;");
				view.getLocation().setHTML("&nbsp;");
				
				if(STATE_NEW.equals(applianceInstance.getState())) {
					view.setStatus(view.getInitializingLabel());
					view.enableCollapsable(false);
				} else {
					view.setStatus(view.getNoVmsLabel());
					view.enableCollapsable(true);
				}
			}
		}
	}
	
	public AggregateAppliance getInstance() {
		return applianceInstance;
	}
	
	@Override
	public void onShutdownClicked() {
		if (view.confirmInstanceShutdown()) {
			view.setShutdownBusyState(true);
			cloudFacadeController.shutdownApplianceInstance(applianceInstance.getId(), new Command() {
				@Override
				public void execute() {
					view.setShutdownBusyState(false);
					eventBus.removeInstance(applianceInstance.getId());
				}
			});
		}
	}

	@Override
	public void onExternalInterfacesClicked() {
		eventBus.showExternalInterfacesEditor(applianceInstance.getId());
	}

	@Override
	public void onSave() {
		eventBus.showAtomicServiceEditor(applianceInstance.getId(), true);
	}

	public void updateAccessInfo() {
		displayDetails();
	}

	public void updateEndpoints() {
		displayDetails();
	}

	@Override
	public void onReboot() {
		if(view.confirmInstanceReboot()) {
			view.setRebootBusyState(true);
			cloudFacadeController.rebootApplianceInstance(applianceInstance.getId(), new Command() {
				@Override
				public void execute() {
					view.setRebootBusyState(false);
				}
			});
		}
	}

	@Override
	public void onSaveInPlace() {
		if(view.saveInPlaceConfirmation()) {
			saveInPlace();
		}
	}

	public void updateGlobalSaveInPlace(List<String> applianceTypeIds) {
		globalSaveInPlaceEnabled = applianceTypeIds.contains(applianceInstance.getApplianceTypeId());
	}

	@Override
	public void onSuspend() {
		final boolean suspended = applianceInstance.getVirtualMachines().get(0).getState().equals("suspended");
		String action = suspended ? "start" : "suspend";
		view.setSuspendBusyState(true);
		suspendToggleActive = true;
		cloudFacadeController.toggleSuspend(action, applianceInstance.getId(), new ActionCallback() {
			@Override
			public void onError(CloudFacadeError error) {
				suspendToggleActive = false;
				view.setSuspendBusyState(false);
			}
			
			@Override
			public void onActionPerformed() {
				suspendToggleActive = false;
				view.setSuspendBusyState(false);
				view.switchPauseButton(!suspended);
			}
		});
	}

	private String formatDate(String dateValue) {
		try {
			DateTimeFormat format = DateTimeFormat.getFormat(PredefinedFormat.ISO_8601);
			Date date = format.parse(dateValue);
			
			return DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT).format(date);
		} catch(Exception e) {
			log.error("Could not parse date {}", dateValue);
			
			return "";
		}
	}

	private void updateStatus(final AggregateVm applianceVm) {
		view.setStatus(applianceVm.getState() != null ? applianceVm.getState() : "&nbsp;");
		
		if(applianceVm.getState() != null) {
			if(asList("active", "paused").contains(applianceVm.getState())) {
				view.enableSave(true);
				view.enableSaveInPlace(true && globalSaveInPlaceEnabled);
				
				if(!suspendToggleActive) {
					view.enablePause(true);
					view.switchPauseButton(applianceVm.getState().equals("paused"));
				}
				
				view.enableExternalInterfaces(true);
				view.enableCollapsable(true);
				
				if(developmentMode) {
					view.enableReboot(true);
				}
			} else if(asList("saving", "reboot").contains(applianceVm.getState())) {
				view.enableSave(false);
				view.enableSaveInPlace(false);
				
				if(!suspendToggleActive) {
					view.enablePause(false);
				}
				
				view.enableExternalInterfaces(false);
				view.collapseDetails();
				view.enableCollapsable(false);
				
				if(developmentMode) {
					view.enableReboot(false);
				}
			}
			
			if(applianceVm.getFlavor() != null) {
				view.setFlavorDetails(formatDate(applianceInstance.getPrepaidUntil()),
						applianceVm.getFlavor().getName(), applianceVm.getFlavor().getCpu(),
						applianceVm.getFlavor().getMemory(), applianceVm.getFlavor().getHdd());
			} else {
				log.error("Flavors for virtual machine with id {} are missing", applianceVm.getId());
			}
		}
	}

	private String costIndicator(final AggregateAppliance applianceInstance) {
		return "$" + NumberFormat.getFormat("0.00").format(((float) applianceInstance.getAmountBilled() / 10000));
	}

	private void displayDetails() {
		displayRedirections();
	}

	private void displayRedirections() {
		List<String> currentWebapps = new ArrayList<String>();
		List<String> currentServices = new ArrayList<String>();
		List<String> currentOtherServices = new ArrayList<String>();
		List<Redirection> redirections = transform();
		
		for(Redirection redirection : redirections) {
			if(redirection.isHttp()) {
				if(redirection.getEndpoints() != null) {
					for(Endpoint endpoint : redirection.getEndpoints()) {
						if("webapp".equals(endpoint.getEndpointType())) {
							currentWebapps.add(endpoint.getId());
							
							//before showing a webapp at least one http mapping has to be available
							if(redirection.getHttpUrl() != null || redirection.getHttpsUrl() != null) {
								if(!webapps.keySet().contains(endpoint.getId())) {
									String endpointHttpUrl = joinUrl(redirection.getHttpUrl(), endpoint.getInvocationPath(),
											endpoint.isSecured() ? ticketReader.getUserLogin() : null, endpoint.isSecured() ? ticketReader.getTicket() : null);
									String endpointHttpsUrl = joinUrl(redirection.getHttpsUrl(), endpoint.getInvocationPath(),
											endpoint.isSecured() ? ticketReader.getUserLogin() : null, endpoint.isSecured() ? ticketReader.getTicket() : null);
									String customHttpUrl = null;
									String customHttpsUrl = null;
									String invovationPath = endpoint.getInvocationPath();
									
									if(redirection.getCustomHttpAlias() != null && redirection.getCustomHttpUrl() != null) {
										customHttpUrl = joinUrl(redirection.getCustomHttpUrl(), endpoint.getInvocationPath(),
												endpoint.isSecured() ? ticketReader.getUserLogin() : null,
														endpoint.isSecured() ? ticketReader.getTicket() : null);
									}
									
									if(redirection.getCustomHttpsAlias() != null && redirection.getCustomHttpsUrl() != null) {
										customHttpsUrl = joinUrl(redirection.getCustomHttpsUrl(), endpoint.getInvocationPath(),
												endpoint.isSecured() ? ticketReader.getUserLogin() : null,
														endpoint.isSecured() ? ticketReader.getTicket() : null);
									}
									
									//nx url params fix
									if(redirection.getName().equals("nx")) {
										Redirection sshRedirection = findSshRedirection(redirections);
										
										if(sshRedirection != null && sshRedirection.getPortMappings().size() > 0) {
											PortMapping portMapping = sshRedirection.getPortMappings().get(0);
											invovationPath += "?nxport=" + portMapping.getSourcePort() + "&nxhost=" + portMapping.getPublicIp();
											
											if(endpointHttpUrl != null) {
												endpointHttpUrl += "?nxport=" + portMapping.getSourcePort() + "&nxhost=" + portMapping.getPublicIp();
											}
											
											if(customHttpUrl != null) {
												customHttpUrl += "?nxport=" + portMapping.getSourcePort() + "&nxhost=" + portMapping.getPublicIp();
											}
											
											if(endpointHttpsUrl != null) {
												endpointHttpsUrl += "?nxport=" + portMapping.getSourcePort() + "&nxhost=" + portMapping.getPublicIp();
											}
											
											if(customHttpsUrl != null) {
												customHttpsUrl += "?nxport=" + portMapping.getSourcePort() + "&nxhost=" + portMapping.getPublicIp();
											}
										}
									}
									
									HttpMappingPresenter presenter = eventBus.addHandler(HttpMappingPresenter.class);
									view.addWebApplication(presenter.getView());
									presenter.setHttpMapping(endpoint.getName(), endpointHttpUrl, endpointHttpsUrl, redirection.getHttpUrlStatus(),
											redirection.getHttpsUrlStatus(), redirection.getCustomHttpAlias(), customHttpUrl,
											redirection.getCustomHttpsAlias(), customHttpsUrl, redirection.getHttpMappingId(),
											redirection.getHttpsMappingId(), invovationPath, null);
									webapps.put(endpoint.getId(), presenter);
								} else {
									HttpMappingPresenter presenter = webapps.get(endpoint.getId());
									
									if(presenter != null) {
										if(redirection.getHttpUrl() != null) {
											presenter.updateHttpStatus(redirection.getHttpUrlStatus());
										}
										
										if(redirection.getHttpsUrl() != null) {
											presenter.updateHttpsStatus(redirection.getHttpsUrlStatus());
										}
									}
								}
							}
						} else {
							currentServices.add(endpoint.getId());
							
							//before showing a service at least one http mapping has to be available
							if(redirection.getHttpUrl() != null || redirection.getHttpsUrl() != null) {
								if(!services.keySet().contains(endpoint.getId())) {
									HttpMappingPresenter presenter = eventBus.addHandler(HttpMappingPresenter.class);
									view.addService(presenter.getView());
									presenter.setHttpMapping(endpoint.getName(), UrlHelper.joinUrl(redirection.getHttpUrl(), endpoint.getInvocationPath(),
													endpoint.isSecured() ? ticketReader.getUserLogin() : null,
															endpoint.isSecured() ? ticketReader.getTicket() : null),
											UrlHelper.joinUrl(redirection.getHttpsUrl(), endpoint.getInvocationPath(),
													endpoint.isSecured() ? ticketReader.getUserLogin() : null,
															endpoint.isSecured() ? ticketReader.getTicket() : null),
											redirection.getHttpUrlStatus(), redirection.getHttpsUrlStatus(), redirection.getCustomHttpAlias(),
											UrlHelper.joinUrl(redirection.getCustomHttpUrl(), endpoint.getInvocationPath(),
													endpoint.isSecured() ? ticketReader.getUserLogin() : null,
															endpoint.isSecured() ? ticketReader.getTicket() : null), redirection.getCustomHttpsAlias(),
											UrlHelper.joinUrl(redirection.getCustomHttpsUrl(), endpoint.getInvocationPath(),
													endpoint.isSecured() ? ticketReader.getUserLogin() : null,
															endpoint.isSecured() ? ticketReader.getTicket() : null),
											redirection.getHttpMappingId(), redirection.getHttpsMappingId(), endpoint.getInvocationPath(),
											endpoint.getDescriptor());
									services.put(endpoint.getId(), presenter);
								} else {
									HttpMappingPresenter presenter = services.get(endpoint.getId());
									
									if(presenter != null) {
										if(redirection.getHttpUrl() != null) {
											presenter.updateHttpStatus(redirection.getHttpUrlStatus());
										}
										
										if(redirection.getHttpsUrl() != null) {
											presenter.updateHttpsStatus(redirection.getHttpsUrlStatus());
										}
									}
								}
							}
						}
					}
				}
			} else {
				if(redirection.getPortMappings().size() > 0) {
					//TODO(DH): for now only the first port mapping is used
					PortMapping portMapping = redirection.getPortMappings().get(0);
					currentOtherServices.add(redirection.getId());
					
					if(!otherServices.keySet().contains(redirection.getId())) {
						String helpBlock = null;
						
						if(redirection.getName().toLowerCase().startsWith("ssh") && developmentMode) {
							helpBlock = view.getSshHelpBlock(portMapping.getPublicIp(),
									portMapping.getSourcePort());
						}
						
						IsWidget widget = view.addOtherService(redirection.getName(),
								portMapping.getPublicIp(), portMapping.getSourcePort(), helpBlock);
						otherServices.put(redirection.getId(), widget);
					}
				}
			}
		}
		
		for(String webappId : webapps.keySet()) {
			if(!currentWebapps.contains(webappId)) {
				HttpMappingPresenter presenter = webapps.remove(webappId);
				view.removeWebapp(presenter.getView());
				eventBus.removeHandler(presenter);
			}
		}
		
		for(String serviceId : services.keySet()) {
			if (!currentServices.contains(serviceId)) {
				HttpMappingPresenter presenter = services.remove(serviceId);
				view.removeService(presenter.getView());
				eventBus.removeHandler(presenter);
			}
		}
		
		for(String otherServiceId : otherServices.keySet()) {
			if (!currentOtherServices.contains(otherServiceId)) {
				view.removeOtherService(otherServices.remove(otherServiceId));
			}
		}
		
		if(webapps.size() == 0) {
			view.showNoWebApplicationsLabel(true);
		} else {
			view.showNoWebApplicationsLabel(false);
		}
		
		if(services.size() == 0) {
			view.showNoServicesLabel(true);
		} else {
			view.showNoServicesLabel(false);
		}
		
		if(otherServices.size() == 0) {
			view.showNoOtherServicesLabel(true);
		} else {
			view.showNoOtherServicesLabel(false);
		}
	}

	private List<Redirection> transform() {
		List<Redirection> result = new ArrayList<Redirection>();
		
		for(AggregatePortMappingTemplate portMappingTemplate : applianceInstance.getPortMappingTemplates()) {
			Redirection redirection = new Redirection();
			redirection.setId(portMappingTemplate.getId());
			redirection.setName(portMappingTemplate.getServiceName());
			redirection.setTargetPort(portMappingTemplate.getTargetPort());
			redirection.setProtocol(portMappingTemplate.getTransportProtocol());
			result.add(redirection);
			
			if(Arrays.asList((new String[] {"http", "https", "http_https"})).contains(
					portMappingTemplate.getApplicationProtocol())) {
				redirection.setHttp(true);
				//fetching http mappings ...
				for(HttpMapping httpMapping : portMappingTemplate.getHttpMappings()) {
					if("http".equals(httpMapping.getApplicationProtocol())) {
						redirection.setHttpUrl(httpMapping.getUrl());
						redirection.setHttpUrlStatus(httpMapping.getStatus());
						redirection.setCustomHttpAlias(httpMapping.getCustonName());
						redirection.setCustomHttpUrl(httpMapping.getCustromUrl());
						redirection.setHttpMappingId(httpMapping.getId());
					} else if ("https".equals(httpMapping.getApplicationProtocol())) {
						redirection.setHttpsUrl(httpMapping.getUrl());
						redirection.setHttpsUrlStatus(httpMapping.getStatus());
						redirection.setCustomHttpsAlias(httpMapping.getCustonName());
						redirection.setCustomHttpsUrl(httpMapping.getCustromUrl());
						redirection.setHttpsMappingId(httpMapping.getId());
					}
				}
				
				//...  and endpoints
				redirection.setEndpoints(portMappingTemplate.getEndpoints());
			} else {
				redirection.setHttp(false);
				//fetching port mappings
				if(applianceInstance.getVirtualMachines() != null && applianceInstance.getVirtualMachines().size() > 0) {
					redirection.setPortMappings(matchPortMappings(applianceInstance.getVirtualMachines().get(0).getPortMappings(), redirection.getId()));
				}
			}
		}
		
		return result;
	}

	private Redirection findSshRedirection(List<Redirection> redirections) {
		for (Redirection redirection : redirections) {
			if (redirection.getName().equals("ssh") && redirection.getProtocol().equals("tcp")) {
				return redirection;
			}
		}
		
		return null;
	}

	private List<PortMapping> matchPortMappings(List<PortMapping> portMappings, String portMappingTemplateId) {
		List<PortMapping> result = new ArrayList<PortMapping>();
	
		for(PortMapping portMapping : portMappings) {
			if(portMappingTemplateId.equals(portMapping.getPortMappingTemplateId())) {
				result.add(portMapping);
			}
		}
		
		return result;
	}

	private void updateView(AggregateAppliance applianceInstance) {
		if(STATE_SATISFIED.equals(applianceInstance.getState())) {
			if(applianceInstance.getVirtualMachines().size() > 0) {
				view.showDetailsPanel(true);
				view.showNoVmsLabel(false);
				
				AggregateVm applianceVm = applianceInstance.getVirtualMachines().get(0);
				view.getIp().setHTML(applianceVm.getIp() != null ? applianceVm.getIp() : "&nbsp;");
				updateStatus(applianceVm);
				view.getLocation().setText(applianceVm.getComputeSite().getName());
				displayDetails();
			} else {
				view.showDetailsPanel(false);
				view.showNoVmsLabel(true);
				view.getIp().setHTML("&nbsp;");
				view.getLocation().setHTML("&nbsp;");
				view.setStatus(view.getNoVmsLabel());
				view.enableCollapsable(true);
			}
		}
	}

	private void saveInPlace() {
		view.setSaveInPlaceBusyState(true);
		cloudFacadeController.getApplianceInstance(applianceInstance.getId(), new ApplianceInstanceCallback() {
			@Override
			public void processApplianceInstance(ApplianceInstance applianceInstance) {
				if(applianceInstance != null) {
					cloudFacadeController.saveApplianceTypeInPlace(applianceInstance.getId(), applianceInstance.getApplianceTypeId(), new ApplianceTypeCallback() {
						@Override
						public void onError(CloudFacadeError error) {
							view.setSaveInPlaceBusyState(false);
						}
						
						@Override
						public void processApplianceType(ApplianceType applianceType) {
							view.setSaveInPlaceBusyState(false);
							eventBus.updateApplianceTypeView(applianceType);
						}
					});
				} else {
					CloudFacadeError error = new CloudFacadeError();
					error.setMessage(view.missingApplianceType());
					eventBus.displayError(error);
					view.setSaveInPlaceBusyState(false);
				}
			}

			@Override
			public void onError(CloudFacadeError error) {
				view.setSaveInPlaceBusyState(false);
			}
		});
	}
}