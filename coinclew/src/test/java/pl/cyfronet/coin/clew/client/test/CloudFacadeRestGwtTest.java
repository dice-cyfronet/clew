package pl.cyfronet.coin.clew.client.test;

import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import pl.cyfronet.coin.clew.client.controller.cf.ApplianceTypesResponse;
import pl.cyfronet.coin.clew.client.controller.cf.ApplianceTypesService;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;

public class CloudFacadeRestGwtTest extends GWTTestCase {
	@Override
	public String getModuleName() {
		return "pl.cyfronet.coin.clew.clew";
	}

	public void testApplianceTypesRetrieval() {
		Defaults.setServiceRoot("http://localhost:3000/api/v1");
		ApplianceTypesService applianceTypes = GWT.create(ApplianceTypesService.class);
		applianceTypes.getApplianceTypes(new MethodCallback<ApplianceTypesResponse>() {
			@Override
			public void onSuccess(Method method, ApplianceTypesResponse response) {
				System.out.println("Response: " + response);
				finishTest();
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				System.err.println("Error: " + exception.getMessage());
				finishTest();
			}
		});
		delayTestFinish(60000);
	}
}