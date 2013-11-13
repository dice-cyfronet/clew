package pl.cyfronet.coin.clew.client.widgets.applications;

import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.clew.client.ErrorCode;
import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceInstancesCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance;
import pl.cyfronet.coin.clew.client.widgets.applications.IApplicationsView.IApplicationsPresenter;
import pl.cyfronet.coin.clew.client.widgets.instance.InstancePresenter;

import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = ApplicationsView.class)
public class ApplicationsPresenter extends BasePresenter<IApplicationsView, MainEventBus> implements IApplicationsPresenter {
	private CloudFacadeController cloudFacadeController;
	private List<InstancePresenter> instancePresenters;

	@Inject
	public ApplicationsPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
		instancePresenters = new ArrayList<InstancePresenter>();
	}
	
	@Override
	public void onStartInstance() {
		eventBus.showStartInstanceDialog();
	}
	
	public void onStart() {
		eventBus.setBody(view);
		loadApplianceInstances();
	}

	public void onSwitchToApplicationsView() {
		eventBus.setBody(view);
		loadApplianceInstances();
	}
	
	private void loadApplianceInstances() {
		view.clearInstanceContainer();
		view.showLoadingInicator();
		cloudFacadeController.getApplianceInstances(new ApplianceInstancesCallback() {
			@Override
			public void processApplianceInstances(List<ApplianceInstance> applianceInstances) {
				view.clearInstanceContainer();
				
				if (applianceInstances.size() == 0) {
					view.addNoInstancesLabel();
				} else {
					view.showHeaderRow(true);
					
					for (ApplianceInstance applianceInstance : applianceInstances) {
						InstancePresenter presenter = eventBus.addHandler(InstancePresenter.class);
						instancePresenters.add(presenter);
						presenter.setInstance(applianceInstance);
						view.getInstanceContainer().add(presenter.getView().asWidget());
					}
				}
			}
			
			@Override
			protected void onError(Throwable e) {
				eventBus.displayError(ErrorCode.CF_ERROR);
				view.clearInstanceContainer();
				view.addNoInstancesLabel();
			}
		});
	}
}