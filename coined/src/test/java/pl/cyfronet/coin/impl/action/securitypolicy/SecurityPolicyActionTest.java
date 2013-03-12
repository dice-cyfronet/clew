package pl.cyfronet.coin.impl.action.securitypolicy;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.impl.action.ActionTest;

public class SecurityPolicyActionTest extends ActionTest {

	protected String username = "marek";
	protected String policyName = "myPolicy";
	protected String policyText = "roles=my_policy";
	
	protected void givenSecurityPolicyStoredInAir() {
		givenSecurityPolicyStoredInAir(username);
	}
	
	protected void givenSecurityPolicyStoredInAirOwnerNotImportant() {
		givenSecurityPolicyStoredInAir(null);
	}
	
	private void givenSecurityPolicyStoredInAir(String requestUsername) {
		NamedOwnedPayload policy = getPayload(username);

		when(air.getSecurityPolicies(requestUsername, policyName)).thenReturn(
				Arrays.asList(policy));
	}
	
	protected NamedOwnedPayload getPayload(String... owners) {
		NamedOwnedPayload policy = new NamedOwnedPayload();
		policy.setName(policyName);
		policy.setOwners(Arrays.asList(owners));
		policy.setPayload(policyText);

		return policy;
	}
	
	protected void givenAirWithoutAskedPolicy(String username) {
		when(air.getSecurityPolicies(username, policyName)).thenReturn(
				new ArrayList<NamedOwnedPayload>());
	}
}
