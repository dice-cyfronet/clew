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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static pl.cyfronet.coin.impl.CoinedAsserts.assertATAndAs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class ListAtomicServicesActionTest extends ActionTest {

	private ApplianceType type1;
	private ApplianceType type2;
	private List<AtomicService> asList;

	@Test
	public void shoutGetAtomicServicesList() {
		givenAirAtomicServicesList();
		whenGetAtomicServices();
		thenValidateReturnedAtomicServices();
	}

	private void givenAirAtomicServicesList() {
		type1 = new ApplianceType();
		type1.setName("type1");
		type1.setDescription("type1 description");
		type1.setHttp(true);
		type1.setIn_proxy(true);
		type1.setPublished(true);
		type1.setScalable(true);
		type1.setShared(true);
		type1.setVnc(true);
		type1.setTemplates_count(0);

		ATEndpoint type1AsEndpoint = new ATEndpoint();
		type1AsEndpoint.setDescription("description");
		type1AsEndpoint.setEndpoint_type("ws");
		type1AsEndpoint.setId("1");
		type1AsEndpoint.setInvocation_path("invocation/path");
		type1AsEndpoint.setPort(9090);
		type1AsEndpoint.setService_name("gimias");

		ATEndpoint type2AsEndpoint = new ATEndpoint();
		type2AsEndpoint.setDescription("description");
		type2AsEndpoint.setId("2");
		type2AsEndpoint.setInvocation_path("path");
		type2AsEndpoint.setPort(9090);
		type2AsEndpoint.setService_name("gimias");

		type1.setEndpoints(Arrays.asList(type1AsEndpoint, type2AsEndpoint));

		type2 = new ApplianceType();
		type2.setPublished(false);
		type2.setName("type2");
		type2.setDescription("type2 description");
		type2.setHttp(false);
		type2.setIn_proxy(false);
		type2.setPublished(false);
		type2.setScalable(false);
		type2.setShared(false);
		type2.setVnc(false);
		type2.setTemplates_count(2);

		//this AS should not be returned, because it is in development mode
		ApplianceType devAS = new ApplianceType();
		devAS.setDevelopment(true);
		
		when(air.getApplianceTypes()).thenReturn(Arrays.asList(type1, type2, devAS));
		
		//descriptor payload
		when(air.getEndpointDescriptor("1")).thenReturn(null);
		when(air.getEndpointDescriptor("2")).thenReturn("GET POST /hello/{name}");
	}

	private void whenGetAtomicServices() {
		ListAtomicServicesAction action = actionFactory
				.createListAtomicServicesAction();
		asList = action.execute();
	}

	private void thenValidateReturnedAtomicServices() {
		assertEquals(asList.size(), 2);

		AtomicService as1 = asList.get(0);
		AtomicService as2 = asList.get(1);

		verify(air, times(1)).getApplianceTypes();

		assertATAndAs(type1, as1, null, "GET POST /hello/{name}");
		assertATAndAs(type2, as2);
		
		thenCheckAirRequestWithEndpoints();
	}

	private void thenCheckAirRequestWithEndpoints() {
		thenCheckAirRequest();
		verify(air, times(2)).getEndpointDescriptor(anyString());
	}
	
	private void thenCheckAirRequest() {
		verify(air, times(1)).getApplianceTypes();
	}

	@Test
	public void shouldGetEmptyAtomicServicesList() throws Exception {
		givenEmptyAirAtomicServicesList();
		whenGetAtomicServices();
		thenCheckReturnedEmptyAtomicServicesList();
	}

	private void givenEmptyAirAtomicServicesList() {
		when(air.getApplianceTypes())
				.thenReturn(new ArrayList<ApplianceType>());
	}
	
	private void thenCheckReturnedEmptyAtomicServicesList() {
		assertEquals(asList.size(), 0);
		thenCheckAirRequest();
	}

}
