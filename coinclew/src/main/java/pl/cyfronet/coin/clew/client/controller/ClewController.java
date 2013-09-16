package pl.cyfronet.coin.clew.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.clew.client.ClewProperties;
import pl.cyfronet.coin.clew.client.widgets.dashboard.DashboardPresenter;
import pl.cyfronet.coin.clew.client.widgets.dev.DevPresenter;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;

public class ClewController {
	private static final Logger log = LoggerFactory.getLogger(ClewController.class);
	
	private DashboardPresenter dashboardPresenter;
	private ClewProperties properties;
	private DevPresenter devPresenter;
	
	@Inject
	public ClewController(DashboardPresenter dashboardPresenter, ClewProperties properties,
			DevPresenter devPresenter) {
		this.dashboardPresenter = dashboardPresenter;
		this.properties = properties;
		this.devPresenter = devPresenter;
	}
	
	public void start() {
		if (RootPanel.get(properties.getDashboardContainerId()) != null) {
			RootPanel.get(properties.getDashboardContainerId()).add(dashboardPresenter.getWidget());
			dashboardPresenter.load();
		}
		
		if (RootPanel.get(properties.getDevContainerId()) != null) {
			RootPanel.get(properties.getDevContainerId()).add(devPresenter.getWidget());
			devPresenter.load();
		}

		log.info("CLEW application successfully loaded");
	}
}