package pl.cyfronet.coin.clew.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.Mvp4gModule;

public class ClewEntryPoint implements EntryPoint {
	@Override
	public void onModuleLoad() {
//		ClewGinjector injector = GWT.create(ClewGinjector.class);
//		ClewController clientController = injector.getClewController();
//		clientController.start();
		
		Mvp4gModule module = (Mvp4gModule) GWT.create(Mvp4gModule.class);
		module.createAndStartModule();
		RootPanel.get().add((Widget) module.getStartView());
	}
}