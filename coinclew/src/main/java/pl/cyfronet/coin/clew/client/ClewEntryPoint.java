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
		ClewProperties clewProperties = GWT.create(ClewProperties.class);
		Defaults.setServiceRoot(clewProperties.getCloudFacadeRootUrl());
		
		Mvp4gModule module = (Mvp4gModule) GWT.create(Mvp4gModule.class);
		module.createAndStartModule();
		RootPanel.get().add((Widget) module.getStartView());
	}
}