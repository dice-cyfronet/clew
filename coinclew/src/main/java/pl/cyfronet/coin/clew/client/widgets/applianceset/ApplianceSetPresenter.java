package pl.cyfronet.coin.clew.client.widgets.applianceset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliance.AggregateAppliance;
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

	public void setApplianceSet(ApplianceSet applianceSet, List<AggregateAppliance> appliances) {
		if(applianceSetId == null) {
			applianceSetId = applianceSet.getId();
			view.getName().setText(applianceSet.getName());
		}
		
		if(appliances.size() == 0) {
			view.showNoInstancesLabel(true);
		} else {
			view.showNoInstancesLabel(false);
		}
		
		List<String> currentInstances = new ArrayList<String>();
		
		for(AggregateAppliance instance : appliances) {
			InstancePresenter presenter = instancePresenters.get(instance.getId());
			
			if(presenter == null) {
				presenter = eventBus.addHandler(InstancePresenter.class);
				instancePresenters.put(instance.getId(), presenter);
				view.getInstanceContainer().add(presenter.getView().asWidget());
			}
			
			presenter.setInstance(instance, false, false);
			currentInstances.add(instance.getId());
		}
		
		//removing those instances which were not sent during the last update
		for(Iterator<String> i = instancePresenters.keySet().iterator(); i.hasNext(); ) {
			String instanceId = i.next();
			
			if (!currentInstances.contains(instanceId)) {
				InstancePresenter presenter = instancePresenters.get(instanceId);
				eventBus.removeHandler(presenter);
				view.getInstanceContainer().remove(presenter.getView().asWidget());
				i.remove();
			}
		}
	}

	@Override
	public void onShutdown() {
		if(view.confirmShutdown()) {
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

	public void cleanUp() {
		for(InstancePresenter instancePresenter : instancePresenters.values()) {
			eventBus.removeHandler(instancePresenter);
			view.getInstanceContainer().remove(instancePresenter.getView().asWidget());
		}
		
		instancePresenters.clear();
		view.showNoInstancesLabel(true);
	}
}