/*
 * Copyright 2012 ACC CYFRONET AGH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package pl.cyfronet.coin.impl.action.securitypolicy;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class ListSecurityPoliciesActionTest extends ActionTest {

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

		when(air.getSecurityPolicies(null, null)).thenReturn(
				Arrays.asList(policy1, policy2));
	}

	private NamedOwnedPayload getSecurityPolicy(int nr) {
		NamedOwnedPayload policy1 = new NamedOwnedPayload();
		policy1.setName("name" + nr);
		policy1.setPayload("payload" + nr);
		policy1.setOwners(Arrays.asList(username ));
		return policy1;
	}

	private void whenListSecurityPolicies() {
		Action<List<String>> action = actionFactory
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
		when(air.getSecurityPolicies(null, null)).thenReturn(
				new ArrayList<NamedOwnedPayload>());
	}

	private void thenEmptySecurityPoliciesListLoaded() {
		assertNotNull(policies);
		assertEquals(policies.size(), 0);
	}
}
