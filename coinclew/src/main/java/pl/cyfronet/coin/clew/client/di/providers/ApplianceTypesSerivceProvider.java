package pl.cyfronet.coin.clew.client.di.providers;

import org.fusesource.restygwt.client.Defaults;

import pl.cyfronet.coin.clew.client.ClewProperties;
import pl.cyfronet.coin.clew.client.controller.cf.ApplianceTypesService;

import com.google.gwt.core.shared.GWT;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ApplianceTypesSerivceProvider implements Provider<ApplianceTypesService> {
	private ClewProperties clewProperties;
	
	@Inject
	public ApplianceTypesSerivceProvider(ClewProperties clewProperties) {
		this.clewProperties = clewProperties;
	}
	
	@Override
	public ApplianceTypesService get() {
		Defaults.setServiceRoot(clewProperties.getCloudFacadeRootUrl());
		
		return GWT.create(ApplianceTypesService.class);
	}
}