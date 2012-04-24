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

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetEndpointsMetadataTest extends AbstractManagerWithAirTest {

	private List<Endpoint> metadatas;

	@Test
	public void shouldGetEndpointsMetadata() throws Exception {
		givenAirContentWithApplianceTypes();
		whenGettingEndpointsMetadata();
		thanHave3Endpoints();
	}

	@Test
	public void shouldGetEmptyEndpointsMetadataListWhenApplianceTypesWithoutEndpoints()
			throws Exception {
		givenAriContentWithoutEndpoints();
		whenGettingEndpointsMetadata();
		thanHave0Endpoints();
	}

	@Test
	public void shouldGetEmptyEndpointMetadataListWhenNoApplianceTypes()
			throws Exception {
		givenAirContentWithoutApplianceTypes();
		whenGettingEndpointsMetadata();
		thanHave0Endpoints();
	}

	private void thanHave0Endpoints() {
		assertEquals(metadatas.size(), 0);
	}

	private void thanHave3Endpoints() {
		assertEquals(metadatas.size(), 3);
		compareEndpointMetadata(metadatas.get(0), "endpoint1");
		compareEndpointMetadata(metadatas.get(1), "endpoint2");
		compareEndpointMetadata(metadatas.get(2), "endpoint3");
	}

	/**
	 * @param metadata
	 * @param string
	 */
	private void compareEndpointMetadata(Endpoint metadata, String endpointName) {
		assertEquals(metadata.getDescription(), getDescription(endpointName));
		assertEquals(metadata.getInvocationPath(),
				getInvocationPath(endpointName));
		assertEquals(metadata.getPort(), 80);
	}

	private void whenGettingEndpointsMetadata() {
		metadatas = manager.getEndpoints();
	}

	private void givenAirContentWithApplianceTypes() {

		ATEndpoint endpoint1 = getEndpoint("endpoint1", 80);
		ATEndpoint endpoint2 = getEndpoint("endpoint2", 80);

		ApplianceType at1 = new ApplianceType();
		at1.setName("as1");
		at1.setEndpoints(Arrays.asList(endpoint1, endpoint2));

		ATEndpoint endpoint3 = getEndpoint("endpoint3", 80);

		ApplianceType at2 = new ApplianceType();
		at2.setName("as2");
		at2.setEndpoints(Arrays.asList(endpoint3));

		ApplianceType at3 = new ApplianceType();

		ApplianceType at4 = new ApplianceType();
		at4.setEndpoints(Collections.<ATEndpoint> emptyList());

		when(air.getApplianceTypes()).thenReturn(
				Arrays.asList(at1, at2, at3, at4));
	}

	private void givenAriContentWithoutEndpoints() {
		ApplianceType at1 = new ApplianceType();
		ApplianceType at2 = new ApplianceType();

		when(air.getApplianceTypes()).thenReturn(Arrays.asList(at1, at2));
	}

	private void givenAirContentWithoutApplianceTypes() {
		when(air.getApplianceTypes()).thenReturn(
				Collections.<ApplianceType> emptyList());
	}

	private ATEndpoint getEndpoint(String endpointName, int port) {
		ATEndpoint endpoint = new ATEndpoint();
		endpoint.setDescription(getDescription(endpointName));
		endpoint.setPort(port);
		endpoint.setInvocation_path(getInvocationPath(endpointName));

		return endpoint;
	}

	private String getDescription(String endpointName) {
		return String.format("%s description", endpointName);
	}

	private String getInvocationPath(String endpointName) {
		return String.format("/%s/path", endpointName);
	}
}
