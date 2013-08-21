package pl.cyfronet.coin.clew.client;

import pl.cyfronet.coin.clew.client.controller.ClewController;
import pl.cyfronet.coin.clew.client.di.ClewGinjector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class ClewEntryPoint implements EntryPoint {
	@Override
	public void onModuleLoad() {
		ClewGinjector injector = GWT.create(ClewGinjector.class);
		ClewController clientController = injector.getClewController();
		clientController.start();
	}
}