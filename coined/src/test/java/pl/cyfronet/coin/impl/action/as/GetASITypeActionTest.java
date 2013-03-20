package pl.cyfronet.coin.impl.action.as;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

public class GetASITypeActionTest extends ActionTest {

	ApplianceType givenType;
	ApplianceType type;
	private String instanceId = "instanceId";

	@Test
	public void shouldGetASIType() throws Exception {
		givenATInAIR();
		whenGetASIType();
		thenASITypeLoaded();
	}

	private void givenATInAIR() {
		givenType = new ApplianceType();
		givenType.setName("AT");

		when(air.getTypeFromVM(instanceId)).thenReturn(givenType);
	}

	private void whenGetASIType() {
		Action<ApplianceType> action = actionFactory
				.createGetASITypeAction(instanceId);
		type = action.execute();
	}

	private void thenASITypeLoaded() {
		assertNotNull(type);
		assertEquals(type.getName(), givenType.getName());
	}

	@Test
	public void shouldThrow404WhenASINotFound() throws Exception {
		givenAIRWithoutASI();
		try {
			whenGetASIType();
			fail();
		} catch (AtomicServiceInstanceNotFoundException e) {
			// OK should be thrown.
		}
	}

	private void givenAIRWithoutASI() {
		when(air.getTypeFromVM(instanceId)).thenThrow(getAirException(400));
	}
}
