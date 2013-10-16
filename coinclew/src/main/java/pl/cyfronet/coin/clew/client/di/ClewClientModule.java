package pl.cyfronet.coin.clew.client.di;

import pl.cyfronet.coin.clew.client.controller.ClewController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypesService;
import pl.cyfronet.coin.clew.client.di.providers.ApplianceTypesSerivceProvider;
import pl.cyfronet.coin.clew.client.widgets.dashboard.DashboardPanel;
import pl.cyfronet.coin.clew.client.widgets.dashboard.DashboardPresenter;
import pl.cyfronet.coin.clew.client.widgets.dev.DevPanel;
import pl.cyfronet.coin.clew.client.widgets.dev.DevPresenter;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class ClewClientModule extends AbstractGinModule {
	@Override
	protected void configure() {
		bind(ClewController.class).in(Singleton.class);
		bind(CloudFacadeController.class).in(Singleton.class);
		bind(ApplianceTypesService.class).toProvider(ApplianceTypesSerivceProvider.class).in(Singleton.class);
		
		configureViewsAndPresenters();
	}

	private void configureViewsAndPresenters() {
		bind(DashboardPresenter.View.class).to(DashboardPanel.class).in(Singleton.class);
		bind(pl.cyfronet.coin.clew.client.widgets.dashboard.Presenter.class).to(DashboardPresenter.class).in(Singleton.class);
		
		bind(DevPresenter.View.class).to(DevPanel.class).in(Singleton.class);
		bind(pl.cyfronet.coin.clew.client.widgets.dev.Presenter.class).to(DevPresenter.class).in(Singleton.class);
	}
}