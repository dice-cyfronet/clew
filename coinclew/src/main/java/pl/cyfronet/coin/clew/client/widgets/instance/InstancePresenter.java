package pl.cyfronet.coin.clew.client.widgets.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.ErrorCode;
import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.UrlHelper;
import pl.cyfronet.coin.clew.client.auth.MiTicketReader;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypeCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceVmsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ComputeSiteCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.DevelopmentModePropertySetCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.RedirectionsCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance.State;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.appliancevm.ApplianceVm;
import pl.cyfronet.coin.clew.client.controller.cf.computesite.ComputeSite;
import pl.cyfronet.coin.clew.client.controller.cf.devmodepropertyset.DevelopmentModePropertySet;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.Endpoint;
import pl.cyfronet.coin.clew.client.controller.cf.portmapping.PortMapping;
import pl.cyfronet.coin.clew.client.controller.overlay.Redirection;
import pl.cyfronet.coin.clew.client.widgets.instance.IInstanceView.IInstancePresenter;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = InstanceView.class, multiple = true)
public class InstancePresenter extends BasePresenter<IInstanceView, MainEventBus> implements IInstancePresenter {
	protected CloudFacadeController cloudFacadeController;
	private Map<String, IsWidget> webapps;
	private Map<String, IsWidget> services;
	private Map<String, IsWidget> otherServices;
	private boolean developmentMode;
	private ApplianceInstance applianceInstance;
	private MiTicketReader ticketReader;
	
	@Inject
	public InstancePresenter(CloudFacadeController cloudFacadeController, MiTicketReader ticketReader) {
		this.cloudFacadeController = cloudFacadeController;
		this.ticketReader = ticketReader;
		webapps = new HashMap<String, IsWidget>();
		services = new HashMap<String, IsWidget>();
		otherServices = new HashMap<String, IsWidget>();
	}
	
	public void setInstance(final ApplianceInstance applianceInstance, final boolean enableShutdown, final boolean developmentMode) {
		this.developmentMode = developmentMode;
		
		if (this.applianceInstance != null) {
			updateView(applianceInstance);
			
			return;
		}

		this.applianceInstance = applianceInstance;
		cloudFacadeController.getApplianceType(applianceInstance.getApplianceTypeId(), new ApplianceTypeCallback() {
			@Override
			public void processApplianceType(ApplianceType applianceType) {
				if (applianceInstance.getName() != null && !applianceInstance.getName().isEmpty()) {
					view.getName().setText(applianceInstance.getName());
				} else {
					view.getName().setText(applianceType.getName());
				}
				
				if (applianceInstance.getState() == State.unsatisfied) {
					view.setUnsatisfiedState(applianceInstance.getStateExplanation());
				}
				
				view.getCost().setText(costIndicator(applianceInstance));
				
				if(applianceType != null && !applianceType.getDescription().isEmpty()) {
					view.getDescription().setText(applianceType.getDescription());
				} else {
					view.setNoDescription();
				}
				
//				view.getSpec().setText(view.getSpecStanza(applianceType.getPreferenceCpu(), applianceType.getPreferenceMemory(), applianceType.getPreferenceDisk()));
				
				if (enableShutdown) {
					view.addShutdownControl();
				}
				
				if (applianceInstance.getState() == State.satisfied) {
					if (developmentMode) {
						view.addExternalInterfacesControl();
						view.addSaveControl();
					}
					
					displayDetails();
				}
			}
		});
		
		if (applianceInstance.getState() == State.satisfied) {
			cloudFacadeController.getInstanceVms(applianceInstance.getId(), new ApplianceVmsCallback() {
				@Override
				public void processApplianceVms(List<ApplianceVm> applianceVms) {
					if (applianceVms.size() > 0) {
						//TODO(DH): for now details of the first VM are shown only
						ApplianceVm applianceVm = applianceVms.get(0);
						view.getIp().setHTML(applianceVm.getIp() != null ? applianceVm.getIp() : "&nbsp;");
						updateStatus(applianceVm);
						cloudFacadeController.getComputeSite(applianceVm.getComputeSiteId(), new ComputeSiteCallback() {
							@Override
							public void processComputeSite(ComputeSite computeSite) {
								view.getLocation().setText(computeSite.getName());
							}
						});
					} else {
						eventBus.displayError(ErrorCode.APPLIANCE_VM_DETAILS_MISSING);
					}
				}
			});
		}
	}
	
