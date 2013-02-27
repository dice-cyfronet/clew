package pl.cyfronet.coin.impl.action.endpoint;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.fail;

import java.util.Arrays;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.EndpointNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.WorkflowActionTest;
import pl.cyfronet.coin.impl.air.client.Vms;

public class RemoveAsiEndpointActionTest extends WorkflowActionTest {

	private String asiId = "asiId";
	private String atId = "atId";
	private String endpointId = "endpointId";

	@Test
	public void shouldRemoveAsiEndpoint() throws Exception {
		givenAsi();
		whenRemoveAsiEndpoint();
		thenAsiEndpointRemoved();
	}

	private void givenAsi() {
		givenWorkflowStarted(WorkflowType.development);

		Vms vm = new Vms();
		vm.setVms_id(asiId);
		vm.setAppliance_type(atId);
		workflowDetails.setVms(Arrays.asList(vm));
	}

	private void whenRemoveAsiEndpoint() {
		Action<Class<Void>> action = actionFactory
				.createRemoveAsiEndpointAction(username, contextId, asiId,
						endpointId);
		action.execute();
	}

	private void thenAsiEndpointRemoved() {
		verify(air, times(1)).removeEndpoint(endpointId);
	}

	@Test
	public void shouldThrowExceptionWhileAsiNotBelongToUser() throws Exception {
		givenAsiNotBelongingToTheUser();
		try {
			whenRemoveAsiEndpoint();
			fail();
		} catch (WorkflowNotFoundException e) {
			// OK should be thrown
		}
	}

	private void givenAsiNotBelongingToTheUser() {
		givenWorkflowStarted(WorkflowType.development);
		username = "differentuser";
	}

	@Test
	public void shouldThrowExceptionWhileAsiNotExist() throws Exception {
		givenWorkflowStarted(WorkflowType.development);
		try {
			whenRemoveAsiEndpoint();
			fail();
		} catch (AtomicServiceInstanceNotFoundException e) {
			// OK should be thrown
		}
	}

	@Test
	public void shouldThrowExceptionWhenWorkflowNotInDevelopment()
			throws Exception {
		givenWorkflowStarted(WorkflowType.portal);
		try {
			whenRemoveAsiEndpoint();
			fail();
		} catch (WorkflowNotInDevelopmentModeException e) {
			// OK should be thrown
		}
	}

	@Test
	public void shouldThrow404WhenEndpointNotFound() throws Exception {
		givenAsiWithoutEndpoint();
		try {
			whenRemoveAsiEndpoint();
			fail();
		} catch (EndpointNotFoundException e) {
			// OK should be thrown.
		}
	}

	private void givenAsiWithoutEndpoint() {
		givenAsi();
		doThrow(getAirException(400)).when(air).removeEndpoint(endpointId);
	}
}
