package pl.cyfronet.coin.impl.action;

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

public class GetEndpointPayloadActionTest extends ActionTest {

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
		GetEndpointPayloadAction action = actionFactory.createGetEndpointPayloadAction(
				asName, servicePort, path);
		receivedDescriptor = action.execute();
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