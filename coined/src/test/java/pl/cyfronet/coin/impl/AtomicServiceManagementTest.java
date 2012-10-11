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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.CloudFacade;
import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.EndpointType;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.ListAtomicServicesAction;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
//@formatter:off
@ContextConfiguration( locations={
		"classpath:rest-test-properties.xml",
		"classpath:rest-test-imports.xml",
		"classpath:rest-test-mocks.xml",
		"classpath:rest-test-as-management-client.xml",
		"classpath:META-INF/spring/rest-services.xml"
	} )
//@formatter:on
public class AtomicServiceManagementTest extends
		AbstractTestNGSpringContextTests {

	@Autowired
	private ActionFactory actionFactory;

	@Autowired
	private CloudFacade asManagementClient;

	private List<AtomicService> atomicServices;

	@DataProvider(name = "getAtomicServicesSizes")
	protected Object[][] getAtomicServicesSizes() {
		return new Object[][] { { 0 }, { 1 }, { 2 }, { 3 }, { 4 } };
	}

	@Test(dataProvider = "getAtomicServicesSizes")
	public void shouldGetAtomicServices(int size) throws Exception {
		givenAtomicServicesSize(size);
		whenGetAtomicServices();
		thanCheckReceivedAtomicServices(size);
	}

	private void givenAtomicServicesSize(int size) {
		List<AtomicService> ases = new ArrayList<AtomicService>();
		for (int i = 0; i < size; i++) {
			AtomicService as = new AtomicService();
			as.setName("as" + i);
			as.setDescription("description " + i);
			as.setHttp(true);
			as.setEndpoints(getEndpoints(i));

			ases.add(as);
		}

		ListAtomicServicesAction action = mock(ListAtomicServicesAction.class);
		
		when(action.execute()).thenReturn(ases);
		when(actionFactory.createListAtomicServicesAction()).thenReturn(action);
	}

	private List<Endpoint> getEndpoints(int size) {
		List<Endpoint> endpoints = new ArrayList<Endpoint>();
		for (int i = 0; i < size; i++) {
			Endpoint endpoint = new Endpoint();
			endpoint.setDescription("endpoint description " + i);
			endpoint.setDescriptor("descriptor " + i);
			endpoint.setInvocationPath("/path/" + i);
			endpoint.setPort(900 + i);
			endpoint.setServiceName("serviceName" + i);
			endpoint.setType(getEndpointType(i));
			endpoints.add(endpoint);
		}
		return endpoints;
	}

	private EndpointType getEndpointType(int i) {
		int nr = i % 3;
		if (nr == 0) {
			return EndpointType.WS;
		} else if (nr == 0) {
			return EndpointType.WEBAPP;
		} else {
			return EndpointType.REST;
		}
	}

	private void whenGetAtomicServices() {
		atomicServices = asManagementClient.getAtomicServices();
	}

	private void thanCheckReceivedAtomicServices(int size) {
		assertEquals(atomicServices.size(), size);
		for (int i = 0; i < size; i++) {
			checkAtomicService(atomicServices.get(i), i);
		}
	}

	private void checkAtomicService(AtomicService atomicService, int nr) {
		assertEquals(atomicService.getName(), "as" + nr);
		assertEquals(atomicService.getDescription(), "description " + nr);
		assertTrue(atomicService.isHttp());
		assertFalse(atomicService.isActive());
		assertFalse(atomicService.isInProxy());
		assertFalse(atomicService.isPublished());
		assertFalse(atomicService.isScalable());
		assertFalse(atomicService.isShared());
		assertFalse(atomicService.isVnc());

		if (nr == 0) {
			checkEndpointsListEmpty(atomicService.getEndpoints());
		} else {
			checkEndpoints(atomicService.getEndpoints(), nr);
		}
	}

	private void checkEndpointsListEmpty(List<Endpoint> endpoints) {
		assertEquals(endpoints.size(), 0);
	}

	private void checkEndpoints(List<Endpoint> endpoints, int size) {
		assertEquals(endpoints.size(), size);
		for (int i = 0; i < size; i++) {
			checkEndpoint(endpoints.get(i), i);
		}
	}

	private void checkEndpoint(Endpoint endpoint, int nr) {
		assertEquals(endpoint.getDescription(), "endpoint description " + nr);
		assertEquals(endpoint.getDescriptor(), "descriptor " + nr);
		assertEquals(endpoint.getInvocationPath(), "/path/" + nr);
		assertEquals(endpoint.getPort(), new Integer(900 + nr));
		assertEquals(endpoint.getServiceName(), "serviceName" + nr);
		assertEquals(endpoint.getType(), getEndpointType(nr));
	}
}
