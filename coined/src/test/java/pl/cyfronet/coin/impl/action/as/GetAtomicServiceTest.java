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
import static org.testng.Assert.fail;
import static pl.cyfronet.coin.impl.CoinedAsserts.assertATAndAs;

import java.util.Arrays;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetAtomicServiceTest extends ActionTest {

	private static final String AS_ID = "asId";
	private ApplianceType asType;
	private AtomicService atomicService;

	@Test
	public void shouldGetAtomicService() throws Exception {
		givenAtomicServiceInAir();
		whenGetAtomicService(AS_ID);
		thenCheckReceivedAtomicService();

	}

	private void givenAtomicServiceInAir() {
		asType = new ApplianceType();
		asType.setId(AS_ID);
		asType.setName("as");
		asType.setDescription("type1 description");
		asType.setPublished(true);
		asType.setScalable(true);
		asType.setShared(true);
		asType.setDevelopment(true);
		asType.setTemplates_count(0);
		asType.setProxy_conf_name("proxy/conf/name");

		ATEndpoint type1AsEndpoint = new ATEndpoint();
		type1AsEndpoint.setDescription("description");
		type1AsEndpoint.setDescriptor(null);
		type1AsEndpoint.setEndpoint_type("ws");
		type1AsEndpoint.setId("123asd");
		type1AsEndpoint.setInvocation_path("invocation/path");
		type1AsEndpoint.setPort(9090);

		ATEndpoint type2AsEndpoint = new ATEndpoint();
		type2AsEndpoint.setDescription("description");
		type2AsEndpoint.setDescriptor(null);
		type2AsEndpoint.setId("www");
		type2AsEndpoint.setInvocation_path("path");
		type2AsEndpoint.setPort(9090);

		asType.setEndpoints(Arrays.asList(type1AsEndpoint, type2AsEndpoint));

		when(air.getApplianceTypes(AS_ID, true)).thenReturn(
				Arrays.asList(asType));
	}

	private void whenGetAtomicService(String asId) {
		Action<AtomicService> action = actionFactory
				.createGetAtomicServiceAction(asId, true);
		atomicService = action.execute();
	}

	private void thenCheckReceivedAtomicService() {
		assertATAndAs(asType, atomicService, null, null);
		thenCheckAirRequest();
	}

	private void thenCheckAirRequest() {
		verify(air, times(1)).getApplianceTypes(AS_ID, true);
	}

	@Test
	public void shouldTrowExceptionWhileGettingNonExistingAS() throws Exception {
		givenAtomicServiceInAir();
		try {
			whenGetAtomicService("nonExisting");
			fail();
		} catch (AtomicServiceNotFoundException e) {
			// OK
		}
	}
}
