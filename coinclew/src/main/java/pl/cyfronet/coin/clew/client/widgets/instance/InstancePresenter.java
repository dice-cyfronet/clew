package pl.cyfronet.coin.clew.client.widgets.instance;

import java.util.Arrays;
import java.util.List;

import pl.cyfronet.coin.clew.client.ErrorCode;
import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypeCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceVmsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ComputeSiteCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.EndpointsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.HttpMappingsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.PortMappingTemplatesCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.appliancevm.ApplianceVm;
import pl.cyfronet.coin.clew.client.controller.cf.computesite.ComputeSite;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.Endpoint;
import pl.cyfronet.coin.clew.client.controller.cf.httpmapping.HttpMapping;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate.PortMappingTemplate;
import pl.cyfronet.coin.clew.client.widgets.instance.IInstanceView.IInstancePresenter;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = InstanceView.class, multiple = true)
public class InstancePresenter extends BasePresenter<IInstanceView, MainEventBus> implements IInstancePresenter {
	private CloudFacadeController cloudFacadeController;
	private String applianceInstanceId;
	private boolean detailsRendered;
	
	@Inject
	public InstancePresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
	}
	
	public void setInstance(ApplianceInstance applianceInstance, final boolean enableShutdown) {
		applianceInstanceId = applianceInstance.getId();
		cloudFacadeController.getApplianceType(applianceInstance.getApplianceTypeId(), new ApplianceTypeCallback() {
			@Override
			public void processApplianceType(ApplianceType applianceType) {
				view.getName().setText(applianceType.getName());
				view.getSpec().setText(view.getSpecStanza(applianceType.getPreferenceCpu(), applianceType.getPreferenceMemory(), applianceType.getPreferenceDisk()));
				
				if (enableShutdown) {
					view.addShutdownControl();
				}
				
				if (!detailsRendered) {
					detailsRendered = true;
					cloudFacadeController.getPortMappingTemplates(applianceType.getId(), new PortMappingTemplatesCallback() {
						@Override
						public void processPortMappingTemplates(List<PortMappingTemplate> portMappingTemplates) {
							if (portMappingTemplates.size() == 0) {
								view.addNoWebApplicationsLabel();
								view.addNoServicesLabel();
							} else {
								for (PortMappingTemplate portMappingTemplate : portMappingTemplates) {
									if (Arrays.asList(new String[] {"http", "https", "http_https"}).contains(
											portMappingTemplate.getApplicationProtocol())) {
										cloudFacadeController.getEndpoints(portMappingTemplate.getId(), new EndpointsCallback() {
											@Override
											public void processEndpoints(final List<Endpoint> endpoints) {
												if (endpoints.size() == 0) {
													view.addNoWebApplicationsLabel();
													view.addNoServicesLabel();
												} else {
													cloudFacadeController.getHttpMappings(applianceInstanceId, new HttpMappingsCallback() {
														@Override
														public void processHttpMappings(List<HttpMapping> httpMappings) {
															if (httpMappings.size() == 0) {
																view.addNoWebApplicationsLabel();
																view.addNoServicesLabel();
															} else {
																boolean webApplicationPresent = false;
																boolean servicePresent = false;
																//TODO(DH): set endpoint name when available
																for (Endpoint endpoint : endpoints) {
																	String httpUrl = null;
																	String httpsUrl = null;
																	
																	for (HttpMapping httpMapping : httpMappings) {
																		if (httpMapping.getApplicationProtocol().equals("http")) {
																			httpUrl = httpMapping.getUrl() + endpoint.getInvocationPath();
																		} else if (httpMapping.getApplicationProtocol().equals("https")) {
																			httpsUrl = httpMapping.getUrl() + endpoint.getInvocationPath();
																		}
																	}
																	
																	if (endpoint.getEndpointType().equals("webapp")) {
																		view.addWebApplication(httpUrl, httpsUrl);
																		webApplicationPresent = true;
																	} else {
																		view.addService(httpUrl, httpsUrl);
																		servicePresent = true;
																	}
																}
																
																if (!webApplicationPresent) {
																	view.addNoWebApplicationsLabel();
																}
																
																if (!servicePresent) {
																	view.addNoServicesLabel();
																}
															}
														}});
												}
											}});
									} else {
										view.addNoWebApplicationsLabel();
										view.addNoServicesLabel();
									}
								}
							}
						}
					});
				}
			}
		});
		cloudFacadeController.getInstanceVms(applianceInstance.getId(), new ApplianceVmsCallback() {
			@Override
			public void processApplianceVms(List<ApplianceVm> applianceVms) {
				if (applianceVms.size() > 0) {
					//TODO(DH): for now details of the first VM are shown only
					ApplianceVm applianceVm = applianceVms.get(0);
					view.getIp().setText(applianceVm.getIp());
					view.getStatus().setText(applianceVm.getState());
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
	public void onShutdownClicked() {
		if (view.confirmInstanceShutdown()) {
			view.setShutdownBusyState(true);
			cloudFacadeController.shutdownApplianceInstance(applianceInstanceId, new Command() {
				@Override
				public void execute() {
					view.setShutdownBusyState(false);
					eventBus.removeInstance(applianceInstanceId);
				}
			});
		}
	}
}