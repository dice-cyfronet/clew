package pl.cyfronet.coin.impl.action;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.exception.SecurityPolicyNotFoundException;

public class GetSecurityPolicyActionTest extends ActionTest {

	private String policyName = "policyName";
	private String nonExistingPolicyName = "NonExisting";
	private String policyPayload = "roles=my_role";
	private String loadedPolicyPayload;

	@Test
	public void shouldGetExistingPolicy() throws Exception {
		givenSecurityProxiesStoredInAir();
		whenGetExistingPolicyName();
		thenReceivedPolicyPayload();
	}

	private void givenSecurityProxiesStoredInAir() {
		when(air.getSecurityPolicy(policyName)).thenReturn(policyPayload);
		when(air.getSecurityPolicy(nonExistingPolicyName)).thenThrow(
				new SecurityPolicyNotFoundException());
	}

	private void whenGetExistingPolicyName() {
		loadSecurityPolicy(policyName);
	}

	private void loadSecurityPolicy(String policyName) {
		GetSecurityPolicyAction action = actionFactory
				.createGetSecurityPolicyAction(policyName);
		loadedPolicyPayload = action.execute();
	}

	private void thenReceivedPolicyPayload() {
		assertEquals(loadedPolicyPayload, policyPayload);
		thenVerifyAirAskedAboutPolicy();
	}

	private void thenVerifyAirAskedAboutPolicy() {
		verify(air, times(1)).getSecurityPolicy(anyString());
	}

	@Test
	public void shouldGetNonExistingPolicy() throws Exception {
		givenSecurityProxiesStoredInAir();
		try {
			whenGetNonExistingSecurityPolicy();
			fail("Exception should be thrown when getting non existing security policy");
		} catch (SecurityPolicyNotFoundException e) {
			thenVerifyAirAskedAboutPolicy();
		}
	}

	private void whenGetNonExistingSecurityPolicy() throws Exception {
		loadSecurityPolicy(nonExistingPolicyName);
	}
}
