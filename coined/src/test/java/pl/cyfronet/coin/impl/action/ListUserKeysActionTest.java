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
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.PublicKeyInfo;
import pl.cyfronet.coin.impl.air.client.UserKeyInfo;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class ListUserKeysActionTest extends ActionTest {

	private String vphUsername = "username";
	private List<PublicKeyInfo> keys;
	private UserKeyInfo key1;
	private UserKeyInfo key2;

	@Test
	public void shouldGet2UserKeys() throws Exception {
		givenAirContentWith2KeysRegisteredForUser();
		whenGetUserKeys();
		thenInformationAbout2KeysReceived();
	}

	private void givenAirContentWith2KeysRegisteredForUser() {
		key1 = getKey(1);
		key2 = getKey(2);

		when(air.getUserKeys(vphUsername))
				.thenReturn(Arrays.asList(key1, key2));
	}

	private UserKeyInfo getKey(int nr) {
		UserKeyInfo key = new UserKeyInfo();
		key.set_id("id" + nr);
		key.setFingerprint("fingerprint" + nr);
		key.setName("key" + nr);

		return key;
	}

	private void whenGetUserKeys() {
		Action<List<PublicKeyInfo>> action = actionFactory
				.createListUserKeysAction(vphUsername);
		keys = action.execute();
	}

	private void thenInformationAbout2KeysReceived() {
		assertEquals(keys.size(), 2);
		assertKeysEquals(keys.get(0), key1);
		assertKeysEquals(keys.get(1), key2);
	}

	private void assertKeysEquals(PublicKeyInfo actual, UserKeyInfo expected) {
		assertEquals(actual.getId(), expected.get_id());
		assertEquals(actual.getKeyName(), expected.getName());
		assertEquals(actual.getFingerprint(), expected.getFingerprint());

		validateAirAskedAboutKeys();
	}

	private void validateAirAskedAboutKeys() {
		verify(air, times(1)).getUserKeys(vphUsername);
	}

	@Test
	public void shouldGet0KeysForExistingUser() throws Exception {
		givenAirWithUserWithoutKeys();
		whenGetUserKeys();
		thenKeysListEmpty();
	}

	private void givenAirWithUserWithoutKeys() {
		when(air.getUserKeys(vphUsername)).thenReturn(
				new ArrayList<UserKeyInfo>());
	}

	private void thenKeysListEmpty() {
		assertEquals(keys.size(), 0);
		validateAirAskedAboutKeys();
	}

	@Test
	public void shouldGet0KeysForNonExistingUser() throws Exception {
		givenAirWithoutUser();
		whenGetUserKeys();
		thenKeysListEmpty();
	}

	private void givenAirWithoutUser() {
		when(air.getUserKeys(vphUsername)).thenThrow(
				new ServerWebApplicationException(new ResponseBuilderImpl()
						.status(400).build()));
	}
}
