package pl.cyfronet.coin.clew.client.widgets.workflows;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class WorkflowsView extends Composite implements IWorkflowsView {
	private static WorkflowsViewUiBinder uiBinder = GWT.create(WorkflowsViewUiBinder.class);
	interface WorkflowsViewUiBinder extends UiBinder<Widget, WorkflowsView> {}

	public WorkflowsView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}