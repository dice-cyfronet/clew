package pl.cyfronet.coin.clew.client.widgets.workflows;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

public interface IWorkflowsView extends IsWidget {
	public interface IWorkflowsPresenter {
		
	}

	void addWorkflowProgressIndicator();
	void addNoWorkflowsLabel();
	HasWidgets getWorkflowsContainer();
}