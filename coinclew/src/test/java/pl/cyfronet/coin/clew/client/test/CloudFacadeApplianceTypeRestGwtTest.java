package pl.cyfronet.coin.clew.client.test;

import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypeRequestResponse;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypesResponse;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypeService;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.NewApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.NewApplianceTypeRequest;

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
	
	public void testAddUpdateDeleteApplianceType() {
		final NewApplianceType newApplianceType = new NewApplianceType();
		newApplianceType.setName(String.valueOf(System.currentTimeMillis()));
		newApplianceType.setVisibleFor("all");
		
		NewApplianceTypeRequest applianceTypeRequest = new NewApplianceTypeRequest();
		applianceTypeRequest.setApplianceType(newApplianceType);
		applianceTypes.addApplianceType(applianceTypeRequest, new MethodCallback<ApplianceTypeRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				System.err.println("Error: " + exception.getMessage());
				System.err.println("Response error: " + method.getResponse().getText());
				fail();
				finishTest();
			}

			@Override
			public void onSuccess(Method method, ApplianceTypeRequestResponse applianceTypeResponse) {
				assertNotNull(applianceTypeResponse.getApplianceType());
				assertEquals(newApplianceType.getName(), applianceTypeResponse.getApplianceType().getName());
				assertEquals(newApplianceType.getVisibleFor(), applianceTypeResponse.getApplianceType().getVisibleFor());
				assertNull(applianceTypeResponse.getApplianceType().getDescription());
				checkUpdate(applianceTypeResponse.getApplianceType());
			}
		});
		delayTestFinish(60000);
	}

	private void checkUpdate(final ApplianceType applianceType) {
		final String description = "description" + String.valueOf(System.currentTimeMillis());
		applianceType.setDescription(description);
		
		ApplianceTypeRequestResponse applianceTypeRequest = new ApplianceTypeRequestResponse();
		applianceTypeRequest.setApplianceType(applianceType);
		applianceTypes.updateApplianceType(applianceType.getId(), applianceTypeRequest, new MethodCallback<ApplianceTypeRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				System.err.println("Error: " + exception.getMessage());
				System.err.println("Response error: " + method.getResponse().getText());
				fail();
				finishTest();
			}

			@Override
			public void onSuccess(Method method, ApplianceTypeRequestResponse applianceTypeResponse) {
				assertNotNull(applianceTypeResponse.getApplianceType());
				assertEquals(description, applianceTypeResponse.getApplianceType().getDescription());
				checkDelete(applianceType);
			}
		});
	}

	private void checkDelete(final ApplianceType applianceType) {
		applianceTypes.deleteApplianceType(applianceType.getId(), new MethodCallback<Void>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				System.err.println("Error: " + exception.getMessage());
				System.err.println("Response error: " + method.getResponse().getText());
				fail();
				finishTest();
			}

			@Override
			public void onSuccess(Method method, Void response) {
				applianceTypes.getApplianceTypes(new MethodCallback<ApplianceTypesResponse>() {
					@Override
					public void onFailure(Method method, Throwable exception) {
						System.err.println("Error: " + exception.getMessage());
						System.err.println("Response error: " + method.getResponse().getText());
						fail();
						finishTest();
					}

					@Override
					public void onSuccess(Method method, ApplianceTypesResponse response) {
						for (ApplianceType ap : response.getApplianceTypes()) {
							if (ap.getName().equals(applianceType.getName())) {
								fail("Appliance with name " + applianceType.getName() + " was not properly deleted");
							}
						}
						
						finishTest();
					}
				});
			}
		});
	}
}