package pl.cyfronet.coin.impl.action.portmapping;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;

public class AddPortMappingActionTest extends ActionTest {

	private String asId = "asId";
	private String serviceName = "my_service";
	private int port = 80;
	private boolean http = true;
	private String givenRedirectionId = "redirectionId";
	private String redirectionId;

	@Test
	public void shouldAddNewRedirection() throws Exception {
		givenExistingAS();
		whenAddPortMapping();
		thenRedirectionAdded();
	}

	private void givenExistingAS() {
		// FIXME waiting for #1334 point 3
		when(air.addPortMapping("rest", asId, serviceName, port, http)).thenReturn(
				givenRedirectionId);
	}

	private void whenAddPortMapping() {
		Action<String> action = new AddPortMappingAction(air, asId,
				serviceName, port, http);
		redirectionId = action.execute();
	}

	private void thenRedirectionAdded() {
		verify(air, times(1)).addPortMapping("rest", asId, serviceName, port, http);
		assertEquals(redirectionId, givenRedirectionId);
	}

	@Test
	public void shouldThrow404WhenASNotFound() throws Exception {
		givenNonExistingAS();
		try {
			whenAddPortMapping();
			fail();
		} catch (AtomicServiceNotFoundException e) {
			// OK should be thrown
		}
	}

	private void givenNonExistingAS() {
		when(air.addPortMapping("rest", asId, serviceName, port, http)).thenThrow(
				getAirException(404));
	}
}
