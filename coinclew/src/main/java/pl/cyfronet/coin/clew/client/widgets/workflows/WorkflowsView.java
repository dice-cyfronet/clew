package pl.cyfronet.coin.clew.client.widgets.workflows;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.constants.IconType;
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
	
	@UiField FlowPanel applianceSetContainer;
	@UiField WorkflowsMessages messages;

	public WorkflowsView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void clearWorkflowContainer() {
		applianceSetContainer.clear();
	}

	@Override
	public void addWorkflowProgressIndicator() {
		Icon icon = new Icon(IconType.SPINNER);
		icon.addStyleName("icon-spin");
		applianceSetContainer.add(icon);
	}

	@Override
	public void addNoWorkflowsLabel() {
		Label label = new Label(messages.noApplianceSetsLabel());
		applianceSetContainer.add(label);
	}

	@Override
	public HasWidgets getWorkflowsContainer() {
		return applianceSetContainer;
	}
}