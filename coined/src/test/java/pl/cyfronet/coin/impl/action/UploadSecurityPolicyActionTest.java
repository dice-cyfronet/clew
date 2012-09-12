package pl.cyfronet.coin.impl.action;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.exception.SecurityPolicyAlreadyExistException;

public class UploadSecurityPolicyActionTest extends ActionTest {

	private String policyName = "myPolicy";
	private String policyText = "roles=new_role";
	private String oldPolicyText = "roles=old_role";
	
	@Test
	public void shouldCreateNewSecurityPolicy() throws Exception {
		givenEmptySecurityPolicyListStoredInAir();
		whenUploadSecurityPolicy();
		thenCheckIfPolicyWasAdded();
	}

	private void givenEmptySecurityPolicyListStoredInAir() {
		when(air.getSecurityPolicy(anyString())).thenThrow(getAirException(400));
	}

	private void whenUploadSecurityPolicy() {
		UploadSecurityPolicyAction action = actionFactory
				.createUploadSecurityPolicyAction(policyName, policyText, false);
		action.execute();
	}

	private void thenCheckIfPolicyWasAdded() {
		// verify that previous security policy is not taken from air.
		verify(air, times(0)).getSecurityPolicy(policyName);
		verify(air, times(1)).uploadSecurityPolicy(policyName, policyText,
				false);
	}

	@Test
	public void shouldThrowExceptionWhileUploadingExistingSecurityPolicy()
			throws Exception {
		givenAirWithOneExistingSecurityPolicy();
		try {
			whenAddingNewSecurityPolicyWithNameAlreadyExisted();
			fail();
		} catch (SecurityPolicyAlreadyExistException e) {
			thenCheckAirRequest();
		}
	}

	private void givenAirWithOneExistingSecurityPolicy() {
		when(air.getSecurityPolicy(policyName)).thenReturn(oldPolicyText);
	}

	private void whenAddingNewSecurityPolicyWithNameAlreadyExisted() {
		whenUploadSecurityPolicy();
	}

	private void thenCheckAirRequest() {
		
	}

	@Test(enabled = false)
	public void shouldUpdateExistingSecurityPolicy() throws Exception {
		givenAirWithOneExistingSecurityPolicy();
		whenUpdatingSecurityPolicy();
		thenPolicyWasUpdated();
	}

	private void whenUpdatingSecurityPolicy() {
		// TODO Auto-generated method stub

	}

	private void thenPolicyWasUpdated() {
		fail();
	}

	// TODO tests for rollback
}
