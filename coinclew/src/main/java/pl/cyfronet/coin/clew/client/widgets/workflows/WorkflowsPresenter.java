package pl.cyfronet.coin.clew.client.widgets.workflows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceSetsCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.ApplianceSet;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.NewApplianceSet.Type;
import pl.cyfronet.coin.clew.client.widgets.applianceset.ApplianceSetPresenter;
import pl.cyfronet.coin.clew.client.widgets.workflows.IWorkflowsView.IWorkflowsPresenter;

import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = WorkflowsView.class)
public class WorkflowsPresenter extends BasePresenter<IWorkflowsView, MainEventBus> implements IWorkflowsPresenter {
	private static final int REFRESH_MILIS = 5000;
	
	private CloudFacadeController cloudFacadeController;
	private Map<String, ApplianceSetPresenter> applianceSetPresenters;
	private Timer timer;

	@Inject
	public WorkflowsPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
		applianceSetPresenters = new HashMap<String, ApplianceSetPresenter>();
	}
	
	public void onSwitchToWorkflowsView() {
		eventBus.setBody(view);
		loadWorkflows();
	}

	private void loadWorkflows() {
		cloudFacadeController.getApplianceSets(Type.workflow, new ApplianceSetsCallback() {
			@Override
			public void processApplianceSet(List<ApplianceSet> applianceSets) {
				if (applianceSets.size() == 0) {
					view.showNoWorkflowsLabel(true);
				} else {
					view.showNoWorkflowsLabel(false);
				}
				
				List<String> currentApplianceSets = new ArrayList<String>();
				
				for (ApplianceSet applianceSet : applianceSets) {
					ApplianceSetPresenter presenter = applianceSetPresenters.get(applianceSet.getId());
					
					if (presenter == null) {
						presenter = eventBus.addHandler(ApplianceSetPresenter.class);
						applianceSetPresenters.put(applianceSet.getId(), presenter);
						view.getWorkflowsContainer().add(presenter.getView().asWidget());
					}
					
					presenter.setApplianceSet(applianceSet);
					currentApplianceSets.add(applianceSet.getId());
				}
				
				//removing appliance sets which were not updated in the last request
				for (Iterator<String> i = applianceSetPresenters.keySet().iterator(); i.hasNext(); ) {
					String applianceSetId = i.next();
					
					if (!currentApplianceSets.contains(applianceSetId)) {
						ApplianceSetPresenter presenter = applianceSetPresenters.get(applianceSetId);
						presenter.cleanUp();
						eventBus.removeHandler(presenter);
						view.getWorkflowsContainer().remove(presenter.getView().asWidget());
						i.remove();
					}
				}
				
				if (timer == null) {
					timer = new Timer() {
						@Override
						public void run() {
							loadWorkflows();
						}
					};
				}

				timer.schedule(REFRESH_MILIS);
			}
		});
	}
	
	public void onDeactivateWorkflowsRefresh() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	
	public void onRemoveApplianceSet(String applianceSetId) {
		ApplianceSetPresenter presenter = applianceSetPresenters.get(applianceSetId);
		
		if (presenter != null) {
			presenter.cleanUp();
			eventBus.removeHandler(presenter);
			applianceSetPresenters.remove(applianceSetId);
			view.getWorkflowsContainer().remove(presenter.getView().asWidget());
			
			if (applianceSetPresenters.size() == 0) {
				view.showNoWorkflowsLabel(true);
			}
		}
	}
}