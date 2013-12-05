package pl.cyfronet.coin.clew.client.widgets.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.ErrorCode;
import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypeCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceVmsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ComputeSiteCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.DevelopmentModePropertySetCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.RedirectionsCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.appliancevm.ApplianceVm;
import pl.cyfronet.coin.clew.client.controller.cf.computesite.ComputeSite;
import pl.cyfronet.coin.clew.client.controller.cf.devmodepropertyset.DevelopmentModePropertySet;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.Endpoint;
import pl.cyfronet.coin.clew.client.controller.cf.portmapping.PortMapping;
import pl.cyfronet.coin.clew.client.controller.overlay.Redirection;
import pl.cyfronet.coin.clew.client.widgets.instance.IInstanceView.IInstancePresenter;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = InstanceView.class, multiple = true)
public class InstancePresenter extends BasePresenter<IInstanceView, MainEventBus> implements IInstancePresenter {
	protected CloudFacadeController cloudFacadeController;
	private boolean detailsRendered;
	private Map<String, IsWidget> accessInfos;
	private Map<String, IsWidget> webapps;
	private Map<String, IsWidget> services;
	private boolean developmentMode;
	private ApplianceInstance applianceInstance;
	
	@Inject
	public InstancePresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
		accessInfos = new HashMap<String, IsWidget>();
		webapps = new HashMap<String, IsWidget>();
		services = new HashMap<String, IsWidget>();
	}
	
	public void setInstance(final ApplianceInstance applianceInstance, final boolean enableShutdown, final boolean developmentMode) {
		this.developmentMode = developmentMode;
		
		if (this.applianceInstance != null) {
			//we are updating
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
				
//				view.getSpec().setText(view.getSpecStanza(applianceType.getPreferenceCpu(), applianceType.getPreferenceMemory(), applianceType.getPreferenceDisk()));
				
				if (enableShutdown) {
					view.addShutdownControl();
				}
				
				if (developmentMode) {
					view.addExternalInterfacesControl();
					view.addSaveControl();
					view.showAccessInfoSection();
				}
				
				if (!detailsRendered) {
					detailsRendered = true;
					displayDetails();
				}
			}
		});
		cloudFacadeController.getInstanceVms(applianceInstance.getId(), new ApplianceVmsCallback() {
			@Override
			public void processApplianceVms(List<ApplianceVm> applianceVms) {
				if (applianceVms.size() > 0) {
					//TODO(DH): for now details of the first VM are shown only
					ApplianceVm applianceVm = applianceVms.get(0);
					view.getIp().setHTML(applianceVm.getIp() != null ? applianceVm.getIp() : "&nbsp;");
					view.getStatus().setHTML(applianceVm.getState() != null ? applianceVm.getState() : "&nbsp;");
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
	
	private void displayDetails() {
		if (developmentMode) {
			cloudFacadeController.getDevelopmentModePropertySet(applianceInstance.getId(), new DevelopmentModePropertySetCallback() {
				@Override
				public void processDeveopmentModePropertySet(DevelopmentModePropertySet developmentModePropertySet) {
					cloudFacadeController.getRedirectionsForDevPropertySetId(developmentModePropertySet.getId(), new RedirectionsCallback() {
						@Override
						public void processRedirections(List<Redirection> redirections) {
							displayRedirections(redirections, developmentMode);
						}
					});
				}});
		} else {
			cloudFacadeController.getRedirectionsForApplianceType(applianceInstance.getApplianceTypeId(), new RedirectionsCallback() {
				@Override
				public void processRedirections(List<Redirection> redirections) {
					displayRedirections(redirections, developmentMode);
				}});
		}
	}
	
	private void displayRedirections(List<Redirection> redirections, boolean developmentMode) {
		List<String> presentWebappIds = new ArrayList<String>();
		List<String> presentServiceIds = new ArrayList<String>();
		List<String> presentAccessInfos = new ArrayList<String>();
		
		for (Redirection redirection : redirections) {
			if (redirection.isHttp()) {
				if (redirection.getEndpoints() != null) {
					for (Endpoint endpoint : redirection.getEndpoints()) {
						if ("webapp".equals(endpoint.getEndpointType())) {
							presentWebappIds.add(endpoint.getId());
							
							if (!webapps.keySet().contains(endpoint.getId())) {
								IsWidget widget = view.addWebApplication(endpoint.getName(), redirection.getHttpUrl(), redirection.getHttpsUrl());
								webapps.put(endpoint.getId(), widget);
							}
						} else {
							presentServiceIds.add(endpoint.getId());
							
							if (!services.keySet().contains(endpoint.getId())) {
								IsWidget widget = view.addService(endpoint.getName(), redirection.getHttpUrl(), redirection.getHttpsUrl(), endpoint.getDescriptor());
								services.put(endpoint.getId(), widget);
							}
						}
					}
				}
			} else if (developmentMode) {
				//access info is displayed only in development mode
				if (redirection.getPortMappings().size() > 0) {
					//TODO(DH): for now only the first port mapping is used
					PortMapping portMapping = redirection.getPortMappings().get(0);
					presentAccessInfos.add(redirection.getId());
					
					if (!accessInfos.keySet().contains(redirection.getId())) {
						IsWidget widget = view.addAccessInfo(redirection.getName(), portMapping.getPublicIp(), portMapping.getSourcePort());
						accessInfos.put(redirection.getId(), widget);
					}
				}
			}
		}
		
		for (Iterator<String> i = webapps.keySet().iterator(); i.hasNext();) {
			String endpointId = i.next();
			
			if (!presentWebappIds.contains(endpointId)) {
				view.removeWebapp(webapps.remove(endpointId));
			}
		}
		
		for (Iterator<String> i = services.keySet().iterator(); i.hasNext();) {
			String endpointId = i.next();
			
			if (!presentServiceIds.contains(endpointId)) {
				view.removeService(services.remove(endpointId));
			}
		}
		
		for (Iterator<String> i = accessInfos.keySet().iterator(); i.hasNext();) {
			String redirectionId = i.next();
			
			if (!presentAccessInfos.contains(redirectionId)) {
				view.removeAccessInfo(accessInfos.remove(redirectionId));
			}
		}
		
		if (presentWebappIds.size() == 0) {
			view.showNoWebApplicationsLabel(true);
		} else {
			view.showNoWebApplicationsLabel(false);
		}
		
		if (presentServiceIds.size() == 0) {
			view.showNoServicesLabel(true);
		} else {
			view.showNoServicesLabel(false);
		}
		
		if (presentAccessInfos.size() == 0) {
			view.showNoAccessInfoLabel(true);
		} else {
			view.showNoAccessInfoLabel(false);
		}
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
		cloudFacadeController.getInstanceVms(applianceInstance.getId(), new ApplianceVmsCallback() {
			@Override
			public void processApplianceVms(List<ApplianceVm> applianceVms) {
				if (applianceVms.size() > 0) {
					//TODO(DH): for now details of the first VM are shown only
					ApplianceVm applianceVm = applianceVms.get(0);
					view.getIp().setHTML(applianceVm.getIp() != null ? applianceVm.getIp() : "&nbsp;");
					view.getStatus().setHTML(applianceVm.getState() != null ? applianceVm.getState() : "&nbsp;");
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