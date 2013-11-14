package pl.cyfronet.coin.clew.client.widgets.applianceset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceInstancesCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.ApplianceSet;
import pl.cyfronet.coin.clew.client.widgets.applianceset.IApplianceSetView.IApplianceSetPresenter;
import pl.cyfronet.coin.clew.client.widgets.instance.InstancePresenter;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = ApplianceSetView.class, multiple = true)
public class ApplianceSetPresenter extends BasePresenter<IApplianceSetView, MainEventBus> implements IApplianceSetPresenter {
	private CloudFacadeController cloudFacadeController;
	private String applianceSetId;
	private Map<String, InstancePresenter> instancePresenters;

	@Inject
	public ApplianceSetPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
		instancePresenters = new HashMap<String, InstancePresenter>();
	}

	public void setApplianceSet(ApplianceSet applianceSet) {
		applianceSetId = applianceSet.getId();
		view.getName().setText(applianceSet.getName());
		cloudFacadeController.getApplianceInstances(applianceSetId, new ApplianceInstancesCallback() {
			@Override
			public void processApplianceInstances(List<ApplianceInstance> applianceInstances) {
				view.getInstanceContainer().clear();
				
				if (applianceInstances.size() == 0) {
					view.addNoInstancesLabel();
				} else {
					for (ApplianceInstance instance : applianceInstances) {
						InstancePresenter presenter = eventBus.addHandler(InstancePresenter.class);
						instancePresenters.put(instance.getId(), presenter);
						view.getInstanceContainer().add(presenter.getView().asWidget());
						presenter.setInstance(instance, false);
					}
				}
			}
		});
	}

	@Override
	public void onShutdown() {
		if (view.confirmShutdown()) {
			view.setShutdownBusyState(true);
			cloudFacadeController.shutdownApplianceSet(applianceSetId, new Command() {
				@Override
				public void execute() {
					view.setShutdownBusyState(false);
					eventBus.removeApplianceSet(applianceSetId);
				}
			});
		}
	}
}