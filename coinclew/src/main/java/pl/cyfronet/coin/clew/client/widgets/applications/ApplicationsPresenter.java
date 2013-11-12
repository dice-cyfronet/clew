package pl.cyfronet.coin.clew.client.widgets.applications;

import java.util.List;

import pl.cyfronet.coin.clew.client.ErrorCode;
import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceInstancesCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance;
import pl.cyfronet.coin.clew.client.widgets.applications.IApplicationsView.IApplicationsPresenter;

import com.github.gwtbootstrap.client.ui.Label;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = ApplicationsView.class)
public class ApplicationsPresenter extends BasePresenter<IApplicationsView, MainEventBus> implements IApplicationsPresenter {
	private CloudFacadeController cloudFacadeController;

	@Inject
	public ApplicationsPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
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
				if (applianceInstances.size() == 0) {
					view.clearInstanceContainer();
					view.addNoInstancesLabel();
				} else {
					for (ApplianceInstance applianceInstance : applianceInstances) {
						view.getInstanceContainer().add(new Label(applianceInstance.getId()));
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