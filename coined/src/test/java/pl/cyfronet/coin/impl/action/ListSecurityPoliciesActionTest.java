package pl.cyfronet.coin.impl.action;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

import pl.cyfronet.coin.impl.air.client.SecurityPolicy;

public class ListSecurityPoliciesActionTest extends ActionTest {

	private List<String> policies;

	@Test
	public void shouldGetSecurityPolicies() throws Exception {
		givenAirWith2SecurityPolicies();
		whenListSecurityPolicies();
		thenTwoPoliciesLoaded();
	}

	private void givenAirWith2SecurityPolicies() {
		SecurityPolicy policy1 = getSecurityPolicy(1);
		SecurityPolicy policy2 = getSecurityPolicy(2);

		when(air.getSecurityPolicies()).thenReturn(
				Arrays.asList(policy1, policy2));
	}

	private SecurityPolicy getSecurityPolicy(int nr) {
		SecurityPolicy policy1 = new SecurityPolicy();
		policy1.setId("id" + nr);
		policy1.setPolicy_name("name" + nr);
		return policy1;
	}

	private void whenListSecurityPolicies() {
		ListSecurityPoliciesAction action = actionFactory
				.createListSecurityPoliciesAction();
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
		when(air.getSecurityPolicies()).thenReturn(
				new ArrayList<SecurityPolicy>());
	}

	private void thenEmptySecurityPoliciesListLoaded() {
		assertNotNull(policies);
		assertEquals(policies.size(), 0);
	}
}
