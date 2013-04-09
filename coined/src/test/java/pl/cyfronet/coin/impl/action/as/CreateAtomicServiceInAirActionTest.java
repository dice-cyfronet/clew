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

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Arrays;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.EndpointType;
import pl.cyfronet.coin.api.exception.AtomicServiceAlreadyExistsException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.ATPortMapping;
import pl.cyfronet.coin.impl.air.client.AppliancePreferences;
import pl.cyfronet.coin.impl.air.client.ApplianceSla;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.mock.matcher.AddAtomicServiceMatcher;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class CreateAtomicServiceInAirActionTest extends ActionTest {

	private ApplianceType at;
	private String createdAsId;
	private String asAirId = "as123";
	private AddAtomicServiceMatcher matcher;
	private Action<String> action;

	/**
	 * #1021
	 * @since 1.1.0
	 */
	private String username = "user123";

	@Test
	public void shouldCreateAtomicServiceRecord() throws Exception {
		givenAtomicServiceDefinitionWithEndpointsAndRedirections();
		whenAddAtomicServiceToAir();
		thenCheckASWithEndpointsAndPortMappingsAdded();
	}

	private void givenAtomicServiceDefinitionWithEndpointsAndRedirections() {
		createAtomicService();
		matcher = new AddAtomicServiceMatcher(username, at);
		when(air.addAtomicService(argThat(matcher))).thenReturn(asAirId);
	}

	private void createAtomicService() {
		at = new ApplianceType();
		at.setId("as");
		at.setName("as");
		at.setDescription("description");
		at.setPublished(true);
		at.setScalable(true);
		at.setShared(true);

		ATEndpoint e1 = new ATEndpoint();
		e1.setDescription("e1 description");
		e1.setDescriptor("e1 descriptor");
		e1.setInvocation_path("e1/path");
		e1.setPort(80);
		e1.setEndpoint_type(EndpointType.REST.toString());

		ATEndpoint e2 = new ATEndpoint();
		e2.setDescription("e2 description");
		e2.setDescriptor("e2 descriptor");
		e2.setInvocation_path("e2/path");
		e2.setPort(81);
		e2.setEndpoint_type(EndpointType.WS.toString());

		ApplianceSla sla = new ApplianceSla();
		sla.setConcurrent_requests(123);
		sla.setMean_response_time(2f);
		sla.setRequests_throughput(3f);
		at.setAppliance_sla(sla);
		
		AppliancePreferences prefs = new AppliancePreferences();
		prefs.setCpu(2.4f);
		prefs.setDisk(222354);
		prefs.setMemory(5423623);
		at.setAppliance_preferences(prefs);
		
		at.setEndpoints(Arrays.asList(e1, e2));

		// #1331
		ATPortMapping p80 = new ATPortMapping();
		p80.setHttp(true);
		p80.setId("p80id");
		p80.setPort(80);
		p80.setService_name("p80_service_name");

		ATPortMapping p81 = new ATPortMapping();
		p81.setHttp(true);
		p81.setId("p81id");
		p81.setPort(81);
		p81.setService_name("p81_service_name");

		ATPortMapping p22 = new ATPortMapping();
		p22.setHttp(false);
		p22.setId("p22id");
		p22.setPort(22);
		p22.setService_name("p22_service_name");

		at.setPort_mappings(Arrays.asList(p80, p81, p22));
	}

	private void whenAddAtomicServiceToAir() {
		action = actionFactory.createCreateAtomicServiceInAirAction(username,
				at);
		createdAsId = action.execute();
	}

	private void thenCheckASWithEndpointsAndPortMappingsAdded() {
		thenCheckRequestSendToAir();
	}

	private void thenCheckRequestSendToAir() {
		verify(air, times(1)).addAtomicService(argThat(matcher));

		assertEquals(createdAsId, asAirId);
	}

	@Test
	public void shouldCreateAtomicServiceWithoutEndpointsAndRedirections()
			throws Exception {
		givenAtomicServiceDefinitionWithoutEndpointsAndRedirections();
		whenAddAtomicServiceToAir();
		thenCheckRequestSendToAir();
	}

	private void givenAtomicServiceDefinitionWithoutEndpointsAndRedirections() {
		String asName = "name";
		String asDescription = "description";

		at = new ApplianceType();
		at.setName(asName);
		at.setDescription(asDescription);

		matcher = new AddAtomicServiceMatcher(username, at);
		when(air.addAtomicService(argThat(matcher))).thenReturn(asAirId);
	}

	@Test
	public void shouldRemoveAtomicServiceOnRollback() throws Exception {
		givenAtomicServiceDefinitionWithoutEndpointsAndRedirections();
		whenAddAtomicServiceToAirAndRollback();
		thenAtomicServiceCreatedAndRemovedFromAir();

	}

	private void whenAddAtomicServiceToAirAndRollback() {
		whenAddAtomicServiceToAir();
		action.rollback();
	}

	private void thenAtomicServiceCreatedAndRemovedFromAir() {
		thenCheckRequestSendToAir();
		verify(air, times(1)).deleteAtomicService(createdAsId, true);
	}

	@Test
	public void shouldThrowExceptionWhileAddingASWithNotUniqueName()
			throws Exception {
		givenAirWithAlreadyASRegistered();
		try {
			whenAddAtomicServiceToAir();
			fail();
		} catch (AtomicServiceAlreadyExistsException e) {
			// OK - should be thrown
		}

		thenCheckRequestSendToAir();
	}

	private void givenAirWithAlreadyASRegistered() {
		createAtomicService();

		matcher = new AddAtomicServiceMatcher(username, at);
		when(air.addAtomicService(argThat(matcher))).thenThrow(
				getAirException(302));
	}
}
