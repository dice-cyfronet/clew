package pl.cyfronet.coin.clew.client.test;

import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import pl.cyfronet.coin.clew.client.controller.cf.userkey.NewUserKey;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.NewUserKeyRequest;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.UserKey;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.UserKeyRequestResponse;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.UserKeyService;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.UserKeysResponse;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;

public class CloudFacadeUserKeyRestGwtTest extends GWTTestCase {
	private UserKeyService userKeys;
	
	@Override
	public String getModuleName() {
		return "pl.cyfronet.coin.clew.clew";
	}
	
	@Override
	protected void gwtSetUp() throws Exception {
		Defaults.setServiceRoot(ClewGwtTestSuiteIgnore.CLOUD_FACADE_URL);
		userKeys = GWT.create(UserKeyService.class);
	}
	
	public void testUserKeysRetrieval() {
		userKeys.getUserKeys(new MethodCallback<UserKeysResponse>() {
			@Override
			public void onSuccess(Method method, UserKeysResponse response) {
				assertNotNull(response);
				assertNotNull(response.getUserKeys());
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
	
	public void testAddDeleteUserKey() {
		NewUserKey userKey = new NewUserKey();
		userKey.setName("user key " + System.currentTimeMillis());
		userKey.setPublicKey("ssh-dss AAAAB3NzaC1kc3MAAACBAIMXosuvnOWtUhDa8cFCe/wln3ouZ0oYBVzT4Pt869VQVt0SzwzcFiXJRHiv8nUxSLA0RO7knBMIRWnCis8LVunnLwDzMfHqzYT8Gbescc5NTBrMEkF26finxpsVufR1Wq08Dx+L7FdsFmqzhr74F0o2K/6HC3yOutLYFcFLJFFNAAAAFQCsuqVOq1SfoKo29Cas89eeaoByMQAAAIBAtFrYPFe2y6xayLmAG324tfm9kXMEoqQ4QiJ6IQXARSTLnI+Njqqi86XKn/vsK5kT3LPjGAsCoj8kRpKkeCmwUty07fmEl9kKKBJkogAGKPSP7Kq2QX31PkwnjADt755lS+twlTxUUxPX2zlrqlbRhSPeBByAoODlJy+FekNIMgAAAIAIam60MscWQjtsRD2fWfn1D4VJypTe2GBdWRPUXpysiFQsyVCxVcTWbsUSSSKi8Ai/Clc2K5w0iK0tB6B67EW2im46iYsxqarF56Nx6skVlK1UAKYHesEBmxxMy00ha4Qfegp8Dqxa+Pk683fZpal237bVxNFteU9QdJtiswhDLQ==");
		
		NewUserKeyRequest userKeyRequest = new NewUserKeyRequest();
		userKeyRequest.setUserKey(userKey);
		userKeys.addUserKey(userKeyRequest, new MethodCallback<UserKeyRequestResponse>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				System.err.println("Error: " + exception.getMessage());
				System.err.println("Response error: " + method.getResponse().getText());
				fail();
				finishTest();
			}

			@Override
			public void onSuccess(Method method, UserKeyRequestResponse response) {
				assertNotNull(response);
				assertNotNull(response.getUserKey());
				assertNotNull(response.getUserKey().getId());
				checkDelete(response.getUserKey());
			}
		});
		delayTestFinish(60000);
	}

	private void checkDelete(final UserKey userKey) {
		userKeys.deleteUserKey(userKey.getId(), new MethodCallback<Void>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				System.err.println("Error: " + exception.getMessage());
				System.err.println("Response error: " + method.getResponse().getText());
				fail();
				finishTest();
			}

			@Override
			public void onSuccess(Method method, Void response) {
				userKeys.getUserKeys(new MethodCallback<UserKeysResponse>() {

					@Override
					public void onFailure(Method method, Throwable exception) {
						System.err.println("Error: " + exception.getMessage());
						System.err.println("Response error: " + method.getResponse().getText());
						fail();
						finishTest();
					}

					@Override
					public void onSuccess(Method method, UserKeysResponse response) {
						assertNotNull(response);
						assertNotNull(response.getUserKeys());
						
						for (UserKey uk : response.getUserKeys()) {
							if (uk.getName().equals(userKey.getName())) {
								fail("User key with name " + userKey.getName() + " was not properly deleted");
							}
						}
						
						finishTest();
					}
				});
			}
		});
	}
}