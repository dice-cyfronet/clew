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
package pl.cyfronet.coin.impl.action;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.exception.SecurityPolicyNotFoundException;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class DeleteSecurityPolicyActionTest extends ActionTest {

	private String policyName = "myPolicy";
	private String policyText = "roles=my_policy";

	@Test
	public void shouldDeleteExistingSecurityPolicy() throws Exception {
		givenSecurityPolicyStoredInAir();
		whenDeleteSecurityPolicy();
		thenCheckIfAirActionWasInvoked();
	}

	private void givenSecurityPolicyStoredInAir() {
		when(air.getSecurityPolicy(policyName)).thenReturn(policyText);
	}

	private void whenDeleteSecurityPolicy() {
		Action<Class<Void>> action = actionFactory
				.createDeleteSecurityPolicyAction(policyName);
		action.execute();
	}

	private void thenCheckIfAirActionWasInvoked() {
		verify(air, times(1)).getSecurityPolicy(policyName);
		verify(air, times(1)).deleteSecurityPolicy(policyName);
	}

	@Test
	public void shouldThrownExceptionWhileDeletingNonExistingSecurityPolicy()
			throws Exception {
		givenAirWithoutSecurityPolicy();
		try {
			whenDeleteSecurityPolicy();
			fail();
		} catch (SecurityPolicyNotFoundException e) {
			thenValidateIfAirWasAskedAboutSecurityPolicy();
		}
	}

	private void givenAirWithoutSecurityPolicy() {
		when(air.getSecurityPolicy(policyName)).thenThrow(getAirException(400));
	}

	private void thenValidateIfAirWasAskedAboutSecurityPolicy() {
		verify(air, times(1)).getSecurityPolicy(policyName);
		verify(air, times(0)).deleteSecurityPolicy(policyName);
	}

	@Test
	public void shouldRollback() throws Exception {
		givenAirDeleteAndUploadMockConfigured();
		whenDeleteSecurityPolicyAndRollback();
		thenDeleteActionIsRollbacked();

	}

	private void givenAirDeleteAndUploadMockConfigured() {
		when(air.getSecurityPolicy(policyName)).thenReturn(policyText);
	}

	private void whenDeleteSecurityPolicyAndRollback() {
		Action<Class<Void>> action = actionFactory
				.createDeleteSecurityPolicyAction(policyName);
		action.execute();
		action.rollback();
	}

	private void thenDeleteActionIsRollbacked() {
		verify(air, times(1)).getSecurityPolicy(policyName);
		verify(air, times(1)).deleteSecurityPolicy(policyName);
		verify(air, times(1)).uploadSecurityPolicy(policyName, policyText,
				false);
	}
}
