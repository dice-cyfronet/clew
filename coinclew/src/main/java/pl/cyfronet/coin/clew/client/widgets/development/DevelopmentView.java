package pl.cyfronet.coin.clew.client.widgets.development;

import pl.cyfronet.coin.clew.client.widgets.development.IDevelopmentView.IDevelopmentPresenter;

import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class DevelopmentView extends Composite implements IDevelopmentView, ReverseViewInterface<IDevelopmentPresenter> {
	private static DevelopmentViewUiBinder uiBinder = GWT.create(DevelopmentViewUiBinder.class);
	interface DevelopmentViewUiBinder extends UiBinder<Widget, DevelopmentView> {}
	
	private IDevelopmentPresenter presenter;
	
	@UiField FlowPanel atomicServicesContainer;
	@UiField FlowPanel runningInstancesContainer;
	@UiField DevelopmentMessages messages;

	public DevelopmentView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("manageUserKeys")
	void manageUserKeysClicked(ClickEvent event) {
		getPresenter().onManageUserKeysClicked();
	}
	
	@Override
	public void setPresenter(IDevelopmentPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IDevelopmentPresenter getPresenter() {
		return presenter;
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