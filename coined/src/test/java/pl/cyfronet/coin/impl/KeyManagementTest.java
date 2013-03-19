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

package pl.cyfronet.coin.impl;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.KeyManagement;
import pl.cyfronet.coin.api.beans.PublicKeyInfo;
import pl.cyfronet.coin.api.exception.KeyAlreadyExistsException;
import pl.cyfronet.coin.api.exception.KeyNotFoundException;
import pl.cyfronet.coin.api.exception.WrongKeyFormatException;
import pl.cyfronet.coin.auth.mi.MasterInterfaceAuthenticationHandler;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.key.DeletePublicKeyAction;
import pl.cyfronet.coin.impl.action.key.GetPublicKeyAction;
import pl.cyfronet.coin.impl.action.key.ListUserKeysAction;
import pl.cyfronet.coin.impl.utils.FileUtils;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
//@formatter:off
@ContextConfiguration( locations={
		"classpath:rest-test-properties-key.xml",
		"classpath:rest-test-imports.xml",
		"classpath:rest-test-mocks.xml",
		"classpath:rest-test-key-management-client.xml",
		"classpath:META-INF/spring/rest-services.xml"
	} )
//@formatter:on
@SuppressWarnings("unchecked")
public class KeyManagementTest extends AbstractServiceTest {

	@Autowired
	@Qualifier("keyManagementClient")
	private KeyManagement keyManagement;

	@Autowired
	private MasterInterfaceAuthenticationHandler authenticationHandler;

	private String username = "User123";

	private List<PublicKeyInfo> receivedKeys;

	private PublicKeyInfo key1;

	private PublicKeyInfo key2;

	private String keyId = "keyId";

	private String keyName = "keyName";

	private String publicKeyContent = FileUtils.getFileContent("id_rsa.pub");

	private String addedKeyId;

	private String receivedPublicKeyContent;

	@BeforeMethod
	protected void setUp() {
		when(authenticationHandler.getUsername(eq(username), anyString()))
				.thenReturn(username);
	}

	@Test
	public void shouldGet2UserKeys() throws Exception {
		givenActionMockedToReturn2KeysForUser123();
		whenGetUserKeys();
		thenReceived2Keys();
	}

	private void givenActionMockedToReturn2KeysForUser123() {
		key1 = getKey(1);
		key2 = getKey(2);

		ListUserKeysAction action = mock(ListUserKeysAction.class);
		when(action.execute()).thenReturn(Arrays.asList(key1, key2));

		when(actionFactory.createListUserKeysAction(username)).thenReturn(
				action);

		currentAction = action;
	}

	private PublicKeyInfo getKey(int nr) {
		PublicKeyInfo key = new PublicKeyInfo();
		key.setId("id" + nr);
		key.setFingerprint("fingerprint" + nr);
		key.setKeyName("key" + nr);

		return key;
	}

	private void whenGetUserKeys() {
		receivedKeys = keyManagement.list();
	}

	private void thenReceived2Keys() {
		thenActionExecuted();
		assertEquals(receivedKeys.size(), 2);
		assertKeyEquals(receivedKeys.get(0), key1);
		assertKeyEquals(receivedKeys.get(1), key2);
	}

	private void assertKeyEquals(PublicKeyInfo actual, PublicKeyInfo expected) {
		assertEquals(actual.getId(), expected.getId());
		assertEquals(actual.getKeyName(), expected.getKeyName());
		assertEquals(actual.getFingerprint(), expected.getFingerprint());
	}

	@Test
	public void shouldAddNewKey() throws Exception {
		givenMockedAddKeyAction();
		whenAddNewKey();
		thenNewKeyAdded();
	}

	private void givenMockedAddKeyAction() {
		Action<String> action = mock(Action.class);
		when(action.execute()).thenReturn(keyId);
		when(
				actionFactory.createAddPublicKeyAction(username, keyName,
						publicKeyContent)).thenReturn(action);
	}

	private void whenAddNewKey() {
		addedKeyId = keyManagement.add(keyName, publicKeyContent);
	}

	private void thenNewKeyAdded() {
		assertEquals(addedKeyId, keyId);
	}

