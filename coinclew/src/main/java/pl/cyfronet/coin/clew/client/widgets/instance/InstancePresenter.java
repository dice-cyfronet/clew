package pl.cyfronet.coin.clew.client.widgets.instance;

import java.util.List;

import pl.cyfronet.coin.clew.client.ErrorCode;
import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypeCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceVmsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ComputeSiteCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.EndpointsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.HttpMappingsCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.appliancevm.ApplianceVm;
import pl.cyfronet.coin.clew.client.controller.cf.computesite.ComputeSite;
import pl.cyfronet.coin.clew.client.controller.cf.endpoint.Endpoint;
import pl.cyfronet.coin.clew.client.controller.cf.httpmapping.HttpMapping;
import pl.cyfronet.coin.clew.client.widgets.instance.IInstanceView.IInstancePresenter;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = InstanceView.class, multiple = true)
public class InstancePresenter extends BasePresenter<IInstanceView, MainEventBus> implements IInstancePresenter {
	private CloudFacadeController cloudFacadeController;
	private String applianceInstanceId;
	
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
		cloudFacadeController.getHttpMappings(applianceInstance.getId(), new HttpMappingsCallback() {
			@Override
			public void processHttpMappings(List<HttpMapping> httpMappings) {
				if (httpMappings.size() == 0) {
					view.addNoWebApplicationsLabel();
					view.addNoServicesLabel();
				} else {
					for (final HttpMapping httpMapping : httpMappings) {
						cloudFacadeController.getEndpoints(httpMapping.getPortMappingTemplateId(), new EndpointsCallback() {
							@Override
							public void processEndpoints(List<Endpoint> endpoints) {
								boolean webappsPresent = false;
								boolean servicesPresent = false;
								
								for (Endpoint endpoint : endpoints) {
									if (endpoint.getEndpointType().equals("webapp")) {
										view.addWebApplication(httpMapping.getUrl());
										webappsPresent = true;
									} else if (endpoint.getEndpointType().equals("ws") || endpoint.getEndpointType().equals("rest")) {
										view.addService(httpMapping.getUrl());
										servicesPresent = true;
									}
								}
								
								if (!webappsPresent) {
									view.addNoWebApplicationsLabel();
								}
								
								if (!servicesPresent) {
									view.addNoServicesLabel();
								}
							}});
					}
				}
			}});
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