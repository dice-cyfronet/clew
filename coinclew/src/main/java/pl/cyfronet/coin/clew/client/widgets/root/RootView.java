package pl.cyfronet.coin.clew.client.widgets.root;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class RootView extends Composite implements IRootView, ReverseViewInterface<RootPresenter> {
	private static RootViewUiBinder uiBinder = GWT.create(RootViewUiBinder.class);
	interface RootViewUiBinder extends UiBinder<Widget, RootView> {}
	
	private RootPresenter presenter;
	
	@UiField FlowPanel menuPanel;
	@UiField FlowPanel bodyPanel;

	public RootView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(RootPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public RootPresenter getPresenter() {
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
}