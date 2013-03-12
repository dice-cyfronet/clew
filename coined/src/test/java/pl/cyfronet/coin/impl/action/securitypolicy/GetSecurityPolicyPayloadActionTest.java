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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.ArrayList;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.Action;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetSecurityPolicyPayloadActionTest extends
		SecurityPolicyActionTest {

	private String nonExistingPolicyName = "NonExisting";
	private String loadedPolicyPayload;

	@Test
	public void shouldGetExistingPolicyPayload() throws Exception {
		givenSecurityProxiesStoredInAir();
		whenGetExistingPolicyName();
		thenReceivedPolicyPayload();
	}

	private void givenSecurityProxiesStoredInAir() {
		givenSecurityPolicyStoredInAirOwnerNotImportant();
		when(air.getSecurityPolicies(null, nonExistingPolicyName)).thenReturn(
				new ArrayList<NamedOwnedPayload>());
	}

	private void whenGetExistingPolicyName() {
		loadSecurityPolicy(policyName);
	}

	private void loadSecurityPolicy(String policyName) {
		Action<String> action = actionFactory
				.createGetSecurityPolicyPayloadAction(policyName);
		loadedPolicyPayload = action.execute();
	}

	private void thenReceivedPolicyPayload() {
		assertEquals(loadedPolicyPayload, policyText);
		thenVerifyAirAskedAboutPolicy();
	}

	private void thenVerifyAirAskedAboutPolicy() {
		verify(air, times(1)).getSecurityPolicies(anyString(), anyString());
	}

	@Test
	public void shouldGetNonExistingPolicy() throws Exception {
		givenSecurityProxiesStoredInAir();
		try {
			whenGetNonExistingSecurityPolicy();
			fail("Exception should be thrown when getting non existing security policy");
		} catch (NotFoundException e) {
			thenVerifyAirAskedAboutPolicy();
		}
	}

	private void whenGetNonExistingSecurityPolicy() throws Exception {
		loadSecurityPolicy(nonExistingPolicyName);
	}
}
