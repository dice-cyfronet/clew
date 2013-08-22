package pl.cyfronet.coin.clew.client.di;

import pl.cyfronet.coin.clew.client.controller.ClewController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.widgets.dashboard.DashboardPanel;
import pl.cyfronet.coin.clew.client.widgets.dashboard.DashboardPresenter;
import pl.cyfronet.coin.clew.client.widgets.dashboard.Presenter;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class ClewClientModule extends AbstractGinModule {
	@Override
	protected void configure() {
		bind(ClewController.class).in(Singleton.class);
		bind(CloudFacadeController.class).in(Singleton.class);
		
		configureViewsAndPresenters();
	}

	private void configureViewsAndPresenters() {
		bind(DashboardPresenter.View.class).to(DashboardPanel.class);
		bind(Presenter.class).to(DashboardPresenter.class);
	}
}