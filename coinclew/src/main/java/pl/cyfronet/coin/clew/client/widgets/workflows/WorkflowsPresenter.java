package pl.cyfronet.coin.clew.client.widgets.workflows;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.widgets.workflows.IWorkflowsView.IWorkflowsPresenter;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = WorkflowsView.class)
public class WorkflowsPresenter extends BasePresenter<IWorkflowsView, MainEventBus> implements IWorkflowsPresenter {
	public void onSwitchToWorkflowsView() {
		eventBus.setBody(view);
	}
}