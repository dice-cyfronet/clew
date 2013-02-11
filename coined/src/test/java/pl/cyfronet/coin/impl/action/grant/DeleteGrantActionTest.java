package pl.cyfronet.coin.impl.action.grant;

import static org.testng.Assert.*;
import static org.mockito.Mockito.*;

import javax.ws.rs.WebApplicationException;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.exception.GrantNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;

public class DeleteGrantActionTest extends ActionTest {

	private String grantName = "my/grant";

	@Test
	public void shouldDeleteExistingGrant() throws Exception {
		whenDeleteGrant();
		thenGrantDeleted();
	}

	private void whenDeleteGrant() {
		Action<Class<Void>> action = actionFactory
				.createDeleteGrantAction(grantName);
		action.execute();
	}

	private void thenGrantDeleted() {
		verify(air, times(1)).deleteGrant(grantName);
	}

	@Test
	public void shouldThrow404WhenDeletingNonExistingGrant() throws Exception {
		givenAIRWithoutGrant();
		try {
			whenDeleteGrant();
			fail();
		} catch (GrantNotFoundException e) {
			// OK should be thrown
		}
	}

	private void givenAIRWithoutGrant() {
		doThrow(new WebApplicationException(400)).when(air).deleteGrant(
				grantName);
	}
}
