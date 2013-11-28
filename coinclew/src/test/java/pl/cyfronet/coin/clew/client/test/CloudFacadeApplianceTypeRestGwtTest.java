package pl.cyfronet.coin.clew.client.test;

import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypeService;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypesResponse;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;

public class CloudFacadeApplianceTypeRestGwtTest extends GWTTestCase {
	private ApplianceTypeService applianceTypes;
	
	@Override
	public String getModuleName() {
		return "pl.cyfronet.coin.clew.clew";
	}
	
	@Override
	protected void gwtSetUp() throws Exception {
		Defaults.setServiceRoot(ClewGwtTestSuite.CLOUD_FACADE_URL);
		applianceTypes = GWT.create(ApplianceTypeService.class);
	}

	public void testApplianceTypesRetrieval() {
		applianceTypes.getApplianceTypes(new MethodCallback<ApplianceTypesResponse>() {
			@Override
			public void onSuccess(Method method, ApplianceTypesResponse response) {
				assertNotNull(response);
				assertNotNull(response.getApplianceTypes());
				finishTest();
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				System.err.println("Error: " + exception.getMessage());
				System.err.println("Response error: " + method.getResponse().getText());
				fail();
				finishTest();
			}
		});
		delayTestFinish(60000);
	}
}