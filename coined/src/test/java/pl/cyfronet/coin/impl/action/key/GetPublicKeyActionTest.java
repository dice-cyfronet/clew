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
import static org.testng.Assert.*;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.exception.KeyNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;
import pl.cyfronet.coin.impl.utils.FileUtils;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetPublicKeyActionTest extends ActionTest {

	private String vphUsername = "username";
	private String keyId = "123df";
	private String publicKeyContent = FileUtils.getFileContent("id_rsa.pub");
	private String receivedKeyContent;

	@Test
	public void shouldGetPublicKey() throws Exception {
		givenAirWithUserKeyRegistered();
		whenGetUserKey();
		thenUserKeyReceived();
	}

	private void givenAirWithUserKeyRegistered() {
		when(air.getPublicKey(vphUsername, keyId)).thenReturn(publicKeyContent);
	}

	private void whenGetUserKey() {
		Action<String> action = actionFactory.createGetPublicKeyAction(
				vphUsername, keyId);
		receivedKeyContent = action.execute();
	}

	private void thenUserKeyReceived() {
		assertEquals(receivedKeyContent, publicKeyContent);
		thenValidateAirAskedAboutUserKey();
	}

	private void thenValidateAirAskedAboutUserKey() {
		verify(air, times(1)).getPublicKey(vphUsername, keyId);
	}

	@Test
	public void shouldThrowExceptionWhileGettingKeyNotBellongingToTheUser()
			throws Exception {
		givenAirWithKeyNotBelongingToTheUser();
		try {
			whenGetUserKey();
			fail();
		} catch (KeyNotFoundException e) {
			// OK exception should be thrown
		}

		thenValidateAirAskedAboutUserKey();

	}

	private void givenAirWithKeyNotBelongingToTheUser() {
		when(air.getPublicKey(vphUsername, keyId)).thenThrow(
				new ServerWebApplicationException(new ResponseBuilderImpl()
						.status(400).build()));
	}
}
