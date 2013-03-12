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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.SecurityPolicyService;
import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.AlreadyExistsException;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.securitypolicy.DeleteSecurityPolicyAction;
import pl.cyfronet.coin.impl.action.securitypolicy.GetSecurityPolicyAction;
import pl.cyfronet.coin.impl.action.securitypolicy.GetSecurityPolicyPayloadAction;
import pl.cyfronet.coin.impl.action.securitypolicy.ListSecurityPoliciesAction;
import pl.cyfronet.coin.impl.action.securitypolicy.NewSecurityPolicyAction;

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
	private DeleteSecurityPolicyAction deleteAction;
	private String policyNameWithNamespace = "my/namespace/policyname";
	private NamedOwnedPayload givenOwnedPayload;
	private NamedOwnedPayload ownedPayload;
	private NewSecurityPolicyAction newPolicyAction;
	private NamedOwnedPayload givenNewPolicy;

	private String username = "User123";

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
		policies = securityPolicyClient.list();
	}

	private void then3SecurityPolicyNamesReturned() {
		assertEquals(policies, givenPolicies);
	}

	@Test
	public void shouldGetExistingSecurityPolicy() throws Exception {
		givenSecurityPolicy();
		whenGetSecurityPolicy();
		thenSecurityPolicyReceived();
	}

	private void givenSecurityPolicy() {
		GetSecurityPolicyAction action = mock(GetSecurityPolicyAction.class);
		givenOwnedPayload = new NamedOwnedPayload();
		givenOwnedPayload.setOwners(Arrays.asList("user1", "user2"));
		givenOwnedPayload.setPayload("payload");
		givenOwnedPayload.setName(policyName);

		when(action.execute()).thenReturn(givenOwnedPayload);
		when(actionFactory.createGetSecurityPolicyAction(policyName))
				.thenReturn(action);
	}

	private void whenGetSecurityPolicy() {
		ownedPayload = securityPolicyClient.get(policyName);
	}

	private void thenSecurityPolicyReceived() {
		assertEquals(givenOwnedPayload.getOwners(), ownedPayload.getOwners());
		assertEquals(givenOwnedPayload.getPayload(), ownedPayload.getPayload());
	}

	@Test
	public void shouldGetExistingSecurityPolicyPayload() throws Exception {
		givenSecurityPolicyPayload();
		whenGetSecurityPolicyPayload();
		thenSecurityPolicyPayloadReceived();
	}

	private void givenSecurityPolicyPayload() {
		GetSecurityPolicyPayloadAction action = mock(GetSecurityPolicyPayloadAction.class);
		when(action.execute()).thenReturn(givenSecurityPolicyContent);
		when(actionFactory.createGetSecurityPolicyPayloadAction(policyName))
				.thenReturn(action);
	}

	private void whenGetSecurityPolicyPayload() {
		securityPolicyContent = securityPolicyClient.getPayload(policyName);
	}

	private void thenSecurityPolicyPayloadReceived() {
		assertEquals(securityPolicyContent, givenSecurityPolicyContent);
	}

	@Test
	public void should404BeThrownWhenGettingNonExistingSecurityPolicy()
			throws Exception {
		givenNonExistingSecurityPolicy();
		try {
			whenGetSecurityPolicyPayload();
			fail("Security policy not found exception should be thrown");
		} catch (NotFoundException e) {
			// OK should be thrown
		}

	}

	private void givenNonExistingSecurityPolicy() {
		GetSecurityPolicyPayloadAction action = mock(GetSecurityPolicyPayloadAction.class);
		when(action.execute()).thenThrow(new NotFoundException());
		when(actionFactory.createGetSecurityPolicyPayloadAction(policyName))
				.thenReturn(action);
	}

	@Test
	public void shouldShouldAddNewSecurityPolicy() throws Exception {
		givenNewSecurityPolicy();
		whenAddingSecurityPolicy();
		thenNewSecurityPolicyAdded();
	}

	private void givenNewSecurityPolicy() {
		newPolicyAction = mock(NewSecurityPolicyAction.class);
		givenNewPolicy = new NamedOwnedPayload();
		givenNewPolicy.setName(policyName);
		givenNewPolicy.setPayload(securityPolicyContent);
		givenNewPolicy.setOwners(Arrays.asList("user1", "user2"));

		when(
				actionFactory.createNewSecurityPolicyAction(username,
						givenNewPolicy)).thenReturn(newPolicyAction);
	}

	private void whenAddingSecurityPolicy() {
		securityPolicyClient.create(givenNewPolicy);
	}

	private void thenNewSecurityPolicyAdded() {
		verify(newPolicyAction, times(1)).execute();
	}

	@Test
	public void shouldThrowExceptionWhileAddingSecurityProxyWithoutUniqueName()
			throws Exception {
		givenExistingSecurityPolicy();
		try {
			whenAddingSecurityPolicyWithNonUniqueName();
			fail("Security policy already exists exception should be thrown");
		} catch (AlreadyExistsException e) {
			// Ok should be thrown
		}
	}

	private void givenExistingSecurityPolicy() {
		givenNewSecurityPolicy();
		when(newPolicyAction.execute()).thenThrow(new AlreadyExistsException());
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
		when(
				actionFactory.createDeleteSecurityPolicyAction(username,
						policyName)).thenReturn(deleteAction);
	}

	private void whenDeleteSecurityPolicy() {
		securityPolicyClient.delete(policyName);
	}

	private void thenSecurityPolicyRemoved() {
		verify(deleteAction, times(1)).execute();
	}

	@Test
	public void shouldThrow404WhenDeletingNonExistingSecurityPolicy()
			throws Exception {
		givenEmptySecurityPoliciesList();
		try {
			whenDeleteSecurityPolicy();
			fail("Security policy not found exception should be thrown");
		} catch (NotFoundException e) {
			// OK should be thrown
		}
	}

	private void givenEmptySecurityPoliciesList() {
		givenDeleteThrowsException(new NotFoundException());
	}

	private void givenDeleteThrowsException(Exception e) {
		deleteAction = mock(DeleteSecurityPolicyAction.class);
		when(deleteAction.execute()).thenThrow(e);
		when(
				actionFactory.createDeleteSecurityPolicyAction(username,
						policyName)).thenReturn(deleteAction);
	}
	
	@Test
	public void shouldThrow403WhileDeletingNotOwnedSecurityPolicy() throws Exception {
		givenSecurityPolicyOwnedByOtherUser();
		try {
			whenDeleteSecurityPolicy();
			fail("Security policy not owned by the user, exception should be thrown");
		} catch (NotAllowedException e) {
			// OK should be thrown
		}
	}
	
	private void givenSecurityPolicyOwnedByOtherUser() {
		givenDeleteThrowsException(new NotAllowedException());		
	}

	@Test
	public void shouldAddSecurityPolicyWithNameSpace() throws Exception {
		givenEmptySecurityPoliciesWaitingForSecPolicyWithNamespace();
		whenAddingSecurityPolicyWithNamespace();
		thenSecurityPolicyWithNamespaceAdded();
	}

	private void givenEmptySecurityPoliciesWaitingForSecPolicyWithNamespace() {
		givenNewSecurityPolicy();
		givenNewPolicy.setName(policyNameWithNamespace);
		when(
				actionFactory.createNewSecurityPolicyAction(username,
						givenNewPolicy)).thenReturn(newPolicyAction);
	}

	private void whenAddingSecurityPolicyWithNamespace() {
		securityPolicyClient.create(givenNewPolicy);
	}

	private void thenSecurityPolicyWithNamespaceAdded() {
		verify(actionFactory, times(1)).createNewSecurityPolicyAction(username,
				givenNewPolicy);
	}

	@Test
	public void shouldRemoveSecurityPolicyWithNamespace() throws Exception {
		givenSecurityPolicyWithNamespaceForDelete();
		whenDeleteSecurityPolicyWithNamespace();
		thenSecurityWithNamespaceRemoved();
	}

	private void givenSecurityPolicyWithNamespaceForDelete() {
		deleteAction = mock(DeleteSecurityPolicyAction.class);
		when(
				actionFactory.createDeleteSecurityPolicyAction(username,
						policyNameWithNamespace)).thenReturn(deleteAction);
	}

	private void whenDeleteSecurityPolicyWithNamespace() {
		securityPolicyClient.delete(policyNameWithNamespace);
	}

	private void thenSecurityWithNamespaceRemoved() {
		verify(actionFactory, times(1)).createDeleteSecurityPolicyAction(
				username, policyNameWithNamespace);
	}

	@Test
	public void shouldGetSecurityPolicyWithNamespace() throws Exception {
		givenSecurityPolicyWithNamespace();
		whenGetSecurityPolicyWithNamespace();
		thenSecurityPolicyWithNamespacePayloadReceived();
	}

	private void givenSecurityPolicyWithNamespace() {
		securityPolicyContent = null;
		GetSecurityPolicyPayloadAction action = mock(GetSecurityPolicyPayloadAction.class);
		when(action.execute()).thenReturn(givenSecurityPolicyContent);
		when(
				actionFactory
						.createGetSecurityPolicyPayloadAction(policyNameWithNamespace))
				.thenReturn(action);
	}

	private void whenGetSecurityPolicyWithNamespace() {
		securityPolicyContent = securityPolicyClient
				.getPayload(policyNameWithNamespace);
	}

	private void thenSecurityPolicyWithNamespacePayloadReceived() {
		assertEquals(securityPolicyContent, givenSecurityPolicyContent);
	}
}