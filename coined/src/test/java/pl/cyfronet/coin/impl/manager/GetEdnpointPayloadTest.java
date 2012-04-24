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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Arrays;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.EndpointNotFoundException;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetEdnpointPayloadTest extends AbstractManagerWithAirTest {

	private int servicePort = 80;
	private String existingInvocationPath = "/my/path";
	private String asName = "as";
	private String descriptorValue = "my descriptor value";

	private String receivedDescriptor;
	private String endpointId;

	@Test
	public void shouldGetEndpointPayload() throws Exception {
		givenAIRContent();
		whenGetExistingDescriptorValueFor(asName, servicePort,
				existingInvocationPath);
		thanDescriptorValueIsEqual(receivedDescriptor);
	}

	@Test
	public void shouldFailedWhenAskingForPayloadFromNonExistingAS()
			throws Exception {
		givenAIRContent();
		try {
			whenGetExistingDescriptorValueFor("nonExist", servicePort,
					existingInvocationPath);
			fail();
		} catch (AtomicServiceNotFoundException e) {
			// should be thrown
		}
		thanVerifyGetASAirInvocation();
	}

	@DataProvider(name = "getNonExistingEndpointIdentificationPortAndPath")
	protected Object[][] getNonExistingEndpointIdentificationPortAndPath() {
		int nonExistingServicePort = 90;
		return new Object[][] { { servicePort, "nonExisting" },
				{ nonExistingServicePort, existingInvocationPath } };
	}

	@Test(dataProvider = "getNonExistingEndpointIdentificationPortAndPath")
	public void shouldThrowExceptionWhenEndpointNotFound(int wrongServicePort,
			String wrongInvocationPath) throws Exception {
		givenAIRContent();
		try {
			whenGetExistingDescriptorValueFor(asName, wrongServicePort,
					wrongInvocationPath);
			fail();
		} catch (EndpointNotFoundException e) {
			// should be thrown
		}
		thanVerifyGetASAirInvocation();
	}

	private void thanDescriptorValueIsEqual(String receivedDescriptorValue) {
		thanVerifyGetPayloadAirInvocations();
		assertEquals(receivedDescriptorValue, descriptorValue);
	}

	private void thanVerifyGetPayloadAirInvocations() {
		thanVerifyGetASAirInvocation();
		verify(air, times(1)).getEndpointDescriptor(endpointId);
	}

	private void thanVerifyGetASAirInvocation() {
		verify(air, times(1)).getApplianceTypes();
	}

	private void whenGetExistingDescriptorValueFor(String asName,
			int servicePort, String path)
			throws AtomicServiceInstanceNotFoundException,
			EndpointNotFoundException {
		receivedDescriptor = manager.getEndpointPayload(asName, servicePort,
				path);
	}

	private void givenAIRContent() {
		ApplianceType at1 = new ApplianceType();
		at1.setName(asName);

		ATEndpoint endpoint1 = new ATEndpoint();
		endpoint1.setPort(servicePort);
		endpoint1.setInvocation_path(existingInvocationPath);
		endpoint1.setId(endpointId);

		ATEndpoint endpoint2 = new ATEndpoint();
		endpoint2.setPort(80);
		endpoint2.setInvocation_path("other/path");

		at1.setEndpoints(Arrays.asList(endpoint1, endpoint2));

		ApplianceType at2 = new ApplianceType();

		when(air.getApplianceTypes()).thenReturn(Arrays.asList(at1, at2));
		when(air.getEndpointDescriptor(endpointId)).thenReturn(descriptorValue);

	}
}
