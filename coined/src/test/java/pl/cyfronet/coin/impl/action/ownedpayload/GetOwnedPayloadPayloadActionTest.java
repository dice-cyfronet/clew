package pl.cyfronet.coin.impl.action.ownedpayload;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.ArrayList;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.Action;

public abstract class GetOwnedPayloadPayloadActionTest extends
		OwnedPayloadActionTest {

	private String nonExistingPolicyName = "NonExisting";
	private String loadedPolicyPayload;

	@Test
	public void shouldGetExistingPolicyPayload() throws Exception {
		givenSecurityProxiesStoredInAir();
		whenGetExistingPolicyName();
		thenReceivedPolicyPayload();
	}

	private void givenSecurityProxiesStoredInAir() {
		givenOwnedPayloadStoredInAirOwnerNotImportant();
		getMethodProvider().mockGetOwnedPayload(null, nonExistingPolicyName,
				new ArrayList<NamedOwnedPayload>());
	}

	private void whenGetExistingPolicyName() {
		loadSecurityPolicy(policyName);
	}

	private void loadSecurityPolicy(String policyName) {
		Action<String> action = getMethodProvider()
				.getOwnedPayloadPayloadAction(policyName);
		loadedPolicyPayload = action.execute();
	}

	private void thenReceivedPolicyPayload() {
		assertEquals(loadedPolicyPayload, policyText);
		getMethodProvider().thenVerifyAirAskedAboutOwnedPayload();
	}

	@Test
	public void shouldGetNonExistingPolicy() throws Exception {
		givenSecurityProxiesStoredInAir();
		try {
			whenGetNonExistingSecurityPolicy();
			fail("Exception should be thrown when getting non existing security policy");
		} catch (NotFoundException e) {
			getMethodProvider().thenVerifyAirAskedAboutOwnedPayload();
		}
	}

	private void whenGetNonExistingSecurityPolicy() throws Exception {
		loadSecurityPolicy(nonExistingPolicyName);
	}
}
