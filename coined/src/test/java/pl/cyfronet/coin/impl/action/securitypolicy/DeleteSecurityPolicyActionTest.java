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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.Action;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class DeleteSecurityPolicyActionTest extends SecurityPolicyActionTest {

	private Action<Class<Void>> action;
	private String differentUser = "wojtek";

	@Test
	public void shouldDeleteExistingSecurityPolicy() throws Exception {
		givenSecurityPolicyStoredInAirOwnerNotImportant();
		whenDeleteSecurityPolicy();
		thenCheckIfAirActionWasInvoked();
	}

	private void whenDeleteSecurityPolicy() {
		whenDeleteSecurityPolicy(username);
	}

	private void whenDeleteSecurityPolicy(String username) {
		action = actionFactory.createDeleteSecurityPolicyAction(username,
				policyName);
		action.execute();
	}

	private void thenCheckIfAirActionWasInvoked() {
		verify(air, times(1)).getSecurityPolicies(null, policyName);
		verify(air, times(1)).deleteSecurityPolicy(username, policyName);
	}

	@Test
	public void shouldThrownExceptionWhileDeletingNonExistingSecurityPolicy()
			throws Exception {
		givenAirWithoutSecurityPolicy();
		try {
			whenDeleteSecurityPolicy();
			fail();
		} catch (NotFoundException e) {
			thenValidateIfAirWasAskedAboutSecurityPolicy();
		}
	}

	private void givenAirWithoutSecurityPolicy() {
		when(air.getSecurityPolicies(null, policyName)).thenReturn(
				new ArrayList<NamedOwnedPayload>());
	}

	private void thenValidateIfAirWasAskedAboutSecurityPolicy() {
		verify(air, times(1)).getSecurityPolicies(null, policyName);
		verify(air, times(0)).deleteSecurityPolicy(username, policyName);
	}

	@Test
	public void shouldRollback() throws Exception {
		givenSecurityPolicyStoredInAirOwnerNotImportant();
		whenDeleteSecurityPolicyAndRollback();
		thenDeleteActionIsRollbacked();

	}

	private void whenDeleteSecurityPolicyAndRollback() {
		whenDeleteSecurityPolicy();
		action.rollback();
	}

	private void thenDeleteActionIsRollbacked() {
		verify(air, times(1)).getSecurityPolicies(null, policyName);
		verify(air, times(1)).deleteSecurityPolicy(username, policyName);
		verify(air, times(1)).addSecurityPolicy(policyName, policyText,
				Arrays.asList(username));
	}

	@Test
	public void shouldThrow403WhileDeletingNotOwnedPolicy() throws Exception {
		givenSecurityPolicyNotBelongingToTheUser();
		try {
			whenDeleteSecurityPolicy(differentUser);
			fail();
		} catch (NotAllowedException e) {
			// OK should be thrown.
		}
	}

	private void givenSecurityPolicyNotBelongingToTheUser() {
		givenSecurityPolicyStoredInAirOwnerNotImportant();
		doThrow(getAirException(404)).when(air).deleteSecurityPolicy(
				differentUser, policyName);
	}
}
