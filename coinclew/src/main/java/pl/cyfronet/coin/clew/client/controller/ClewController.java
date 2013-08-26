package pl.cyfronet.coin.clew.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.clew.client.widgets.dashboard.DashboardPresenter;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;

public class ClewController {
	private static final Logger log = LoggerFactory.getLogger(ClewController.class);
	
	private DashboardPresenter dashboardPresenter;
	
	@Inject
	public ClewController(DashboardPresenter dashboardPresenter) {
		this.dashboardPresenter = dashboardPresenter;
	}
	
	public void start() {
		RootPanel.get().add(dashboardPresenter.getWidget());
		dashboardPresenter.load();
		log.info("CLEW application successfully loaded");
	}
}