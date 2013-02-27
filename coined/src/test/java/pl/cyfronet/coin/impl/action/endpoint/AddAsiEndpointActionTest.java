package pl.cyfronet.coin.impl.action.endpoint;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Arrays;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.EndpointType;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.WorkflowActionTest;
import pl.cyfronet.coin.impl.air.client.Vms;

public class AddAsiEndpointActionTest extends WorkflowActionTest {

	private String asiId = "asiId";
	private String atId = "atId";
	private String givenEndpointId = "eId";
	private Endpoint endpoint;

	private String endpointId;

	@Test
	public void shouldAddAsiEndpoint() throws Exception {
		givenAsiStartedInDevelopment();
		whenAddingNewEndpoint();
		thenEndpointAdded();
	}

	private void givenAsiStartedInDevelopment() {
		givenWorkflowStarted(WorkflowType.development);

		Vms vm = new Vms();
		vm.setVms_id(asiId);
		vm.setAppliance_type(atId);
		workflowDetails.setVms(Arrays.asList(vm));

		endpoint = new Endpoint();
		endpoint.setDescription("description");
		endpoint.setDescriptor("descriptor");
		endpoint.setInvocationPath("/invocation_path");
		endpoint.setPort(80);
		endpoint.setType(EndpointType.REST);

		when(
				air.addEndpoint("rest", atId, endpoint.getInvocationPath(),
						endpoint.getType(), endpoint.getDescription(),
						endpoint.getDescriptor(), endpoint.getPort()))
				.thenReturn(givenEndpointId);
	}

	private void whenAddingNewEndpoint() {
		Action<String> action = actionFactory.createAddAsiEndpointAction(
				username, contextId, asiId, endpoint);
		endpointId = action.execute();
	}

	private void thenEndpointAdded() {
		verify(air, times(1)).addEndpoint("rest", atId,
				endpoint.getInvocationPath(), endpoint.getType(),
				endpoint.getDescription(), endpoint.getDescriptor(),
				endpoint.getPort());
		assertEquals(endpointId, givenEndpointId);
	}
	
	@Test
	public void shouldThrowExceptionWhileWorkflowNotInDevelopment() throws Exception {
		givenWorkflowStarted(WorkflowType.portal);
		try {
			whenAddingNewEndpoint();
			fail();
		} catch(WorkflowNotInDevelopmentModeException e) {
			// OK should be thrown.
		}
	}
}
