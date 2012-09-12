package pl.cyfronet.coin.impl.action;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.exception.SecurityPolicyNotFoundException;

public class DeleteSecurityPolicyActionTest extends ActionTest {

	private String policyName = "myPolicy";
	private String policyText = "roles=my_policy";

	@Test
	public void shouldDeleteExistingSecurityPolicy() throws Exception {
		givenSecurityPolicyStoredInAir();
		whenDeleteSecurityPolicy();
		thenCheckIfAirActionWasInvoked();
	}

	private void givenSecurityPolicyStoredInAir() {
		when(air.getSecurityPolicy(policyName)).thenReturn(policyText);
	}

	private void whenDeleteSecurityPolicy() {
		DeleteSecurityPolicyAction action = actionFactory
				.createDeleteSecurityPolicyAction(policyName);
		action.execute();
	}

	private void thenCheckIfAirActionWasInvoked() {
		verify(air, times(1)).getSecurityPolicy(policyName);
		verify(air, times(1)).deleteSecurityPolicy(policyName);
	}

	@Test
	public void shouldThrownExceptionWhileDeletingNonExistingSecurityPolicy()
			throws Exception {
		givenAirWithoutSecurityPolicy();
		try {
			whenDeleteSecurityPolicy();
			fail();
		} catch (SecurityPolicyNotFoundException e) {
			thenValidateIfAirWasAskedAboutSecurityPolicy();
		}
	}

	private void givenAirWithoutSecurityPolicy() {
		when(air.getSecurityPolicy(policyName)).thenThrow(
				new SecurityPolicyNotFoundException());
	}

	private void thenValidateIfAirWasAskedAboutSecurityPolicy() {
		verify(air, times(1)).getSecurityPolicy(policyName);
		verify(air, times(0)).deleteSecurityPolicy(policyName);
	}
	
	@Test
	public void shouldRollback() throws Exception {
		givenAirDeleteAndUploadMockConfigured();
		whenDeleteSecurityPolicyAndRollback();
		thenDeleteActionIsRollbacked();

	}

	private void givenAirDeleteAndUploadMockConfigured() {
 		when(air.getSecurityPolicy(policyName)).thenReturn(policyText);
	}

	private void whenDeleteSecurityPolicyAndRollback() {
		DeleteSecurityPolicyAction action = actionFactory
				.createDeleteSecurityPolicyAction(policyName);
		action.execute();
		action.rollback();
	}

	private void thenDeleteActionIsRollbacked() {
		verify(air, times(1)).getSecurityPolicy(policyName);
		verify(air, times(1)).deleteSecurityPolicy(policyName);
		verify(air, times(1)).uploadSecurityPolicy(policyName, policyText, false);
	}
}