	private void updateStatus(ApplianceVm applianceVm) {
		view.setStatus(applianceVm.getState() != null ? applianceVm.getState() : "&nbsp;");
		
		if(applianceVm.getState() != null) {
			if(applianceVm.getState().equals("active")) {
				view.enableSave(true);
				view.enableExternalInterfaces(true);
				view.enableCollapsable(true);
			} else if(applianceVm.getState().equals("saving")) {
				view.enableSave(false);
				view.enableExternalInterfaces(false);
				view.collapseDetails();
				view.enableCollapsable(false);
			}
		}
	}
	
	private String costIndicator(final ApplianceInstance applianceInstance) {
		return "$" + NumberFormat.getFormat("0.00").format(((float) applianceInstance.getAmountBilled() / 10000));
	}
	
	private void displayDetails() {
		if (developmentMode) {
			cloudFacadeController.getDevelopmentModePropertySet(applianceInstance.getId(), new DevelopmentModePropertySetCallback() {
				@Override
				public void processDeveopmentModePropertySet(DevelopmentModePropertySet developmentModePropertySet) {
					cloudFacadeController.getRedirectionsForDevPropertySetId(developmentModePropertySet.getId(), new RedirectionsCallback() {
						@Override
						public void processRedirections(List<Redirection> redirections) {
							displayRedirections(redirections);
						}
					});
				}});
		} else {
			cloudFacadeController.getRedirectionsForApplianceType(applianceInstance.getApplianceTypeId(), new RedirectionsCallback() {
				@Override
				public void processRedirections(List<Redirection> redirections) {
					displayRedirections(redirections);
				}});
		}
	}
	
