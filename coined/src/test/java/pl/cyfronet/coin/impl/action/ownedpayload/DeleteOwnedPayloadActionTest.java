package pl.cyfronet.coin.impl.action.ownedpayload;

import static org.testng.Assert.fail;

import java.util.Arrays;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.Action;

public abstract class DeleteOwnedPayloadActionTest extends
		OwnedPayloadActionTest {

	private Action<Class<Void>> action;
	private String differentUser = "wojtek";

	@Test
	public void shouldDeleteExistingOwnedPayload() throws Exception {
		givenOwnedPayloadStoredInAirOwnerNotImportant();
		whenDeleteOwnedPayload();
		thenCheckIfAirActionWasInvoked();
	}

	private void whenDeleteOwnedPayload() {
		whenDeleteOwnedPayload(username);
	}

	private void whenDeleteOwnedPayload(String username) {
		action = getMethodProvider().getDeleteOwnedPayloadAction(username,
				policyName);
		action.execute();
	}

	private void thenCheckIfAirActionWasInvoked() {
		getMethodProvider().verifyGetOwnedPayload(1, null, policyName);
		getMethodProvider().verifyOwnedPayloadDeleted(1, username, policyName);
	}

	@Test
	public void shouldThrownExceptionWhileDeletingNonExistingOwnedPayload()
			throws Exception {
		givenAirWithoutOwnedPayload();
		try {
			whenDeleteOwnedPayload();
			fail();
		} catch (NotFoundException e) {
			thenValidateIfAirWasAskedAboutOwnedPayload();
		}
	}

	private void givenAirWithoutOwnedPayload() {
		givenAirWithoutAskedOwnedPayload(null);
	}

	private void thenValidateIfAirWasAskedAboutOwnedPayload() {
		getMethodProvider().verifyGetOwnedPayload(1, null, policyName);
		getMethodProvider().verifyOwnedPayloadDeleted(0, username, policyName);
	}

	@Test
	public void shouldRollback() throws Exception {
		givenOwnedPayloadStoredInAirOwnerNotImportant();
		whenDeleteOwnedPayloadAndRollback();
		thenDeleteActionIsRollbacked();

	}

	private void whenDeleteOwnedPayloadAndRollback() {
		whenDeleteOwnedPayload();
		action.rollback();
	}

	private void thenDeleteActionIsRollbacked() {
		getMethodProvider().verifyGetOwnedPayload(1, null, policyName);
		getMethodProvider().verifyOwnedPayloadDeleted(1, username, policyName);
		getMethodProvider().verifyAddOwnedPayload(1, policyName, policyText,
				Arrays.asList(username));
	}

	@Test
	public void shouldThrow403WhileDeletingNotOwnedPolicy() throws Exception {
		givenSecurityPolicyNotBelongingToTheUser();
		try {
			whenDeleteOwnedPayload(differentUser);
			fail();
		} catch (NotAllowedException e) {
			// OK should be thrown.
		}
	}

	private void givenSecurityPolicyNotBelongingToTheUser() {
		givenOwnedPayloadStoredInAirOwnerNotImportant();
		getMethodProvider().throwDeleteOwnedPayloadException(404,
				differentUser, policyName);
	}
}
