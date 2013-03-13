package pl.cyfronet.coin.impl.action.ownedpayload;

import java.util.ArrayList;
import java.util.Arrays;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.impl.action.ActionTest;

public abstract class OwnedPayloadActionTest extends ActionTest {

	protected String username = "marek";
	protected String policyName = "myPolicy";
	protected String policyText = "roles=my_policy";

	protected void givenSecurityPolicyStoredInAir() {
		givenSecurityPolicyStoredInAir(username);
	}

	protected void givenOwnedPayloadStoredInAirOwnerNotImportant() {
		givenSecurityPolicyStoredInAir(null);
	}

	private void givenSecurityPolicyStoredInAir(String requestUsername) {
		NamedOwnedPayload policy = getPayload(username);
		getMethodProvider().mockGetOwnedPayload(requestUsername, policyName,
				Arrays.asList(policy));
	}

	protected NamedOwnedPayload getPayload(String... owners) {
		NamedOwnedPayload policy = new NamedOwnedPayload();
		policy.setName(policyName);
		policy.setOwners(Arrays.asList(owners));
		policy.setPayload(policyText);

		return policy;
	}

	protected void givenAirWithoutAskedOwnedPayload(String username) {
		getMethodProvider().mockGetOwnedPayload(username, policyName,
				new ArrayList<NamedOwnedPayload>());
	}

	protected abstract MethodProvider getMethodProvider();
}
