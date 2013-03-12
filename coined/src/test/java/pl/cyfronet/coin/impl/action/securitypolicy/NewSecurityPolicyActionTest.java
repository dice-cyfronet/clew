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

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.AlreadyExistsException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class NewSecurityPolicyActionTest extends ActionTest {

	private String username = "marek";
	private String policyName = "myPolicy";
	private String policyText = "roles=new_role";
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
		when(air.getSecurityPolicies(eq(username), anyString())).thenThrow(
				getAirException(400));
	}

	private void whenUploadSecurityPolicy(String... owners) {
		NamedOwnedPayload newPolicy = new NamedOwnedPayload();
		newPolicy.setName(policyName);
		newPolicy.setPayload(policyText);
		newPolicy.setOwners(Arrays.asList(owners));

		action = actionFactory.createNewSecurityPolicyAction(username,
				newPolicy);
		action.execute();
	}

	private void thenCheckIfPolicyWasAdded(String... owners) {
		List<String> o = owners == null ? null : Arrays.asList(owners);
		verify(air, times(1)).addSecurityPolicy(policyName, policyText, o);
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
		doThrow(new ServerWebApplicationException(Response.status(400).build()))
				.when(air).addSecurityPolicy(policyName, policyText,
						Arrays.asList(username));
	}

	private void whenAddingNewSecurityPolicyWithNameAlreadyExisted() {
		whenUploadSecurityPolicy();
	}

	private void thenCheckAirRequest() {
		verify(air, times(1)).addSecurityPolicy(eq(policyName), anyString(),
				anyListOf(String.class));
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

		when(air.getSecurityPolicies(null, policyName)).thenReturn(
				Arrays.asList(oldPolicy));
	}

	private void whenAddAndRollback() {
		whenUploadSecurityPolicy();
		action.rollback();
	}

	private void thenSecurityPolicyAddedAndRollbacked() {
		verify(air, times(1)).addSecurityPolicy(policyName, policyText,
				Arrays.asList(username));
		// delete action rollback
		verify(air, times(1)).getSecurityPolicies(null, policyName);
		verify(air, times(1)).deleteSecurityPolicy(username, policyName);
	}
}