	private void displayRedirections(List<Redirection> redirections) {
		List<String> currentWebapps = new ArrayList<String>();
		List<String> currentServices = new ArrayList<String>();
		List<String> currentOtherServices = new ArrayList<String>();
		
		for (Redirection redirection : redirections) {
			if (redirection.isHttp()) {
				if (redirection.getEndpoints() != null) {
					for (Endpoint endpoint : redirection.getEndpoints()) {
						if ("webapp".equals(endpoint.getEndpointType())) {
							currentWebapps.add(endpoint.getId());
							
							//before showing a webapp at least one http mapping has to be available
							if(redirection.getHttpUrl() != null || redirection.getHttpsUrl() != null) {
								if (!webapps.keySet().contains(endpoint.getId())) {
									String endpointHttpUrl = UrlHelper.joinUrl(redirection.getHttpUrl(), endpoint.getInvocationPath(),
											endpoint.isSecured() ? ticketReader.getUserLogin() : null, endpoint.isSecured() ? ticketReader.getTicket() : null);
									String endpointHttpsUrl = UrlHelper.joinUrl(redirection.getHttpsUrl(), endpoint.getInvocationPath(),
											endpoint.isSecured() ? ticketReader.getUserLogin() : null, endpoint.isSecured() ? ticketReader.getTicket() : null);
									
									//nx url params fix
									if (redirection.getName().equals("nx")) {
										Redirection sshRedirection = findSshRedirection(redirections);
										
										if (sshRedirection != null && sshRedirection.getPortMappings().size() > 0) {
											PortMapping portMapping = sshRedirection.getPortMappings().get(0);
											
											if (endpointHttpUrl != null) {
												endpointHttpUrl += "?nxport=" + portMapping.getSourcePort() + "&nxhost=" + portMapping.getPublicIp();
											}
											
											if (endpointHttpsUrl != null) {
												endpointHttpsUrl += "?nxport=" + portMapping.getSourcePort() + "&nxhost=" + portMapping.getPublicIp();
											}
										}
									}
									
									IsWidget widget = view.addWebApplication(endpoint.getName(), endpointHttpUrl, endpointHttpsUrl,
											redirection.getId(), redirection.getHttpUrlStatus(), redirection.getHttpsUrlStatus());
									webapps.put(endpoint.getId(), widget);
								} else {
									//updating endpoint status
									if(redirection.getHttpUrl() != null) {
										view.updateHttpStatus(redirection.getId(), redirection.getHttpUrlStatus());
									}
									
									if(redirection.getHttpsUrl() != null) {
										view.updateHttpsStatus(redirection.getId(), redirection.getHttpsUrlStatus());
									}
								}
							}
						} else {
							currentServices.add(endpoint.getId());
							
							//before showing a service at least one http mapping has to be available
							if(redirection.getHttpUrl() != null || redirection.getHttpsUrl() != null) {
								if (!services.keySet().contains(endpoint.getId())) {
									IsWidget widget = view.addService(endpoint.getName(),
											UrlHelper.joinUrl(redirection.getHttpUrl(), endpoint.getInvocationPath(),
													endpoint.isSecured() ? ticketReader.getUserLogin() : null, endpoint.isSecured() ? ticketReader.getTicket() : null),
											UrlHelper.joinUrl(redirection.getHttpsUrl(), endpoint.getInvocationPath(),
													endpoint.isSecured() ? ticketReader.getUserLogin() : null, endpoint.isSecured() ? ticketReader.getTicket() : null),
											endpoint.getDescriptor(), redirection.getId(), redirection.getHttpUrlStatus(), redirection.getHttpsUrlStatus());
									services.put(endpoint.getId(), widget);
								} else {
									//updating endpoint status
									if(redirection.getHttpUrl() != null) {
										view.updateHttpStatus(redirection.getId(), redirection.getHttpUrlStatus());
									}
									
									if(redirection.getHttpsUrl() != null) {
										view.updateHttpsStatus(redirection.getId(), redirection.getHttpsUrlStatus());
									}
								}
							}
						}
					}
				}
			} else {
				if (redirection.getPortMappings().size() > 0) {
					//TODO(DH): for now only the first port mapping is used
					PortMapping portMapping = redirection.getPortMappings().get(0);
					currentOtherServices.add(redirection.getId());
					
					if (!otherServices.keySet().contains(redirection.getId())) {
						String helpBlock = null;
						
						if (redirection.getName().toLowerCase().startsWith("ssh") && developmentMode) {
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
		
		for (String webappId : webapps.keySet()) {
			if (!currentWebapps.contains(webappId)) {
				view.removeWebapp(webapps.remove(webappId));
			}
		}
		
		for (String serviceId : services.keySet()) {
			if (!currentServices.contains(serviceId)) {
				view.removeService(services.remove(serviceId));
			}
		}
		
		for (String otherServiceId : otherServices.keySet()) {
			if (!currentOtherServices.contains(otherServiceId)) {
				view.removeOtherService(otherServices.remove(otherServiceId));
			}
		}
		
		if (webapps.size() == 0) {
			view.showNoWebApplicationsLabel(true);
		} else {
			view.showNoWebApplicationsLabel(false);
		}
		
		if (services.size() == 0) {
			view.showNoServicesLabel(true);
		} else {
			view.showNoServicesLabel(false);
		}
		
		if (otherServices.size() == 0) {
			view.showNoOtherServicesLabel(true);
		} else {
			view.showNoOtherServicesLabel(false);
		}
	}

	private Redirection findSshRedirection(List<Redirection> redirections) {
		for (Redirection redirection : redirections) {
			if (redirection.getName().equals("ssh") && redirection.getProtocol().equals("tcp")) {
				return redirection;
			}
		}
		
		return null;
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
	
	private void updateView(ApplianceInstance applianceInstance) {
		if (applianceInstance.getState() == State.satisfied) {
			cloudFacadeController.getInstanceVms(applianceInstance.getId(), new ApplianceVmsCallback() {
				@Override
				public void processApplianceVms(List<ApplianceVm> applianceVms) {
					if (applianceVms.size() > 0) {
						//TODO(DH): for now details of the first VM are shown only
						ApplianceVm applianceVm = applianceVms.get(0);
						view.getIp().setHTML(applianceVm.getIp() != null ? applianceVm.getIp() : "&nbsp;");
						updateStatus(applianceVm);
						cloudFacadeController.getComputeSite(applianceVm.getComputeSiteId(), new ComputeSiteCallback() {
							@Override
							public void processComputeSite(ComputeSite computeSite) {
								view.getLocation().setText(computeSite.getName());
							}
						});
					} else {
						eventBus.displayError(ErrorCode.APPLIANCE_VM_DETAILS_MISSING);
					}
				}
			});
			displayDetails();
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
}