package pl.cyfronet.coin.clew.client.di;

import pl.cyfronet.coin.clew.client.controller.ClewController;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(ClewClientModule.class)
public interface ClewGinjector extends Ginjector {
	ClewController getClewController();
}