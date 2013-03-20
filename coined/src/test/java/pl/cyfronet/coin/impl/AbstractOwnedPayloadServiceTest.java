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
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.OwnedPayloadService;
import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.AlreadyExistsException;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ownedpayload.OwnedPayloadActionFactory;

@SuppressWarnings("unchecked")
public abstract class AbstractOwnedPayloadServiceTest extends
		AbstractServiceTest {

	@Autowired
	@Qualifier("client")
	private OwnedPayloadService client;

	private List<String> names;
	private List<String> givenPolicies;
	private String givenSecurityPolicyContent = "owned payload content";
	private String securityPolicyContent;
	private String policyName = "simpleName";
	private String policyNameWithNamespace = "my/namespace/name";
	private NamedOwnedPayload givenOwnedPayload;
	private NamedOwnedPayload ownedPayload;
	private NamedOwnedPayload givenNewOwnedPayload;

	private Action<Class<Void>> deleteAction;
	private Action<Class<Void>> newOwnedPayloadAction;

	private String username = "User123";

	@Override
	protected void postSetUp() {
		OwnedPayloadActionFactory ownedPayloadActionFactory = mock(OwnedPayloadActionFactory.class);
		when(actionFactory.getPoliciesActionFactory()).thenReturn(
				ownedPayloadActionFactory);
		when(actionFactory.getProxiesActionFactory()).thenReturn(
				ownedPayloadActionFactory);
	}

	@Test
	public void shouldListNames() throws Exception {
		given3OwnedPayloads();
		whenGetOwnedPayloadNames();
		then3OwnedPayloadNamesReturned();
	}

	private void given3OwnedPayloads() {
		Action<List<String>> action = mock(Action.class);
		givenPolicies = Arrays.asList("a", "b", "c");
		when(action.execute()).thenReturn(givenPolicies);
		mockListAction(action);
	}

	protected abstract void mockListAction(Action<List<String>> action);

	private void whenGetOwnedPayloadNames() {
		names = client.list();
	}

	private void then3OwnedPayloadNamesReturned() {
		assertEquals(names, givenPolicies);
	}

	@Test
	public void shouldGetExistingOwnedPayload() throws Exception {
		givenOwnedPayload();
		whenGetOwnedPayload();
		thenOwnedPayloadReceived();
	}

	private void givenOwnedPayload() {
		Action<NamedOwnedPayload> action = mock(Action.class);
		givenOwnedPayload = new NamedOwnedPayload();
		givenOwnedPayload.setOwners(Arrays.asList("user1", "user2"));
		givenOwnedPayload.setPayload("payload");
		givenOwnedPayload.setName(policyName);

		when(action.execute()).thenReturn(givenOwnedPayload);
		mockGetAction(policyName, action);
	}

	protected abstract void mockGetAction(String policyName,
			Action<NamedOwnedPayload> action);

	private void whenGetOwnedPayload() {
		ownedPayload = client.get(policyName);
	}

	private void thenOwnedPayloadReceived() {
		assertEquals(givenOwnedPayload.getOwners(), ownedPayload.getOwners());
		assertEquals(givenOwnedPayload.getPayload(), ownedPayload.getPayload());
	}

	@Test
	public void shouldGetExistingOwnedPayloadPayload() throws Exception {
		givenOwnedPayloadPayload();
		whenGetOwnedPayloadPayload();
		thenOwnedPayloadPayloadReceived();
	}

	private void givenOwnedPayloadPayload() {
		Action<String> action = mock(Action.class);
		when(action.execute()).thenReturn(givenSecurityPolicyContent);
		mockGetPayloadAction(policyName, action);
	}

	protected abstract void mockGetPayloadAction(String name,
			Action<String> action);

	private void whenGetOwnedPayloadPayload() {
		securityPolicyContent = client.getPayload(policyName);
	}

	private void thenOwnedPayloadPayloadReceived() {
		assertEquals(securityPolicyContent, givenSecurityPolicyContent);
	}

	@Test
	public void should404BeThrownWhenGettingNonExistingOwnedPayload()
			throws Exception {
		givenNonExistingOwnedPayload();
		try {
			whenGetOwnedPayloadPayload();
			fail("Not found exception should be thrown");
		} catch (NotFoundException e) {
			// OK should be thrown
		}

	}

	private void givenNonExistingOwnedPayload() {
		Action<String> action = mock(Action.class);
		when(action.execute()).thenThrow(new NotFoundException());
		mockGetPayloadAction(policyName, action);
	}

	@Test
	public void shouldShouldAddNewOwnedPayload() throws Exception {
		givenNewOwnedPayload();
		whenAddingOwnedPayload();
		thenNewOwnedPayloadAdded();
	}

	private void givenNewOwnedPayload() {
		newOwnedPayloadAction = mock(Action.class);
		givenNewOwnedPayload = new NamedOwnedPayload();
		givenNewOwnedPayload.setName(policyName);
		givenNewOwnedPayload.setPayload(securityPolicyContent);
		givenNewOwnedPayload.setOwners(Arrays.asList("user1", "user2"));

		mockNewAction(username, givenNewOwnedPayload, newOwnedPayloadAction);
	}

	protected abstract void mockNewAction(String username,
			NamedOwnedPayload payload, Action<Class<Void>> action);

	private void whenAddingOwnedPayload() {
		client.create(givenNewOwnedPayload);
	}

	private void thenNewOwnedPayloadAdded() {
		verify(newOwnedPayloadAction, times(1)).execute();
	}

	@Test
	public void shouldThrowExceptionWhileAddingOwnedPayloadWithoutUniqueName()
			throws Exception {
		givenExistingOwnedPayload();
		try {
			whenAddingOwnedPayloadWithNonUniqueName();
			fail("Already exists exception should be thrown");
		} catch (AlreadyExistsException e) {
			// Ok should be thrown
		}
	}

	private void givenExistingOwnedPayload() {
		givenNewOwnedPayload();
		when(newOwnedPayloadAction.execute()).thenThrow(
				new AlreadyExistsException());
	}

	private void whenAddingOwnedPayloadWithNonUniqueName() {
		whenAddingOwnedPayload();
	}

	@Test
	public void shouldDeleteExistingOwnedPayload() throws Exception {
		givenOwnedPayloadToDelete();
		whenDeleteOwnedPayload();
		thenSecurityPolicyRemoved();
	}

	private void givenOwnedPayloadToDelete() {
		deleteAction = mock(Action.class);
		mockDeleteAction(username, policyName, deleteAction);
	}

	protected abstract void mockDeleteAction(String username, String name,
			Action<Class<Void>> action);

	private void whenDeleteOwnedPayload() {
		client.delete(policyName);
	}

	private void thenSecurityPolicyRemoved() {
		verify(deleteAction, times(1)).execute();
	}

	@Test
	public void shouldThrow404WhenDeletingNonExistingOwnedPayload()
			throws Exception {
		givenEmptyOwnedPayloadList();
		try {
			whenDeleteOwnedPayload();
			fail("Not found exception should be thrown");
		} catch (NotFoundException e) {
			// OK should be thrown
		}
	}

	private void givenEmptyOwnedPayloadList() {
		givenDeleteThrowsException(new NotFoundException());
	}

	private void givenDeleteThrowsException(Exception e) {
		deleteAction = mock(Action.class);
		when(deleteAction.execute()).thenThrow(e);
		mockDeleteAction(username, policyName, deleteAction);
	}

	@Test
	public void shouldThrow403WhileDeletingNotOwnedOwnedPayload()
			throws Exception {
		givenOwnedPayloadOwnedByOtherUser();
		try {
			whenDeleteOwnedPayload();
			fail("Not owned by the user, exception should be thrown");
		} catch (NotAllowedException e) {
			// OK should be thrown
		}
	}

	private void givenOwnedPayloadOwnedByOtherUser() {
		givenDeleteThrowsException(new NotAllowedException());
	}

	@Test
	public void shouldAddOwnedPayloadWithNameSpace() throws Exception {
		givenEmptyOwnedPayloadWaitingForSecPolicyWithNamespace();
		whenAddingOwnedPayloadWithNamespace();
		thenOwnedPayloadWithNamespaceAdded();
	}

	private void givenEmptyOwnedPayloadWaitingForSecPolicyWithNamespace() {
		givenNewOwnedPayload();
		givenNewOwnedPayload.setName(policyNameWithNamespace);
		mockNewAction(username, givenNewOwnedPayload, newOwnedPayloadAction);
	}

	private void whenAddingOwnedPayloadWithNamespace() {
		client.create(givenNewOwnedPayload);
	}

	private void thenOwnedPayloadWithNamespaceAdded() {
		verifyNewAction(username, givenNewOwnedPayload);
	}

	protected abstract void verifyNewAction(String username,
			NamedOwnedPayload payload);

	@Test
	public void shouldRemoveOwnedPayloadWithNamespace() throws Exception {
		givenOwnedPayloadWithNamespaceForDelete();
		whenDeleteOwnedPayloadWithNamespace();
		thenOwnedPayloadWithNamespaceRemoved();
	}

	private void givenOwnedPayloadWithNamespaceForDelete() {
		deleteAction = mock(Action.class);
		mockDeleteAction(username, policyNameWithNamespace, deleteAction);
	}

	private void whenDeleteOwnedPayloadWithNamespace() {
		client.delete(policyNameWithNamespace);
	}

	private void thenOwnedPayloadWithNamespaceRemoved() {
		verifyDeleteAction(username, policyNameWithNamespace);
	}

	protected abstract void verifyDeleteAction(String username, String name);

	@Test
	public void shouldOwnedPayloadPolicyWithNamespace() throws Exception {
		givenOwnedPayloadWithNamespace();
		whenGetOwnedPayloadWithNamespace();
		thenOwnedPayloadWithNamespacePayloadReceived();
	}

	private void givenOwnedPayloadWithNamespace() {
		securityPolicyContent = null;
		Action<String> action = mock(Action.class);
		when(action.execute()).thenReturn(givenSecurityPolicyContent);
		mockGetPayloadAction(policyNameWithNamespace, action);
	}

	private void whenGetOwnedPayloadWithNamespace() {
		securityPolicyContent = client.getPayload(policyNameWithNamespace);
	}

	private void thenOwnedPayloadWithNamespacePayloadReceived() {
		assertEquals(securityPolicyContent, givenSecurityPolicyContent);
	}
}
