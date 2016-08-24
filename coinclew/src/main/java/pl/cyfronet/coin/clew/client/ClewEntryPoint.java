package pl.cyfronet.coin.clew.client;

import org.fusesource.restygwt.client.Defaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.Mvp4gModule;

public class ClewEntryPoint implements EntryPoint {

	private static final Logger log = LoggerFactory.getLogger(ClewEntryPoint.class);

	@Override
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				log.error("Uncaught exception occurred", e);
			}
		});

		CloudFacadeOverrideProperties cloudFacadeEndpointProperty = new CloudFacadeOverrideProperties();
		Defaults.setServiceRoot(cloudFacadeEndpointProperty.getCloudFacadeEndpoint());

		Mvp4gModule module = (Mvp4gModule) GWT.create(Mvp4gModule.class);
		module.createAndStartModule();

		ClewProperties clewProperties = GWT.create(ClewProperties.class);
		RootPanel rootPanel = RootPanel.get(clewProperties.getDashboardContainerId());

		if (rootPanel == null) {
			rootPanel = RootPanel.get();
		}

		rootPanel.add((Widget) module.getStartView());
	}
}
