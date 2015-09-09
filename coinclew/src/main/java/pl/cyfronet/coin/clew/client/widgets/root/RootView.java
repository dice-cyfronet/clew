package pl.cyfronet.coin.clew.client.widgets.root;

import pl.cyfronet.coin.clew.client.widgets.root.IRootView.IRootPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class RootView extends Composite implements IRootView, ReverseViewInterface<IRootPresenter> {
	private static RootViewUiBinder uiBinder = GWT.create(RootViewUiBinder.class);
	interface RootViewUiBinder extends UiBinder<Widget, RootView> {}
	
	private IRootPresenter presenter;
	
	@UiField FlowPanel menuPanel;
	@UiField FlowPanel bodyPanel;
	@UiField FlowPanel popups;
	@UiField HTML buildInfo;
	@UiField HTMLPanel globalProgress;
	
	PopupPanel errorPopup;
	HTML errorLabel;

	public RootView() {
		errorLabel = new HTML();
		errorLabel.addStyleName("alert alert-danger");
		errorLabel.getElement().getStyle().setMargin(10, Unit.PX);
		
		errorPopup = new PopupPanel();
		errorPopup.add(errorLabel);
		errorPopup.setAnimationEnabled(true);
		
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
		if(visible) {
			errorPopup.show();
		} else {
			errorPopup.hide();
		}
	}

	@Override
	public void setBuildInfo(String buildInfo) {
		this.buildInfo.setText(buildInfo);
	}

	@Override
	public void showStartApplicationLabel(boolean show) {
		globalProgress.setVisible(show);
	}
}