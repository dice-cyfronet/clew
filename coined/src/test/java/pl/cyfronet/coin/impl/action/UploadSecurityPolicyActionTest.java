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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.exception.SecurityPolicyAlreadyExistException;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class UploadSecurityPolicyActionTest extends ActionTest {

	private String policyName = "myPolicy";
	private String policyText = "roles=new_role";
	private String oldPolicyText = "roles=old_role";
	private Action<Class<Void>> action;

	@Test
	public void shouldCreateNewSecurityPolicy() throws Exception {
		givenEmptySecurityPolicyListStoredInAir();
		whenUploadSecurityPolicy();
		thenCheckIfPolicyWasAdded();
	}

	private void givenEmptySecurityPolicyListStoredInAir() {
		when(air.getSecurityPolicy(anyString()))
				.thenThrow(getAirException(400));
	}

	private void whenUploadSecurityPolicy() {
		whenUploadSecurityPolicy(false);
	}

	private void whenUploadSecurityPolicy(boolean overwrite) {
		action = actionFactory.createUploadSecurityPolicyAction(policyName,
				policyText, overwrite);
		action.execute();
	}

	private void thenCheckIfPolicyWasAdded() {
		// verify that previous security policy is not taken from air when
		// overwrite is false.
		verify(air, times(0)).getSecurityPolicy(policyName);
		verify(air, times(1)).uploadSecurityPolicy(policyName, policyText,
				false);
	}

	@Test
	public void shouldThrowExceptionWhileUploadingExistingSecurityPolicy()
			throws Exception {
		givenAirWithOneExistingSecurityPolicy();
		try {
			whenAddingNewSecurityPolicyWithNameAlreadyExisted();
			fail();
		} catch (SecurityPolicyAlreadyExistException e) {
			thenCheckAirRequest();
		}
	}

	private void givenAirWithOneExistingSecurityPolicy() {
		when(air.getSecurityPolicy(policyName)).thenReturn(oldPolicyText);
		doThrow(new ServerWebApplicationException(Response.status(400).build()))
				.when(air).uploadSecurityPolicy(policyName, policyText, false);
	}

	private void whenAddingNewSecurityPolicyWithNameAlreadyExisted() {
		whenUploadSecurityPolicy();
	}

	private void thenCheckAirRequest() {
		// verify that previous security policy is not taken from air when
		// overwrite is false.
		verify(air, times(0)).getSecurityPolicy(policyName);
		verify(air, times(1)).uploadSecurityPolicy(policyName, policyText,
				false);
	}

	@Test
	public void shouldUpdateExistingSecurityPolicy() throws Exception {
		givenAirWithOneExistingSecurityPolicy();
		whenUpdatingSecurityPolicy();
		thenPolicyWasUpdated();
	}

	private void whenUpdatingSecurityPolicy() {
		whenUploadSecurityPolicy(true);
	}

	private void thenPolicyWasUpdated() {
		// data for rollback
		verify(air, times(1)).getSecurityPolicy(policyName);
		verify(air, times(1))
				.uploadSecurityPolicy(policyName, policyText, true);
	}

	@Test
	public void shouldRollbackUpload() throws Exception {
		givenAirEmptyAndThenWithAddedSecurityPolicy();
		whenUploadAndRollback();
		thenSecurityPolicyUploadedAndRollbacked();

	}

	private void givenAirEmptyAndThenWithAddedSecurityPolicy() {
		when(air.getSecurityPolicy(policyName)).thenReturn(oldPolicyText);
	}

	private void whenUploadAndRollback() {
		whenUploadAndRollback(false);
	}

	private void whenUploadAndRollback(boolean overwrite) {
		whenUploadSecurityPolicy(overwrite);
		action.rollback();
	}

	private void thenSecurityPolicyUploadedAndRollbacked() {
		verify(air, times(1)).uploadSecurityPolicy(policyName, policyText,
				false);
		// delete action rollback
		verify(air, times(1)).getSecurityPolicy(policyName);
		verify(air, times(1)).deleteSecurityPolicy(policyName);
	}

	@Test
	public void shouldRollbackUpdate() throws Exception {
		givenAirWithOneExistingSecurityPolicy();
		whenUpdateAndRollback();
		thenSecurityPolicyUpdatedAndRollbacked();
	}

	private void whenUpdateAndRollback() {
		whenUploadAndRollback(true);
	}

	private void thenSecurityPolicyUpdatedAndRollbacked() {
		// two times - one for first action rollback and second for rollback
		// rollback :)
		verify(air, times(2)).getSecurityPolicy(policyName);
		verify(air, times(1))
				.uploadSecurityPolicy(policyName, policyText, true);
		verify(air, times(1)).uploadSecurityPolicy(policyName, oldPolicyText,
				true);

	}
}
