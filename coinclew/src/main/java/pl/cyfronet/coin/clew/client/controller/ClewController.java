package pl.cyfronet.coin.clew.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.clew.client.ClewProperties;
import pl.cyfronet.coin.clew.client.widgets.dashboard.DashboardPresenter;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;

public class ClewController {
	private static final Logger log = LoggerFactory.getLogger(ClewController.class);
	
	private DashboardPresenter dashboardPresenter;
	private ClewProperties properties;
	
	@Inject
	public ClewController(DashboardPresenter dashboardPresenter, ClewProperties properties) {
		this.dashboardPresenter = dashboardPresenter;
		this.properties = properties;
	}
	
	public void start() {
		RootPanel.get(properties.getContainerId()).add(dashboardPresenter.getWidget());
		dashboardPresenter.load();
		log.info("CLEW application successfully loaded");
	}
}