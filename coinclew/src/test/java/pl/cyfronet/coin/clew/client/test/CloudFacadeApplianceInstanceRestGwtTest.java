package pl.cyfronet.coin.clew.client.test;

import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstanceService;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstancesResponse;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypesResponse;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;

public class CloudFacadeApplianceInstanceRestGwtTest extends GWTTestCase {
	private ApplianceInstanceService applianceInstances;
	
	@Override
	public String getModuleName() {
		return "pl.cyfronet.coin.clew.clew";
	}
	
	@Override
	protected void gwtSetUp() throws Exception {
		Defaults.setServiceRoot(ClewGwtTestSuiteIgnore.CLOUD_FACADE_URL);
		applianceInstances = GWT.create(ApplianceInstanceService.class);
	}
	
	public void testApplianceTypesRetrieval() {
		applianceInstances.getApplianceInstances(new MethodCallback<ApplianceInstancesResponse>() {
			@Override
			public void onSuccess(Method method, ApplianceInstancesResponse response) {
				assertNotNull(response);
				assertNotNull(response.getApplianceInstances());
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