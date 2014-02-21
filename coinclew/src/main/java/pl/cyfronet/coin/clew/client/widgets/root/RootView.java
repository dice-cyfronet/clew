package pl.cyfronet.coin.clew.client.widgets.root;

import pl.cyfronet.coin.clew.client.widgets.root.IRootView.IRootPresenter;

import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class RootView extends Composite implements IRootView, ReverseViewInterface<IRootPresenter> {
	private static RootViewUiBinder uiBinder = GWT.create(RootViewUiBinder.class);
	interface RootViewUiBinder extends UiBinder<Widget, RootView> {}
	
	private IRootPresenter presenter;
	
	@UiField FlowPanel menuPanel;
	@UiField FlowPanel bodyPanel;
	@UiField Label errorLabel;
	@UiField FlowPanel popups;
	@UiField HTML buildInfo;

	public RootView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(IRootPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IRootPresenter getPresenter() {
		return presenter;
	}

	@Override
	public void setMenu(IsWidget widget) {
		menuPanel.add(widget);
	}

	@Override
	public void setBody(IsWidget widget) {
		bodyPanel.clear();
		bodyPanel.add(widget);
	}

	@Override
	public HasText getErrorLabel() {
		return errorLabel;
	}

	@Override
	public void addPopup(IsWidget widget) {
		popups.add(widget);
	}

	@Override
	public void setErrorLabelVisible(boolean visible) {
		errorLabel.setVisible(visible);
	}

	@Override
	public void setBuildInfo(String buildInfo) {
		this.buildInfo.setText(buildInfo);
	}
}