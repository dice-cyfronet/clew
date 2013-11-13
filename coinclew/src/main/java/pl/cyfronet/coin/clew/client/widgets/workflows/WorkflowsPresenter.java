package pl.cyfronet.coin.clew.client.widgets.workflows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceSetsCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.ApplianceSet;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.NewApplianceSet;
import pl.cyfronet.coin.clew.client.widgets.applianceset.ApplianceSetPresenter;
import pl.cyfronet.coin.clew.client.widgets.workflows.IWorkflowsView.IWorkflowsPresenter;

import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = WorkflowsView.class)
public class WorkflowsPresenter extends BasePresenter<IWorkflowsView, MainEventBus> implements IWorkflowsPresenter {
	private static final int REFRESH_MILIS = 10000;
	
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
		view.clearWorkflowContainer();
		view.addWorkflowProgressIndicator();
		cloudFacadeController.getApplianceSets(NewApplianceSet.Type.workflow, new ApplianceSetsCallback() {
			@Override
			public void processApplianceSet(List<ApplianceSet> applianceSets) {
				view.clearWorkflowContainer();
				
				if (applianceSets.size() == 0) {
					view.addNoWorkflowsLabel();
				} else {
					for (ApplianceSet applianceSet : applianceSets) {
						ApplianceSetPresenter presenter = eventBus.addHandler(ApplianceSetPresenter.class);
						applianceSetPresenters.put(applianceSet.getId(), presenter);
						presenter.setApplianceSet(applianceSet);
						view.getWorkflowsContainer().add(presenter.getView().asWidget());
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
			eventBus.removeHandler(presenter);
			applianceSetPresenters.remove(applianceSetId);
			view.getWorkflowsContainer().remove(presenter.getView().asWidget());
			
			if (applianceSetPresenters.size() == 0) {
				view.addNoWorkflowsLabel();
			}
		}
	}
}