	@Test
	public void shouldThrowExceptionWhileAddingKeyWithNotUniqueName()
			throws Exception {
		givenMockedAddNotUniqueKeyAction();
		try {
			whenAddNewKey();
			fail();
		} catch (KeyAlreadyExistsException e) {
			// OK should be thrown
		}
		thenActionExecuted();
	}

	private void givenMockedAddNotUniqueKeyAction() {
		Action<String> action = mock(Action.class);
		when(action.execute())
				.thenThrow(new KeyAlreadyExistsException(keyName));
		when(
				actionFactory.createAddPublicKeyAction(username, keyName,
						publicKeyContent)).thenReturn(action);
		currentAction = action;
	}

	@Test
	public void shouldDeleteKey() throws Exception {
		givenMockedActionAbleToDeleteKey();
		whenDeleteKey();
		thenKeyDeleted();

	}

	private void givenMockedActionAbleToDeleteKey() {
		DeletePublicKeyAction action = mock(DeletePublicKeyAction.class);
		when(actionFactory.createDeletePublicKeyAction(username, keyId))
				.thenReturn(action);
		currentAction = action;
	}

	private void whenDeleteKey() {
		keyManagement.delete(keyId);
	}

	private void thenKeyDeleted() {
		thenActionExecuted();
	}

	@Test
	public void shouldThrow404WhenDeletingNonExistingOrNotOwnedKey()
			throws Exception {
		givenDeleteActionThrowingKeyNotFoundException();
		try {
			whenDeleteKey();
			fail();
		} catch (KeyNotFoundException e) {
			// OK should be thrown.
		}
		thenActionExecuted();

	}

	private void givenDeleteActionThrowingKeyNotFoundException() {
		DeletePublicKeyAction action = mock(DeletePublicKeyAction.class);
		when(action.execute()).thenThrow(new KeyNotFoundException());

		when(actionFactory.createDeletePublicKeyAction(username, keyId))
				.thenReturn(action);

		currentAction = action;
	}

	@Test
	public void shouldGetKeyValue() throws Exception {
		givenActionReturningUserKey();
		whenGetPublicUserKey();
		thenPublicKeyReceived();
	}

	private void givenActionReturningUserKey() {
		GetPublicKeyAction action = mock(GetPublicKeyAction.class);
		when(action.execute()).thenReturn(publicKeyContent);

		when(actionFactory.createGetPublicKeyAction(username, keyId))
				.thenReturn(action);
		currentAction = action;
	}

	private void whenGetPublicUserKey() {
		receivedPublicKeyContent = keyManagement.get(keyId);
	}

	private void thenPublicKeyReceived() {
		thenActionExecuted();
		assertEquals(receivedPublicKeyContent, publicKeyContent);
	}

	@Test
	public void shouldThrow404WhenGettingNonExistingOrNotOwnedKey()
			throws Exception {
		givenGetPublicKeyThrowingKeyNotFoundException();
		try {
			whenGetPublicUserKey();
			fail();
		} catch (KeyNotFoundException e) {
			// OK should be thrown
		}

		thenActionExecuted();
	}

	private void givenGetPublicKeyThrowingKeyNotFoundException() {
		GetPublicKeyAction action = mock(GetPublicKeyAction.class);
		when(action.execute()).thenThrow(new KeyNotFoundException());

		when(actionFactory.createGetPublicKeyAction(username, keyId))
				.thenReturn(action);
		currentAction = action;
	}

	@Test
	public void shouldThrown400WhenKeyHasWrongFormat() throws Exception {
		givenKeyInWrongFormat();
		try {
			whenGetPublicUserKey();
			fail();
		} catch (WrongKeyFormatException e) {
			assertEquals(e.getMessage(), "error message");
		}

		thenActionExecuted();
	}

	private void givenKeyInWrongFormat() {
		GetPublicKeyAction action = mock(GetPublicKeyAction.class);
		when(action.execute()).thenThrow(
				new WrongKeyFormatException("error message"));

		when(actionFactory.createGetPublicKeyAction(username, keyId))
				.thenReturn(action);
		currentAction = action;
	}
}
