/*
 * Copyright 2013 ACC CYFRONET AGH
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

package pl.cyfronet.coin.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.SecurityPolicyService;
import pl.cyfronet.coin.api.exception.SecurityPolicyAlreadyExistException;
import pl.cyfronet.coin.api.exception.SecurityPolicyNotFoundException;
import pl.cyfronet.coin.impl.action.DeleteSecurityPolicyAction;
import pl.cyfronet.coin.impl.action.GetSecurityPolicyAction;
import pl.cyfronet.coin.impl.action.ListSecurityPoliciesAction;
import pl.cyfronet.coin.impl.action.UploadSecurityPolicyAction;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
//@formatter:off
@ContextConfiguration( locations={
		"classpath:rest-test-properties-securitypolicy.xml",
		"classpath:rest-test-imports.xml",
		"classpath:rest-test-mocks.xml",
		"classpath:rest-test-securitypolicy-client.xml",
		"classpath:META-INF/spring/rest-services.xml"
	} )
//@formatter:on
public class SecurityPolicyServiceTest extends AbstractServiceTest {

	@Autowired
	@Qualifier("securityPolicyClient")
	private SecurityPolicyService securityPolicyClient;
	private List<String> policies;
	private List<String> givenPolicies;
	private String givenSecurityPolicyContent = "security policy content";
	private String securityPolicyContent;
	private String policyName = "securityPolicyName";
	private UploadSecurityPolicyAction uploadAction;
	private DeleteSecurityPolicyAction deleteAction;
	private String policyNameWithNamespace = "my/namespace/policyname";

	@Test
	public void shouldListSecurityPolicies() throws Exception {
		given3SecurityPolicies();
		whenGetSecurityPlicyNames();
		then3SecurityPolicyNamesReturned();
	}

	private void given3SecurityPolicies() {
		ListSecurityPoliciesAction action = mock(ListSecurityPoliciesAction.class);
		givenPolicies = Arrays.asList("a", "b", "c");
		when(action.execute()).thenReturn(givenPolicies);
		when(actionFactory.createListSecurityPoliciesAction()).thenReturn(
				action);
	}

	private void whenGetSecurityPlicyNames() {
		policies = securityPolicyClient.getPoliciesNames();
	}

	private void then3SecurityPolicyNamesReturned() {
		assertEquals(policies, givenPolicies);
	}

	@Test
	public void shouldGetExistingSecurityPolicy() throws Exception {
		givenSecurityPolicy();
		whenGetSecurityPolicy();
		thenSecurityPolicyContentReceived();
	}

	private void givenSecurityPolicy() {
		GetSecurityPolicyAction action = mock(GetSecurityPolicyAction.class);
		when(action.execute()).thenReturn(givenSecurityPolicyContent);
		when(actionFactory.createGetSecurityPolicyAction(policyName))
				.thenReturn(action);
	}

	private void whenGetSecurityPolicy() {
		securityPolicyContent = securityPolicyClient
				.getSecurityPolicy(policyName);
	}

	private void thenSecurityPolicyContentReceived() {
		assertEquals(securityPolicyContent, givenSecurityPolicyContent);
	}

	@Test
	public void should404BeThrownWhenGettingNonExistingSecurityPolicy()
			throws Exception {
		givenNonExistingSecurityPolicy();
		try {
			whenGetSecurityPolicy();
			fail("Security policy not found exception should be thrown");
		} catch (SecurityPolicyNotFoundException e) {
			// OK should be thrown
		}

	}

	private void givenNonExistingSecurityPolicy() {
		GetSecurityPolicyAction action = mock(GetSecurityPolicyAction.class);
		when(action.execute()).thenThrow(new SecurityPolicyNotFoundException());
		when(actionFactory.createGetSecurityPolicyAction(policyName))
				.thenReturn(action);
	}

	@Test
	public void shouldShouldAddNewSecurityPolicy() throws Exception {
		givenNewSecurityPolicy();
		whenAddingSecurityPolicy();
		thenNewSecurityPolicyAdded();
	}

	private void givenNewSecurityPolicy() {
		uploadAction = mock(UploadSecurityPolicyAction.class);
		when(
				actionFactory.createUploadSecurityPolicyAction(policyName,
						givenSecurityPolicyContent, false)).thenReturn(
				uploadAction);
	}

	private void whenAddingSecurityPolicy() {
		securityPolicyClient.updateSecurityPolicy(policyName,
				givenSecurityPolicyContent, false);
	}

	private void thenNewSecurityPolicyAdded() {
		verify(uploadAction, times(1)).execute();
	}

	@Test
	public void shouldThrowExceptionWhileAddingSecurityProxyWithNonUniqueNameWithoutForce()
			throws Exception {
		givenExistingSecurityPolicy();
		try {
			whenAddingSecurityPolicyWithNonUniqueName();
			fail("Security policy already exists exception should be thrown");
		} catch (SecurityPolicyAlreadyExistException e) {
			// Ok should be thrown
		}
	}

	private void givenExistingSecurityPolicy() {
		uploadAction = mock(UploadSecurityPolicyAction.class);
		when(uploadAction.execute()).thenThrow(
				new SecurityPolicyAlreadyExistException());
		when(
				actionFactory.createUploadSecurityPolicyAction(policyName,
						givenSecurityPolicyContent, false)).thenReturn(
				uploadAction);
	}

	private void whenAddingSecurityPolicyWithNonUniqueName() {
		whenAddingSecurityPolicy();
	}

	@Test
	public void shouldDeleteExistingSecurityPolicy() throws Exception {
		givenSecurityPolicyToDelete();
		whenDeleteSecurityPolicy();
		thenSecurityPolicyRemoved();
	}

	private void givenSecurityPolicyToDelete() {
		deleteAction = mock(DeleteSecurityPolicyAction.class);
		when(actionFactory.createDeleteSecurityPolicyAction(policyName))
				.thenReturn(deleteAction);
	}

	private void whenDeleteSecurityPolicy() {
		securityPolicyClient.deleteSecurityPolicy(policyName);
	}

	private void thenSecurityPolicyRemoved() {
		verify(deleteAction, times(1)).execute();
	}

	@Test
	public void shouldThrow404WhenDeletingNonExistingSecurityPolicy()
			throws Exception {
		givenEmptySecurityPoliciesList();
		try {
			whenDeleteNonexistingSecurityPolicy();
			fail("Security policy not found exception should be thrown");
		} catch (SecurityPolicyNotFoundException e) {
			// OK should be thrown
		}
	}

	private void givenEmptySecurityPoliciesList() {
		deleteAction = mock(DeleteSecurityPolicyAction.class);
		when(deleteAction.execute()).thenThrow(
				new SecurityPolicyNotFoundException());
		when(actionFactory.createDeleteSecurityPolicyAction(policyName))
				.thenReturn(deleteAction);
	}

	private void whenDeleteNonexistingSecurityPolicy() {
		whenDeleteSecurityPolicy();
	}
}