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
import static org.testng.Assert.*;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.exception.KeyAlreadyExistsException;
import pl.cyfronet.coin.impl.utils.FileUtils;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class AddPublicKeyActionTest extends ActionTest {

	private String keyId = "12353df";
	private String username = "username";
	private String keyName = "myNewKey";
	private String publicKeyContent = FileUtils.getFileContent("id_rsa.pub");
	private String fingerprint = "01:5b:10:bc:40:78:38:d1:d0:2a:5a:e6:6a:ab:87:59";

	private String addedKeytId;
	private AddPublicKeyAction action;

	@Test
	public void shouldAddNewPublicKey() throws Exception {
		givenMockedAirAbleToAddUserKey();
		whenUserAddsNewKey();
		thenKeyAddedToAir();
	}

	private void givenMockedAirAbleToAddUserKey() {
		when(air.addKey(username, keyName, publicKeyContent, fingerprint))
				.thenReturn(keyId);
	}

	private void whenUserAddsNewKey() {
		action = actionFactory.createAddPublicKeyAction(username, keyName,
				publicKeyContent);
		addedKeytId = action.execute();
	}

	private void thenKeyAddedToAir() {
		thenValidateCFTriesToAddKeyToAir();
		assertEquals(addedKeytId, keyId);
	}

	private void thenValidateCFTriesToAddKeyToAir() {
		verify(air, times(1)).addKey(username, keyName, publicKeyContent,
				fingerprint);
	}

	@Test
	public void shouldThrowExceptionWhileTryingToAddNewKeyWithNotUniqueName()
			throws Exception {
		givenAirThrown409WhenAddingKeyWithExistingName();
		try {
			whenUserAddsNewKey();
			fail();
		} catch (KeyAlreadyExistsException e) {
			// Ok should be thrown
		}
		thenValidateCFTriesToAddKeyToAir();

	}

	private void givenAirThrown409WhenAddingKeyWithExistingName() {
		when(air.addKey(username, keyName, publicKeyContent, fingerprint))
				.thenThrow(
						new ServerWebApplicationException(
								new ResponseBuilderImpl().status(409).build()));
	}

	@Test
	public void shouldRollback() throws Exception {
		givenMockedAirAbleToAddUserKey();
		whenKeyAddedAndRollbacked();
		thenKeyAddedAndRemovedFromAir();
	}

	private void whenKeyAddedAndRollbacked() {
		whenUserAddsNewKey();
		action.rollback();
	}

	private void thenKeyAddedAndRemovedFromAir() throws Exception {
		thenValidateCFTriesToAddKeyToAir();
		verify(atmosphere, times(1)).removeKeyPair(addedKeytId);
		verify(air, times(1)).deletePublicKey(username, addedKeytId);
	}
}
