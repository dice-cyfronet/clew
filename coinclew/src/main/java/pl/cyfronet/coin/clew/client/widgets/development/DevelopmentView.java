package pl.cyfronet.coin.clew.client.widgets.development;

import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class DevelopmentView extends Composite implements IDevelopmentView {
	private static DevelopmentViewUiBinder uiBinder = GWT.create(DevelopmentViewUiBinder.class);
	interface DevelopmentViewUiBinder extends UiBinder<Widget, DevelopmentView> {}
	
	@UiField FlowPanel atomicServicesContainer;
	@UiField FlowPanel runningInstancesContainer;
	@UiField DevelopmentMessages messages;

	public DevelopmentView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void addNoRunningInstancesLabel() {
		Label label = new Label(messages.noRunningInstanceLabel());
		runningInstancesContainer.add(label);
	}

	@Override
	public void addNoAtomicServicesLabel() {
		Label label = new Label(messages.noAtomicServicesLabel());
		atomicServicesContainer.add(label);
	}
}