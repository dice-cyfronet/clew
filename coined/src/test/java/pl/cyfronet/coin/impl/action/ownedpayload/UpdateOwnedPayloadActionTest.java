package pl.cyfronet.coin.impl.action.ownedpayload;

import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.beans.OwnedPayload;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.Action;

public abstract class UpdateOwnedPayloadActionTest extends
		OwnedPayloadActionTest {

	private String newPayload = "newPayload";
	private String differentUser = "wojtek";
	private List<String> newOwners = Arrays.asList("newUser1", "newUser2");
	private Action<Class<Void>> action;

	@Test
	public void shouldUpdateExistingSecurityPolicy() throws Exception {
		givenOwnedPayloadStoredInAirOwnerNotImportant();
		whenUpdatingSecurityPolicy();
		thenPolicyWasUpdated();
	}

	private void whenUpdatingSecurityPolicy() {
		whenUpdatingOwnedPayload(username);
	}

	private void whenUpdatingOwnedPayload(String username) {
		OwnedPayload newPolicy = new OwnedPayload();
		newPolicy.setPayload(newPayload);
		newPolicy.setOwners(newOwners);
		action = getMethodProvider().createUpdateOwnedPayloadAction(username,
				policyName, newPolicy);
		action.execute();
	}

	private void thenPolicyWasUpdated() {
		// data for rollback
		getMethodProvider().verifyGetOwnedPayload(1, null, policyName);
		getMethodProvider().verifyUpdateOwnedPayload(1, username, policyName,
				newPayload, newOwners);
	}

	@Test
	public void shouldUpdateOnlyPolicyPayload() throws Exception {
		givenOwnedPayloadStoredInAirOwnerNotImportant();
		whenUpdatingSecurityPolicyPayload();
		thenPolicyPayloadUpdated();
	}

	private void whenUpdatingSecurityPolicyPayload() {
		OwnedPayload newPolicy = new OwnedPayload();
		newPolicy.setPayload(newPayload);
		action = getMethodProvider().createUpdateOwnedPayloadAction(username,
				policyName, newPolicy);
		action.execute();
	}

	private void thenPolicyPayloadUpdated() {
		// data for rollback
		getMethodProvider().verifyGetOwnedPayload(1, null, policyName);
		getMethodProvider().verifyUpdateOwnedPayload(1, username, policyName,
				newPayload, Arrays.asList(username));
	}

	@Test
	public void shouldUpdateOnlyOwners() throws Exception {
		givenOwnedPayloadStoredInAirOwnerNotImportant();
		whenUpdatingSecurityPolicyOwners();
		thenOnlySecurityPolicyOwnersUpdated();
	}

	private void whenUpdatingSecurityPolicyOwners() {
		OwnedPayload newPolicy = new OwnedPayload();
		newPolicy.setOwners(newOwners);
		action = getMethodProvider().createUpdateOwnedPayloadAction(username,
				policyName, newPolicy);
		action.execute();
	}

	private void thenOnlySecurityPolicyOwnersUpdated() {
		// data for rollback
		getMethodProvider().verifyGetOwnedPayload(1, null, policyName);
		getMethodProvider().verifyUpdateOwnedPayload(1, username, policyName,
				policyText, newOwners);
	}

	@Test
	public void shouldUpdateAndRollback() throws Exception {
		givenOwnedPayloadStoredInAirOwnerNotImportant();
		whenUpdatingSecurityPolicyAndRollback();
		thenPolicyWasUpdatedAndRollbacked();
	}

	private void whenUpdatingSecurityPolicyAndRollback() {
		whenUpdatingSecurityPolicy();
		action.rollback();
	}

	private void thenPolicyWasUpdatedAndRollbacked() {
		thenPolicyWasUpdated();
		getMethodProvider().verifyUpdateOwnedPayload(1, username, policyName,
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

	private void givenAirWithoutAskedPolicy(String username) {
		getMethodProvider().mockGetOwnedPayload(username, null,
				new ArrayList<NamedOwnedPayload>());
	}

	@Test
	public void shouldThrow403WhenUserIsNotOwnersList() throws Exception {
		givenPolicyNotBelongingToTheUser();
		try {
			whenUpdatingOwnedPayload(differentUser);
			fail();
		} catch (NotAllowedException e) {
			// OK should be thrown.
		}
	}

	private void givenPolicyNotBelongingToTheUser() {
		givenOwnedPayloadStoredInAirOwnerNotImportant();
		getMethodProvider().throwUpdateOwnedPayloadException(404,
				differentUser, policyName, newPayload, newOwners);
	}
}
