package pl.cyfronet.coin.clew.client.widgets.development;

import java.util.List;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.auth.MiTicketReader;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceInstancesCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypesCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.widgets.atomicservice.AtomicServicePresenter;
import pl.cyfronet.coin.clew.client.widgets.development.IDevelopmentView.IDevelopmentPresenter;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = DevelopmentView.class)
public class DevelopmentPresenter extends BasePresenter<IDevelopmentView, MainEventBus> implements IDevelopmentPresenter {
	private CloudFacadeController cloudFacadeController;
	private MiTicketReader ticketReader;

	@Inject
	public DevelopmentPresenter(CloudFacadeController cloudFacadeController, MiTicketReader ticketReader) {
		this.cloudFacadeController = cloudFacadeController;
		this.ticketReader = ticketReader;
	}
	
	public void onSwitchToDevelopmentView() {
		if (ticketReader.isDeveloper()) {
			eventBus.setBody(view);
			loadDevelopmentResources();
		}
	}
	
	public void onRefreshDevelopmentInstanceList() {
		loadInstances();
	}

	@Override
	public void onManageUserKeysClicked() {
		eventBus.showKeyManagerDialog();
	}
	
	private void loadDevelopmentResources() {
		loadAtomicServices();
		loadInstances();
	}

	private void loadInstances() {
		view.getInstanceContainer().clear();
		view.showInstanceLoadingIndicator(true);
		cloudFacadeController.getDevelopmentApplianceInstances(new ApplianceInstancesCallback() {
			@Override
			public void processApplianceInstances(List<ApplianceInstance> applianceInstances) {
				view.showInstanceLoadingIndicator(false);
				
				if (applianceInstances.size() == 0) {
					view.showNoRunningInstancesLabel(true);
				} else {
					view.showNoRunningInstancesLabel(false);
					Window.alert("Show " + applianceInstances.size() + " development instances.");
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
}