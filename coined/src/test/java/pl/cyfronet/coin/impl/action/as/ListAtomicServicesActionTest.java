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
package pl.cyfronet.coin.impl.action.as;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static pl.cyfronet.coin.impl.CoinedAsserts.assertATAndAs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class ListAtomicServicesActionTest extends ActionTest {

	private ApplianceType type1;
	private ApplianceType type2;
	private List<AtomicService> asList;
	private String username = "username";
	private ApplianceType userDevAS;

	@Test
	public void shoutGetAtomicServicesList() {
		givenAirAtomicServicesList();
		whenGetAtomicServices();
		thenValidateReturnedAtomicServices();
	}

	private void givenAirAtomicServicesList() {
		type1 = new ApplianceType();
		type1.setName("type1");
		type1.setId("type1Id");
		type1.setDescription("type1 description");
		type1.setPublished(true);
		type1.setScalable(true);
		type1.setShared(true);
		type1.setTemplates_count(0);
		type1.setAuthor("marek");

		ATEndpoint type1AsEndpoint = new ATEndpoint();
		type1AsEndpoint.setDescription("description");
		type1AsEndpoint.setEndpoint_type("ws");
		type1AsEndpoint.setId("1");
		type1AsEndpoint.setInvocation_path("invocation/path");
		type1AsEndpoint.setPort(9090);

		ATEndpoint type2AsEndpoint = new ATEndpoint();
		type2AsEndpoint.setDescription("description");
		type2AsEndpoint.setId("2");
		type2AsEndpoint.setInvocation_path("path");
		type2AsEndpoint.setPort(9090);
		type2AsEndpoint.setDescriptor("GET POST /hello/{name}");

		type1.setEndpoints(Arrays.asList(type1AsEndpoint, type2AsEndpoint));

		type2 = new ApplianceType();
		type2.setPublished(false);
		type2.setName("type2");
		type2.setId("type2Id");
		type2.setDescription("type2 description");
		type2.setPublished(false);
		type2.setScalable(false);
		type2.setShared(false);
		type2.setTemplates_count(2);

		// this AS should not be returned, because it is in
		// development mode and it does not belong to the user.
		ApplianceType devAS = new ApplianceType();
		devAS.setDevelopment(true);

		userDevAS = new ApplianceType();
		userDevAS.setAuthor(username);
		userDevAS.setDevelopment(true);
		userDevAS.setName("UserDevelopmentAS");

		when(air.getApplianceTypes(null, true)).thenReturn(
				Arrays.asList(type1, type2, devAS, userDevAS));
	}

	private void whenGetAtomicServices() {
		Action<List<AtomicService>> action = actionFactory
				.createListAtomicServicesAction(username);
		asList = action.execute();
	}

	private void thenValidateReturnedAtomicServices() {
		assertEquals(asList.size(), 3);

		AtomicService as1 = asList.get(0);
		AtomicService as2 = asList.get(1);
		AtomicService devAs = asList.get(2);

		verify(air, times(1)).getApplianceTypes(null, true);

		assertATAndAs(type1, as1, null, "GET POST /hello/{name}");
		assertATAndAs(type2, as2);

		assertEquals(devAs.getName(), userDevAS.getName());
		assertTrue(devAs.isDevelopment());

		thenCheckAirRequestWithEndpoints();
	}

	private void thenCheckAirRequestWithEndpoints() {
		thenCheckAirRequest();
	}

	private void thenCheckAirRequest() {
		verify(air, times(1)).getApplianceTypes(null, true);
	}

	@Test
	public void shouldGetEmptyAtomicServicesList() throws Exception {
		givenEmptyAirAtomicServicesList();
		whenGetAtomicServices();
		thenCheckReturnedEmptyAtomicServicesList();
	}

	private void givenEmptyAirAtomicServicesList() {
		when(air.getApplianceTypes(null, true)).thenReturn(
				new ArrayList<ApplianceType>());
	}

	private void thenCheckReturnedEmptyAtomicServicesList() {
		assertEquals(asList.size(), 0);
		thenCheckAirRequest();
	}

}
