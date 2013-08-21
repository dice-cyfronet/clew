package pl.cyfronet.coin.clew.client.widgets.dashboard;

import pl.cyfronet.coin.clew.client.widgets.dashboard.DashboardPresenter.View;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class DashboardPanel extends Composite implements View {
	private static DashboardPanelUiBinder uiBinder = GWT.create(DashboardPanelUiBinder.class);
	interface DashboardPanelUiBinder extends UiBinder<Widget, DashboardPanel> {}
	
	private Provider<Presenter> presenter;

	@Inject
	public DashboardPanel(Provider<Presenter> presenter) {
		this.presenter = presenter;
		initWidget(uiBinder.createAndBindUi(this));
	}
}