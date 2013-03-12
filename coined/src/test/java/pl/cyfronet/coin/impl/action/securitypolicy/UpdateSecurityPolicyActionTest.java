package pl.cyfronet.coin.impl.action.securitypolicy;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.OwnedPayload;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.Action;

public class UpdateSecurityPolicyActionTest extends SecurityPolicyActionTest {

	private String newPayload = "newPayload";
	private String differentUser = "wojtek";
	private List<String> newOwners = Arrays.asList("newUser1", "newUser2");
	private Action<Class<Void>> action;

	@Test
	public void shouldUpdateExistingSecurityPolicy() throws Exception {
		givenSecurityPolicyStoredInAirOwnerNotImportant();
		whenUpdatingSecurityPolicy();
		thenPolicyWasUpdated();
	}

	private void whenUpdatingSecurityPolicy() {
		whenUpdatingSecurityPolicy(username);
	}

	private void whenUpdatingSecurityPolicy(String username) {
		OwnedPayload newPolicy = new OwnedPayload();
		newPolicy.setPayload(newPayload);
		newPolicy.setOwners(newOwners);
		action = actionFactory.createUpdateSecurityPolicyAction(username,
				policyName, newPolicy);
		action.execute();
	}

	private void thenPolicyWasUpdated() {
		// data for rollback
		verify(air, times(1)).getSecurityPolicies(null, policyName);
		verify(air, times(1)).updateSecurityPolicy(username, policyName,
				newPayload, newOwners);
	}

	@Test
	public void shouldUpdateOnlyPolicyPayload() throws Exception {
		givenSecurityPolicyStoredInAirOwnerNotImportant();
		whenUpdatingSecurityPolicyPayload();
		thenPolicyPayloadUpdated();
	}

	private void whenUpdatingSecurityPolicyPayload() {
		OwnedPayload newPolicy = new OwnedPayload();
		newPolicy.setPayload(newPayload);
		action = actionFactory.createUpdateSecurityPolicyAction(username,
				policyName, newPolicy);
		action.execute();
	}

	private void thenPolicyPayloadUpdated() {
		// data for rollback
		verify(air, times(1)).getSecurityPolicies(null, policyName);
		verify(air, times(1)).updateSecurityPolicy(username, policyName,
				newPayload, Arrays.asList(username));
	}

	@Test
	public void shouldUpdateOnlyOwners() throws Exception {
		givenSecurityPolicyStoredInAirOwnerNotImportant();
		whenUpdatingSecurityPolicyOwners();
		thenOnlySecurityPolicyOwnersUpdated();
	}

	private void whenUpdatingSecurityPolicyOwners() {
		OwnedPayload newPolicy = new OwnedPayload();
		newPolicy.setOwners(newOwners);
		action = actionFactory.createUpdateSecurityPolicyAction(username,
				policyName, newPolicy);
		action.execute();
	}

	private void thenOnlySecurityPolicyOwnersUpdated() {
		// data for rollback
		verify(air, times(1)).getSecurityPolicies(null, policyName);
		verify(air, times(1)).updateSecurityPolicy(username, policyName,
				policyText, newOwners);
	}

	@Test
	public void shouldUpdateAndRollback() throws Exception {
		givenSecurityPolicyStoredInAirOwnerNotImportant();
		whenUpdatingSecurityPolicyAndRollback();
		thenPolicyWasUpdatedAndRollbacked();
	}

	private void whenUpdatingSecurityPolicyAndRollback() {
		whenUpdatingSecurityPolicy();
		action.rollback();
	}

	private void thenPolicyWasUpdatedAndRollbacked() {
		thenPolicyWasUpdated();
		verify(air, times(1)).updateSecurityPolicy(username, policyName,
				policyText, Arrays.asList(username));
	}

	@Test
	public void shouldThrow404WhenPolicyNotFound() throws Exception {
		givenAirWithoutAskedPolicy(username);
		try {
			whenUpdatingSecurityPolicy();
			fail();
		} catch (NotFoundException e) {
			// OK should be thrown.
		}
	}

	@Test
	public void shouldThrow403WhenUserIsNotOwnersList() throws Exception {
		givenPolicyNotBelongingToTheUser();
		try {
			whenUpdatingSecurityPolicy(differentUser);
			fail();
		} catch (NotAllowedException e) {
			// OK should be thrown.
		}
	}

	private void givenPolicyNotBelongingToTheUser() {
		givenSecurityPolicyStoredInAirOwnerNotImportant();
		doThrow(getAirException(404)).when(air).updateSecurityPolicy(
				differentUser, policyName, newPayload, newOwners);
	}
}
