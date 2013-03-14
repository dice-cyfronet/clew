package pl.cyfronet.coin.impl.action.ownedpayload;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.impl.action.Action;

public abstract class ListOwnedPayloadActionTest extends OwnedPayloadActionTest {

	private List<String> policies;
	private String username = "marek";

	@Test
	public void shouldGetSecurityPolicies() throws Exception {
		givenAirWith2SecurityPolicies();
		whenListSecurityPolicies();
		thenTwoPoliciesLoaded();
	}

	private void givenAirWith2SecurityPolicies() {
		NamedOwnedPayload policy1 = getSecurityPolicy(1);
		NamedOwnedPayload policy2 = getSecurityPolicy(2);

		getMethodProvider().whenGetOwnedPayload(null, null,
				Arrays.asList(policy1, policy2));
	}

	private NamedOwnedPayload getSecurityPolicy(int nr) {
		NamedOwnedPayload policy1 = new NamedOwnedPayload();
		policy1.setName("name" + nr);
		policy1.setPayload("payload" + nr);
		policy1.setOwners(Arrays.asList(username));
		return policy1;
	}

	private void whenListSecurityPolicies() {
		Action<List<String>> action = getMethodProvider()
				.getListOwnedPayloadAction();
		policies = action.execute();
	}

	private void thenTwoPoliciesLoaded() {
		assertEquals(policies.toArray(), new String[] { "name1", "name2" });
	}

	@Test
	public void shouldGetEmptyPolicyList() throws Exception {
		givenEmptySecurityPoliciesStoredInAir();
		whenListSecurityPolicies();
		thenEmptySecurityPoliciesListLoaded();
	}

	private void givenEmptySecurityPoliciesStoredInAir() {
		getMethodProvider().whenGetOwnedPayload(null, null,
				new ArrayList<NamedOwnedPayload>());
	}

	private void thenEmptySecurityPoliciesListLoaded() {
		assertNotNull(policies);
		assertEquals(policies.size(), 0);
	}
}
