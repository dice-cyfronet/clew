package pl.cyfronet.coin.clew.client.widgets.development;

import java.util.List;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.auth.MiTicketReader;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypesCallback;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.widgets.atomicservice.AtomicServicePresenter;
import pl.cyfronet.coin.clew.client.widgets.development.IDevelopmentView.IDevelopmentPresenter;

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

	@Override
	public void onManageUserKeysClicked() {
		eventBus.showKeyManagerDialog();
	}
	
	private void loadDevelopmentResources() {
		loadAtomicService();
		loadInstances();
	}

	private void loadInstances() {
		view.showNoRunningInstancesLabel(true);
	}

	private void loadAtomicService() {
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
}