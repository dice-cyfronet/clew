package pl.cyfronet.coin.impl.action.ownedpayload;

import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.AlreadyExistsException;
import pl.cyfronet.coin.impl.action.Action;

public abstract class NewOwnedPayloadActionTest extends OwnedPayloadActionTest {

	private String username = "marek";
	private Action<Class<Void>> action;

	@Test
	public void shouldCreateNewSecurityPolicyWithoutOwner() throws Exception {
		// if no owner is set in form than owner should be set into user which
		// perform the request.
		givenEmptySecurityPolicyListStoredInAir();
		whenUploadSecurityPolicy();
		thenCheckIfPolicyWasAdded(username);
	}

	private void givenEmptySecurityPolicyListStoredInAir() {
		getMethodProvider().mockOwnedPayloadNotExistsInAir(policyName);
	}

	private void whenUploadSecurityPolicy(String... owners) {
		NamedOwnedPayload newPolicy = getPayload(owners);

		action = getMethodProvider().getNewOwnedPayloadAction(username,
				newPolicy);
		action.execute();
	}

	private void thenCheckIfPolicyWasAdded(String... owners) {
		List<String> o = owners == null ? null : Arrays.asList(owners);
		getMethodProvider().verifyAddOwnedPayload(1, policyName, policyText, o);
	}

	@Test
	public void shouldAddNewSecurityPolicyWithOwners() throws Exception {
		String[] owners = new String[] { "userA", "userB" };
		givenEmptySecurityPolicyListStoredInAir();
		whenUploadSecurityPolicy(owners);
		thenCheckIfPolicyWasAdded(owners);

	}

	@Test
	public void shouldThrowExceptionWhileUploadingExistingSecurityPolicy()
			throws Exception {
		givenAirWithOneExistingSecurityPolicy();
		try {
			whenAddingNewSecurityPolicyWithNameAlreadyExisted();
			fail();
		} catch (AlreadyExistsException e) {
			thenCheckAirRequest();
		}
	}

	private void givenAirWithOneExistingSecurityPolicy() {
		getMethodProvider().throwExceptionWhileAddingOwnedPayload(400,
				policyName, policyText, Arrays.asList(username));
	}

	private void whenAddingNewSecurityPolicyWithNameAlreadyExisted() {
		whenUploadSecurityPolicy();
	}

	private void thenCheckAirRequest() {
		getMethodProvider().verifyAddOwnedPayload(1, policyName, policyText,
				Arrays.asList(username));
	}

	@Test
	public void shouldRollbackAddSecurityPolicy() throws Exception {
		givenAirEmptyAndThenWithAddedSecurityPolicy();
		whenAddAndRollback();
		thenSecurityPolicyAddedAndRollbacked();

	}

	private void givenAirEmptyAndThenWithAddedSecurityPolicy() {
		NamedOwnedPayload oldPolicy = new NamedOwnedPayload();
		oldPolicy.setName(policyName);
		getMethodProvider().mockGetOwnedPayload(null, policyName,
				Arrays.asList(oldPolicy));
	}

	private void whenAddAndRollback() {
		whenUploadSecurityPolicy();
		action.rollback();
	}

	private void thenSecurityPolicyAddedAndRollbacked() {
		getMethodProvider().verifyAddOwnedPayload(1, policyName, policyText,
				Arrays.asList(username));
		// delete action rollback
		getMethodProvider().verifyGetOwnedPayload(1, null, policyName);
		getMethodProvider().verifyOwnedPayloadDeleted(1, username, policyName);
	}
}
