package pl.cyfronet.coin.impl.action.securitypolicy;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Arrays;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.Action;

public class GetSecurityPolicyActionTest extends SecurityPolicyActionTest {

	private Action<NamedOwnedPayload> action;
	private NamedOwnedPayload policy;

	@Test
	public void shouldGetSecurityPolicy() throws Exception {
		givenSecurityPolicyStoredInAirOwnerNotImportant();
		whenGetSecurityPolicy();
		thenPolicyLoaded();
	}

	private void whenGetSecurityPolicy() {
		action = actionFactory.createGetSecurityPolicyAction(policyName);
		policy = action.execute();
	}

	private void thenPolicyLoaded() {
		assertEquals(policy.getName(), policyName);
		assertEquals(policy.getPayload(), policyText);
		assertEquals(policy.getOwners(), Arrays.asList(username));
	}

	@Test
	public void shouldThrow404WhenPolicyNotFound() throws Exception {
		givenAirWithoutAskedPolicy(null);
		try {
			whenGetSecurityPolicy();
			fail();
		} catch (NotFoundException e) {
			// OK should be thrown.
		}
	}	
}
