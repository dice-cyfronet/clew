package pl.cyfronet.coin.impl.action.portmapping;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.exception.RedirectionNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;

public class RemovePortMappingActionTest extends ActionTest {

	private String redirectionId = "redirectionId";

	@Test
	public void shouldRemovePortMapping() throws Exception {
		whenRemovePortMapping();
		thenPortMappingRemoved();
	}

	private void whenRemovePortMapping() {
		Action<Class<Void>> action = new RemovePortMappingAction(air,
				redirectionId);
		action.execute();
	}

	private void thenPortMappingRemoved() {
		verify(air, times(1)).removePortMapping(redirectionId);
	}

	@Test
	public void shouldThrown404WhileRedirectionDoesNotExist() throws Exception {
		givenNonExistingRedirection();
		try {
			whenRemovePortMapping();
			fail();
		} catch (RedirectionNotFoundException e) {
			// OK should be thrown
		}
	}

	private void givenNonExistingRedirection() {
		doThrow(getAirException(404)).when(air)
				.removePortMapping(redirectionId);
	}
}
