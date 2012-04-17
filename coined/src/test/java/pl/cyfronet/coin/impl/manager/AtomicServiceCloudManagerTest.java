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

package pl.cyfronet.coin.impl.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.manager.exception.ApplianceTypeNotFound;
import pl.cyfronet.coin.impl.manager.matcher.AddAtomicServiceMatcher;
import pl.cyfronet.dyrealla.core.DyReAllaManagerService;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class AtomicServiceCloudManagerTest extends AbstractCloudManagerTest {

	@Test
	public void shoutGetAtomicServicesList() {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		manager.setAir(air);

		ApplianceType type1 = new ApplianceType();
		type1.setName("type1");
		type1.setDescription("type1 description");
		type1.setHttp(true);
		type1.setIn_proxy(true);
		type1.setPublished(true);
		type1.setScalable(true);
		type1.setShared(true);
		type1.setVnc(true);

		ApplianceType type2 = new ApplianceType();
		type2.setPublished(false);
		type1.setName("type2");
		type1.setDescription("type2 description");
		type1.setHttp(false);
		type1.setIn_proxy(false);
		type1.setPublished(false);
		type1.setScalable(false);
		type1.setShared(false);
		type1.setVnc(false);

		// when
		when(air.getApplianceTypes()).thenReturn(Arrays.asList(type1, type2));
		List<AtomicService> asList = manager.getAtomicServices();

		// then
		assertEquals(2, asList.size());

		AtomicService as1 = asList.get(0);
		AtomicService as2 = asList.get(1);

		verify(air, times(1)).getApplianceTypes();

		assertATAndAs(type1, as1);
		assertATAndAs(type2, as2);
	}

	@Test
	public void shouldTrowExceptionWhenASIsNotFound() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		manager.setAir(air);

		ApplianceType type1 = new ApplianceType();
		type1.setName("type1");

		ApplianceType type2 = new ApplianceType();
		type1.setName("type2");

		// when
		when(air.getApplianceTypes()).thenReturn(Arrays.asList(type1, type2));
		
		try {
			manager.getInitialConfigurations("nonExisting");
			fail();
		} catch(ApplianceTypeNotFound e) {
			//this exception should be thrown
		}

		// then
		verify(air, times(1)).getApplianceTypes();
	}
	
	@Test
	public void shouldCreateAtomicServiceWithoutEndpoints() throws Exception {
		// given
		String cloudSite = "cyfronet-nova";

		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		DyReAllaManagerService atmosphere = mock(DyReAllaManagerService.class);
		manager.setDefaultSiteId(cloudSite);

		manager.setAir(air);
		manager.setAtmosphere(atmosphere);

		String asiId = "23";
		String asId = "1";
		String asAirId = "asdf3432";
		String asName = "name";
		String asDescription = "description";
		String username = "user";

		AtomicService as = new AtomicService();
		as.setName(asName);
		as.setDescription(asDescription);
		as.setHttp(true);

		AddAtomicServiceMatcher matcher = new AddAtomicServiceMatcher(as);

		// when
		when(air.addAtomicService(argThat(matcher))).thenReturn(asAirId);

		when(atmosphere.createTemplate(asiId, asName, cloudSite, asAirId))
				.thenReturn(asId);

		String createdAsId = manager.createAtomicService(asiId, as, username);

		// then
		verify(air, times(1)).addAtomicService(argThat(matcher));
		verify(atmosphere, times(1)).createTemplate(asiId, asName, cloudSite,
				asAirId);
		assertEquals(asAirId, createdAsId);
	}

	@Test
	public void shouldCreateAtomicServiceWithEndpoints() throws Exception {
		// given
		String cloudSite = "cyfronet-nova";

		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		DyReAllaManagerService atmosphere = mock(DyReAllaManagerService.class);
		manager.setDefaultSiteId(cloudSite);

		manager.setAir(air);
		manager.setAtmosphere(atmosphere);

		String asiId = "23";
		String asId = "1";
		String asAirId = "asdf3432";
		String asName = "name";
		String asDescription = "description";
		String username = "user";

		AtomicService as = new AtomicService();
		as.setName(asName);
		as.setDescription(asDescription);
		as.setHttp(true);

		Endpoint e1 = new Endpoint();
		e1.setDescription("e1 description");
		e1.setDescriptor("<wsdl/>");
		e1.setInvocationPath("/service1/path");
		e1.setPort(8080);
		e1.setServiceName("e1ServiceName");

		Endpoint e2 = new Endpoint();
		e2.setDescription("e2 description");
		e2.setDescriptor("<wadl></wadl>");
		e2.setInvocationPath("/service2/path");
		e2.setPort(8080);
		e2.setServiceName("e2ServiceName");

		as.setEndpoints(Arrays.asList(e1, e2));

		AddAtomicServiceMatcher matcher = new AddAtomicServiceMatcher(as);

		// when
		when(air.addAtomicService(argThat(matcher))).thenReturn(asAirId);

		when(atmosphere.createTemplate(asiId, asName, cloudSite, asAirId))
				.thenReturn(asId);

		String createdAsId = manager.createAtomicService(asiId, as, username);

		// then
		verify(air, times(1)).addAtomicService(argThat(matcher));
		verify(atmosphere, times(1)).createTemplate(asiId, asName, cloudSite,
				asAirId);
		assertEquals(asAirId, createdAsId);
	}
}
