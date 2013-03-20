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

package pl.cyfronet.coin.impl.action.key;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import java.util.Arrays;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.exception.KeyNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;
import pl.cyfronet.coin.impl.air.client.UserKeyInfo;
import pl.cyfronet.coin.impl.utils.FileUtils;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class DeletePublicKeyActionTest extends ActionTest {

	private String vphUsername = "username";
	private String keyId = "123df";
	private String keyName = "keyName";
	private String publicKeyContent = FileUtils.getFileContent("id_rsa.pub");
	private String fingerprint = "01:5b:10:bc:40:78:38:d1:d0:2a:5a:e6:6a:ab:87:59";
	private Action<Class<Void>> action;

	@Test
	public void shouldDeleteUserPublicKey() throws Exception {
		whenDeletePublicUserKey();
		thenValidateAirDeleteRequest();
	}

	private void whenDeletePublicUserKey() {
		action = actionFactory.createDeletePublicKeyAction(vphUsername, keyId);
		action.execute();
	}

	private void thenValidateAirDeleteRequest() throws Exception {
		verify(air, times(1)).deletePublicKey(vphUsername, keyId);
		verify(atmosphere, times(1)).removeKeyPair(keyId);
	}

	@Test
	public void shouldThrowExceptionWhileRemovingNotOwnedKey() throws Exception {
		givenAirWithKeyNotBelongingToTheRequestUser();
		try {
			whenDeletePublicUserKey();
			fail();
		} catch (KeyNotFoundException e) {
			// OK should be thrown
		}
	}

	private void givenAirWithKeyNotBelongingToTheRequestUser() {
		when(air.getPublicKey(vphUsername, keyId)).thenThrow(
				new ServerWebApplicationException(new ResponseBuilderImpl()
						.status(400).build()));
	}

	@Test
	public void shouldRollback() throws Exception {
		givenMockedAirActionsForDeleteAndRollback();
		whenKeyDeletedAndRollbacked();
		thenKeyDeletedAndAddedOnceAgainToAir();
	}

	private void givenMockedAirActionsForDeleteAndRollback() {
		when(air.getPublicKey(vphUsername, keyId)).thenReturn(
				publicKeyContent.trim());

		UserKeyInfo info = new UserKeyInfo();
		info.set_id(keyId);
		info.setName(keyName);
		when(air.getUserKeys(vphUsername)).thenReturn(Arrays.asList(info));

		when(
				air.addKey(vphUsername, keyName, publicKeyContent.trim(),
						fingerprint)).thenReturn("newId");
	}

	private void whenKeyDeletedAndRollbacked() {
		whenDeletePublicUserKey();
		action.rollback();
	}

	private void thenKeyDeletedAndAddedOnceAgainToAir() throws Exception {
		thenValidatePubliKeyFetchedFromAir();
		thenValidateAirDeleteRequest();

		// rollback
		verify(air, times(1)).addKey(vphUsername, keyName,
				publicKeyContent.trim(), fingerprint);
	}

	private void thenValidatePubliKeyFetchedFromAir() {
		verify(air, times(1)).getUserKeys(vphUsername);
		verify(air, times(1)).getPublicKey(vphUsername, keyId);
	}
}
