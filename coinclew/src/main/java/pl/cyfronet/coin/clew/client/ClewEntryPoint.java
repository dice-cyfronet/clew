package pl.cyfronet.coin.clew.client;

import org.fusesource.restygwt.client.Defaults;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.Mvp4gModule;

public class ClewEntryPoint implements EntryPoint {
	@Override
	public void onModuleLoad() {
		CloudFacadeOverrideProperties cloudFacadeEndpointProperty = new CloudFacadeOverrideProperties();
		Defaults.setServiceRoot(cloudFacadeEndpointProperty.getCloudFacadeEndpoint());
		Defaults.ignoreJsonNulls();
		
		Mvp4gModule module = (Mvp4gModule) GWT.create(Mvp4gModule.class);
		module.createAndStartModule();
		
		ClewProperties clewProperties = GWT.create(ClewProperties.class);
		RootPanel rootPanel = RootPanel.get(clewProperties.getDashboardContainerId());
		
		if(rootPanel == null) {
			rootPanel = RootPanel.get();
		}
		
		rootPanel.add((Widget) module.getStartView());
	}
}