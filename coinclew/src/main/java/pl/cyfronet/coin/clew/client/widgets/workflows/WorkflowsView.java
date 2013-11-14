package pl.cyfronet.coin.clew.client.widgets.workflows;

import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class WorkflowsView extends Composite implements IWorkflowsView {
	private static WorkflowsViewUiBinder uiBinder = GWT.create(WorkflowsViewUiBinder.class);
	interface WorkflowsViewUiBinder extends UiBinder<Widget, WorkflowsView> {}
	
	private Label noWorkflowLabel;
	
	@UiField FlowPanel applianceSetContainer;
	@UiField WorkflowsMessages messages;

	public WorkflowsView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void showNoWorkflowsLabel(boolean show) {
		if (show) {
			if (noWorkflowLabel == null) {
				noWorkflowLabel = new Label(messages.noApplianceSetsLabel());
				applianceSetContainer.add(noWorkflowLabel);
			}
		} else {
			if (noWorkflowLabel != null) {
				applianceSetContainer.remove(noWorkflowLabel);
				noWorkflowLabel = null;
			}
		}
	}

	@Override
	public HasWidgets getWorkflowsContainer() {
		return applianceSetContainer;
	}
}