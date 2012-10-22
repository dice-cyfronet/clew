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

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.KeyManagement;
import pl.cyfronet.coin.api.beans.PublicKeyInfo;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.AddPublicKeyAction;
import pl.cyfronet.coin.impl.action.ListUserKeysAction;
import pl.cyfronet.coin.impl.security.mi.MasterInterfaceAuthenticationHandler;
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
public class KeyManagementTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ActionFactory actionFactory;

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
		AddPublicKeyAction action = mock(AddPublicKeyAction.class);
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

//	@Test(expectedExceptions = KeyAlreadyExistsException.class)
//	public void shouldThrowExceptionWhileAddingKeyWithNotUniqueName()
//			throws Exception {
//		givenMockedAddNotUniqueKeyAction();
//		whenAddNewKey();
//	}
//
//	private void givenMockedAddNotUniqueKeyAction() {
//		AddPublicKeyAction action = mock(AddPublicKeyAction.class);
//		when(action.execute())
//				.thenThrow(new KeyAlreadyExistsException(keyName));
//		when(
//				actionFactory.createAddPublicKeyAction(username, keyName,
//						publicKeyContent)).thenReturn(action);
//	}
}
