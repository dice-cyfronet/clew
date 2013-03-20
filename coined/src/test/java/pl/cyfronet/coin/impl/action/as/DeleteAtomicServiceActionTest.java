package pl.cyfronet.coin.impl.action.as;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.exception.NotAcceptableException;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;
import pl.cyfronet.dyrealla.api.DyReAllaException;
import pl.cyfronet.dyrealla.api.RemoveTemplatesResponse;
import pl.cyfronet.dyrealla.api.allocation.OperationStatus;
import pl.cyfronet.dyrealla.api.impl.RemoveTemplatesResponseImpl;

@SuppressWarnings("unchecked")
public class DeleteAtomicServiceActionTest extends ActionTest {

	private String asId = "asId";
	private Action<Class<Void>> deleteAsFromAirAction;
	private String username = "marek";

	@Test
	public void shouldRemoveOwnedAtomicService() throws Exception {
		givenAtomicServiceBelongingToTheUser();
		whenDeleteAtomicService();
		thenAtomicServiceRemoved();
	}

	private void givenAtomicServiceBelongingToTheUser() throws Exception {
		givenAtomicServiceBelongingTo(username);
	}
	
	private void givenAtomicServiceBelongingTo(String username)
			throws Exception {
		givenGetAtomicService(username);
		deleteAsFromAirAction = mock(Action.class);
		when(actionFactory.createDeleteAtomicServiceFromAirAction(asId))
				.thenReturn(deleteAsFromAirAction);
		givenDyreallaResponse(OperationStatus.SUCCESSFUL);
	}

	private void givenGetAtomicService(String username) {
		AtomicService as = new AtomicService();
		as.setOwner(username);
		Action<AtomicService> action = mock(Action.class);
		when(action.execute()).thenReturn(as);
		when(actionFactory.createGetAtomicServiceAction(asId)).thenReturn(
				action);
	}

	private void givenDyreallaResponse(OperationStatus status) throws Exception {
		RemoveTemplatesResponse resp = new RemoveTemplatesResponseImpl();
		resp.setOperationStatus(status);

		when(atmosphere.removeTemplatesOfApplianceType(asId)).thenReturn(resp);
	}

	private void whenDeleteAtomicService() {
		whenDeleteAtomicService(false);
	}

	private void whenDeleteAtomicService(boolean admin) {
		Action<Class<Void>> action = actionFactory
				.createDeleteAtomicServiceAction(username, asId, admin);
		action.execute();
	}

	private void thenAtomicServiceRemoved() throws DyReAllaException {
		verify(atmosphere, times(1)).removeTemplatesOfApplianceType(asId);
		verify(deleteAsFromAirAction, times(1)).execute();
	}

	@Test
	public void shouldThrowExceptionWhileUnableToRemoveASFromDyrealla()
			throws Exception {
		givenDyreallaReturnFailedStatusWhileRemovingAS();
		try {
			whenDeleteAtomicService();
			fail();
		} catch (NotAcceptableException e) {
			// OK should be thrown.
		}
	}

	private void givenDyreallaReturnFailedStatusWhileRemovingAS()
			throws Exception {
		givenGetAtomicService(username);
		givenDyreallaResponse(OperationStatus.FAILED);
	}

	@Test
	public void shouldThrowWhileDeletingNotOwnedAS() throws Exception {
		givenASBelongingToOtherUser();
		try {
			whenDeleteAtomicService();
			fail();
		} catch (NotAllowedException e) {
			// Ok should be thrown
		}
	}

	private void givenASBelongingToOtherUser() throws Exception {
		givenAtomicServiceBelongingTo("otherUser");
	}

	@Test
	public void shouldRemoveNoOwnedASWhenHasAdminRole() throws Exception {
		givenASBelongingToOtherUser();
		whenDeleteAtomicServiceAsAdmin();
		thenAtomicServiceRemoved();
	}

	private void whenDeleteAtomicServiceAsAdmin() {
		whenDeleteAtomicService(true);
	}
}
