package pl.cyfronet.coin.impl.action.grant;

import static org.testng.Assert.*;
import static org.mockito.Mockito.*;

import javax.ws.rs.WebApplicationException;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.Grant;
import pl.cyfronet.coin.api.exception.GrantAlreadyExistException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 *
 */
public class UpdateGrantActionTest extends ActionTest {

	private String grantName = "my/grant";
	private Grant grant;

	@Test
	public void shouldCreateNewGrant() throws Exception {
		givenAIRWithoutGrant();
		whenUpdateGrant(false);
		thenGrantAddedOrUpdated(false);
	}

	private void givenAIRWithoutGrant() {
		createGrant();
		when(
				air.updateGrant(grantName, grant.getGet(), grant.getPost(),
						grant.getPut(), grant.getDelete(), false)).thenReturn(
				"internalAIRGrantId");
	}

	private void createGrant() {
		grant = new Grant();
		grant.setDelete("delete");
		grant.setGet("get");
		grant.setPost("post");
		grant.setPut("put");
	}

	private void whenUpdateGrant(boolean overwrite) {
		Action<Class<Void>> action = actionFactory.createUpdateGrantAction(grantName, grant, overwrite);
		action.execute();
	}

	private void thenGrantAddedOrUpdated(boolean overwrite) {
		verify(air, times(1)).updateGrant(grantName, grant.getGet(),
				grant.getPost(), grant.getPut(), grant.getDelete(), overwrite);
	}

	@Test
	public void shouldThrow409WhileAddingGrantWithExistingName()
			throws Exception {
		givenAIRWithExistingGrant();
		try {
			whenUpdateGrant(false);
			fail();
		} catch (GrantAlreadyExistException e) {
			// OK should be thrown
		}
	}

	private void givenAIRWithExistingGrant() {
		when(
				air.updateGrant(grantName, grant.getGet(), grant.getPost(),
						grant.getPut(), grant.getDelete(), false)).thenThrow(
				new WebApplicationException(400));
	}

	@Test
	public void shouldUpdateGrant() throws Exception {
		givenAIRWithExistingGrant();
		whenUpdateGrant(true);
		thenGrantAddedOrUpdated(true);
	}
}
