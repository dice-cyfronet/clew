package pl.cyfronet.coin.clew.client.widgets.development;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.auth.MiTicketReader;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceInstancesCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypesCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate.PortMappingTemplate;
import pl.cyfronet.coin.clew.client.widgets.atomicservice.AtomicServicePresenter;
import pl.cyfronet.coin.clew.client.widgets.development.IDevelopmentView.IDevelopmentPresenter;
import pl.cyfronet.coin.clew.client.widgets.instance.InstancePresenter;

import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = DevelopmentView.class)
public class DevelopmentPresenter extends BasePresenter<IDevelopmentView, MainEventBus> implements IDevelopmentPresenter {
	private static final int REFRESH_MILIS = 5000;
	
	private CloudFacadeController cloudFacadeController;
	private MiTicketReader ticketReader;
	private Map<String, InstancePresenter> instancePresenters;
	private Map<String, AtomicServicePresenter> atomicServicePresenters;
	private Timer timer;

	@Inject
	public DevelopmentPresenter(CloudFacadeController cloudFacadeController, MiTicketReader ticketReader) {
		this.cloudFacadeController = cloudFacadeController;
		this.ticketReader = ticketReader;
		instancePresenters = new HashMap<String, InstancePresenter>();
		atomicServicePresenters = new HashMap<String, AtomicServicePresenter>();
	}
	
	public void onSwitchToDevelopmentView() {
		if (ticketReader.isDeveloper()) {
			eventBus.setBody(view);
			loadDevelopmentResources();
		}
	}
	
	public void onRefreshDevelopmentInstanceList() {
		loadInstances(true);
	}

	@Override
	public void onManageUserKeysClicked() {
		eventBus.showKeyManagerDialog();
	}
	
	private void loadDevelopmentResources() {
		loadAtomicServices();
		loadInstances(false);
	}
	
	public void onDeactivateDevelopmentRefresh() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	
	public void onExternalInterfacesChanged(String applianceInstanceId) {
		InstancePresenter presenter = instancePresenters.get(applianceInstanceId);
		
		if (presenter != null) {
			presenter.updateAccessInfo();
		}
	}
	
	public void onEndpointsChanged(String applianceInstanceId) {
		InstancePresenter presenter = instancePresenters.get(applianceInstanceId);
		
		if (presenter != null) {
			presenter.updateEndpoints();
		}
	}

	private void loadInstances(boolean update) {
		view.showNoRunningInstancesLabel(false);
		
		if (!update) {
			view.getInstanceContainer().clear();
			view.showInstanceLoadingIndicator(true);
		}
		
		cloudFacadeController.getDevelopmentApplianceInstances(new ApplianceInstancesCallback() {
			@Override
			public void processApplianceInstances(List<ApplianceInstance> applianceInstances) {
				view.showInstanceLoadingIndicator(false);
				
				if (applianceInstances.size() == 0) {
					view.showNoRunningInstancesLabel(true);
				} else {
					view.showNoRunningInstancesLabel(false);
					
					for (ApplianceInstance instance : applianceInstances) {
						
						InstancePresenter presenter = instancePresenters.get(instance.getId());
						
						if (presenter == null) {
							presenter = eventBus.addHandler(InstancePresenter.class);
							instancePresenters.put(instance.getId(), presenter);
							view.getInstanceContainer().add(presenter.getView().asWidget());
						}
						
						presenter.setInstance(instance, true, true);
					}
					
					if (timer == null) {
						timer = new Timer() {
							@Override
							public void run() {
								loadInstances(true);
							}
						};
					}

					timer.schedule(REFRESH_MILIS);
				}
			}
		});
	}

	private void loadAtomicServices() {
		view.getAtomicServicesContainer().clear();
		view.addAtomicServiceProgressIndicator();
		cloudFacadeController.getApplianceTypes(new ApplianceTypesCallback() {
			@Override
			public void processApplianceTypes(List<ApplianceType> applianceTypes) {
				if (applianceTypes.size() == 0) {
					view.getAtomicServicesContainer().clear();
					view.showNoAtomicServicesLabel(true);
				} else {
					view.getAtomicServicesContainer().clear();
					
					for (ApplianceType applianceType : applianceTypes) {
						AtomicServicePresenter presenter = eventBus.addHandler(AtomicServicePresenter.class);
						atomicServicePresenters.put(applianceType.getId(), presenter);
						view.getAtomicServicesContainer().add(presenter.getView().asWidget());
						presenter.setApplianceType(applianceType);
					}
				}
			}
		});
	}

	@Override
	public void onStartDevInstance() {
		eventBus.showStartInstanceDialog(true);
	}
	
	public void onRemoveInstance(String applianceInstanceId) {
		InstancePresenter instancePresenter = instancePresenters.get(applianceInstanceId);
		
		if (instancePresenter != null) {
			eventBus.removeHandler(instancePresenter);
			view.getInstanceContainer().remove(instancePresenter.getView().asWidget());
			instancePresenters.remove(applianceInstanceId);
			
			if (instancePresenters.size() == 0) {
				view.showHeaderRow(false);
				view.showNoRunningInstancesLabel(true);
				onDeactivateDevelopmentRefresh();
			}
		}
	}
	
	public void onUpdateApplianceTypeView(ApplianceType applianceType) {
		AtomicServicePresenter presenter = atomicServicePresenters.get(applianceType.getId());
		
		if (presenter == null) {
			presenter = eventBus.addHandler(AtomicServicePresenter.class);
			atomicServicePresenters.put(applianceType.getId(), presenter);
			view.getAtomicServicesContainer().add(presenter.getView().asWidget());
		}
		
		presenter.setApplianceType(applianceType);
	}
}