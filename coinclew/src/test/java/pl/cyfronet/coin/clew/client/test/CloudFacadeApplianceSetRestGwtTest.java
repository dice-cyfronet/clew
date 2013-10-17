package pl.cyfronet.coin.clew.client.test;

import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import pl.cyfronet.coin.clew.client.controller.cf.applianceset.ApplianceSet;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.ApplianceSetRequestResponse;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.ApplianceSetService;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.ApplianceSetsResponse;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.NewApplianceSet;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.NewApplianceSet.Type;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.NewApplianceSetRequest;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;

public class CloudFacadeApplianceSetRestGwtTest extends GWTTestCase {
	private ApplianceSetService applianceSets;

	@Override
	public String getModuleName() {
		return "pl.cyfronet.coin.clew.clew";
	}
	
	@Override
	protected void gwtSetUp() throws Exception {
		Defaults.setServiceRoot("http://localhost:3000/api/v1");
		applianceSets = GWT.create(ApplianceSetService.class);
	}
	
	public void testApplianceSetsRetrieval() {
		applianceSets.getApplianceSets(new MethodCallback<ApplianceSetsResponse>() {
			@Override
			public void onSuccess(Method method, ApplianceSetsResponse response) {
				assertNotNull(response);
				assertNotNull(response.getApplianceSets());
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
	
	public void testAddDeleteApplianceset() {
		NewApplianceSet applianceSet = new NewApplianceSet();
		applianceSet.setName(String.valueOf(System.currentTimeMillis()));
		applianceSet.setType(Type.workflow);
		applianceSet.setPriority("50");
		
		NewApplianceSetRequest applianceSetRequest = new NewApplianceSetRequest();
		applianceSetRequest.setApplianceSet(applianceSet);
		applianceSets.addApplianceSet(applianceSetRequest, new MethodCallback<ApplianceSetRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				System.err.println("Error: " + exception.getMessage());
				System.err.println("Response error: " + method.getResponse().getText());
				fail();
				finishTest();
			}

			@Override
			public void onSuccess(Method method, ApplianceSetRequestResponse response) {
				assertNotNull(response);
				assertNotNull(response.getApplianceSet());
				assertNotNull(response.getApplianceSet().getId());
				checkDelete(response.getApplianceSet());
			}
		});
	}

	private void checkDelete(final ApplianceSet applianceSet) {
		applianceSets.deleteApplianceSet(applianceSet.getId(), new MethodCallback<Void>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				System.err.println("Error: " + exception.getMessage());
				System.err.println("Response error: " + method.getResponse().getText());
				fail();
				finishTest();
			}

			@Override
			public void onSuccess(Method method, Void response) {
				applianceSets.getApplianceSets(new MethodCallback<ApplianceSetsResponse>() {
					@Override
					public void onFailure(Method method, Throwable exception) {
						System.err.println("Error: " + exception.getMessage());
						System.err.println("Response error: " + method.getResponse().getText());
						fail();
						finishTest();
					}

					@Override
					public void onSuccess(Method method, ApplianceSetsResponse response) {
						assertNotNull(response);
						assertNotNull(response.getApplianceSets());
						
						for (ApplianceSet ap : response.getApplianceSets()) {
							if (ap.getName().equals(applianceSet.getName())) {
								fail("Appliance set with name " + applianceSet.getName() + " was not properly deleted");
							}
						}
						
						finishTest();
					}
				});
			}
		});
	}
}