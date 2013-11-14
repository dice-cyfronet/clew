package pl.cyfronet.coin.clew.client.widgets.applications;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.ErrorCode;
import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceInstancesCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance;
import pl.cyfronet.coin.clew.client.widgets.applications.IApplicationsView.IApplicationsPresenter;
import pl.cyfronet.coin.clew.client.widgets.instance.InstancePresenter;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = ApplicationsView.class)
public class ApplicationsPresenter extends BasePresenter<IApplicationsView, MainEventBus> implements IApplicationsPresenter {
	private CloudFacadeController cloudFacadeController;
	private Map<String, InstancePresenter> instancePresenters;

	@Inject
	public ApplicationsPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
		instancePresenters = new HashMap<String, InstancePresenter>();
	}
	
	@Override
	public void onStartInstance() {
		eventBus.showStartInstanceDialog();
	}

	public void onSwitchToApplicationsView() {
		eventBus.setBody(view);
		loadApplianceInstances();
	}
	
	public void onRemoveInstance(String applianceInstanceId) {
		InstancePresenter instancePresenter = instancePresenters.get(applianceInstanceId);
		
		if (instancePresenter != null) {
			eventBus.removeHandler(instancePresenter);
			view.getInstanceContainer().remove(instancePresenter.getView().asWidget());
			instancePresenters.remove(applianceInstanceId);
			
			if (instancePresenters.size() == 0) {
				view.showHeaderRow(false);
				view.addNoInstancesLabel();
			}
		}
	}
	
	public void onRefreshInstanceList() {
		loadApplianceInstances();
	}
	
	private void loadApplianceInstances() {
		view.clearInstanceContainer();
		view.showLoadingInicator();
		cloudFacadeController.getPortalApplianceInstances(new ApplianceInstancesCallback() {
			@Override
			public void processApplianceInstances(List<ApplianceInstance> applianceInstances) {
				view.clearInstanceContainer();
				
				if (applianceInstances.size() == 0) {
					view.addNoInstancesLabel();
				} else {
					view.showHeaderRow(true);
					
					for (ApplianceInstance applianceInstance : applianceInstances) {
						InstancePresenter presenter = eventBus.addHandler(InstancePresenter.class);
						instancePresenters.put(applianceInstance.getId(), presenter);
						presenter.setInstance(applianceInstance, true);
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