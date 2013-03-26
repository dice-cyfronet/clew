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
import static org.testng.Assert.fail;

import java.util.Arrays;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.EndpointNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.ATPortMapping;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetEndpointPayloadActionTest extends ActionTest {

	private String serviceName = "serviceName";
	private String existingInvocationPath = "/my/path";
	private String asId = "as";
	private String descriptorValue = "my descriptor value";

	private String receivedDescriptor;
	private String endpointId;

	@Test
	public void shouldGetEndpointPayload() throws Exception {
		givenAIRContent();
		whenGetExistingDescriptorValueFor(asId, serviceName,
				existingInvocationPath);
		thanDescriptorValueIsEqual(receivedDescriptor);
	}

	@Test
	public void shouldFailedWhenAskingForPayloadFromNonExistingAS()
			throws Exception {
		givenAIRContent();
		try {
			whenGetExistingDescriptorValueFor("nonExist", serviceName,
					existingInvocationPath);
			fail();
		} catch (AtomicServiceNotFoundException e) {
			// should be thrown
		}
	}

	@DataProvider(name = "getNonExistingEndpointIdentificationPortAndPath")
	protected Object[][] getNonExistingEndpointIdentificationPortAndPath() {
		return new Object[][] { { serviceName, "nonExisting" },
				{ "nonExistingService", existingInvocationPath } };
	}

	@Test(dataProvider = "getNonExistingEndpointIdentificationPortAndPath")
	public void shouldThrowExceptionWhenEndpointNotFound(
			String wrongServiceName, String wrongInvocationPath)
			throws Exception {
		givenAIRContent();
		try {
			whenGetExistingDescriptorValueFor(asId, wrongServiceName,
					wrongInvocationPath);
			fail();
		} catch (EndpointNotFoundException e) {
			// should be thrown
		}
	}

	private void thanDescriptorValueIsEqual(String receivedDescriptorValue) {
		thanVerifyGetASAirInvocation();
		assertEquals(receivedDescriptorValue, descriptorValue);
	}

	private void thanVerifyGetASAirInvocation() {
		verify(air, times(1)).getApplianceTypes(asId, true);
	}

	private void whenGetExistingDescriptorValueFor(String asName,
			String serviceName, String path)
			throws AtomicServiceInstanceNotFoundException,
			EndpointNotFoundException {
		Action<String> action = actionFactory.createGetEndpointPayloadAction(
				asName, serviceName, path);
		receivedDescriptor = action.execute();
	}

	private void givenAIRContent() {
		ApplianceType at1 = new ApplianceType();
		at1.setId(asId);

		ATEndpoint endpoint1 = new ATEndpoint();
		endpoint1.setPort(81);
		endpoint1.setInvocation_path(existingInvocationPath);
		endpoint1.setId(endpointId);
		endpoint1.setDescriptor(descriptorValue);
		
		ATEndpoint endpoint2 = new ATEndpoint();
		endpoint2.setPort(80);
		endpoint2.setInvocation_path("other/path");

		at1.setEndpoints(Arrays.asList(endpoint1, endpoint2));

		ATPortMapping portMapping = new ATPortMapping();
		portMapping.setHttp(true);
		portMapping.setPort(81);
		portMapping.setService_name(serviceName);

		at1.setPort_mappings(Arrays.asList(portMapping));

		ApplianceType at2 = new ApplianceType();

		when(air.getApplianceTypes(asId, true)).thenReturn(Arrays.asList(at1, at2));
	}
}
