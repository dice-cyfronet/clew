package pl.cyfronet.coin.impl.action.endpoint;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.impl.CoinedAsserts;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.workflow.WorkflowActionTest;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.Vms;

public class ListAsiEndpointsActionTest extends WorkflowActionTest {

	private String atId = "atId";
	private String asiId = "asiId";
	private List<Endpoint> endpoints;
	private ApplianceType at;

	@Test
	public void shouldListAsiEndpoints() throws Exception {
		givenAsiWithEndpoints();
		whenGetAsiEndpoints();
		thenAsiEndpointsReceived();
	}

	private void givenAsiWithEndpoints() {
		givenAsiAndAT();
		at.setEndpoints(Arrays.asList(getATEndpoint("e1", "ws"),
				getATEndpoint("e2", "rest"), getATEndpoint("e3", "webapp")));
	}

	private void givenAsiAndAT() {
		givenWorkflowStarted();

		Vms asi = new Vms();
		asi.setVms_id(asiId);
		asi.setAppliance_type(atId);

		workflowDetails.setVms(Arrays.asList(asi));

		at = new ApplianceType();
		at.setId(atId);

		when(air.getApplianceTypes(true)).thenReturn(Arrays.asList(at));
	}

	private ATEndpoint getATEndpoint(String name, String type) {
		ATEndpoint endpoint = new ATEndpoint();
		endpoint.setDescription(name + " description");
		endpoint.setPort(80);
		endpoint.setId(name + "Id");
		endpoint.setDescriptor(name + " descriptor");
		endpoint.setEndpoint_type(type);
		endpoint.setInvocation_path("/" + name);

		return endpoint;
	}

	private void whenGetAsiEndpoints() {
		Action<List<Endpoint>> action = actionFactory
				.createListAsiEndpointsAction(username, contextId, asiId);
		endpoints = action.execute();
	}

	private void thenAsiEndpointsReceived() {
		CoinedAsserts.assertEndpoints(endpoints, at.getEndpoints(), at
				.getEndpoints().get(0).getDescriptor(), at.getEndpoints()
				.get(1).getDescriptor(), at.getEndpoints().get(2)
				.getDescriptor());
	}

	@Test
	public void shouldReturnEmptyListWhenNoEndpoints() throws Exception {
		givenAsiWithoutEndpoints();
		whenGetAsiEndpoints();
		thenEmptyEndpointsList();

	}

	private void givenAsiWithoutEndpoints() {
		givenAsiAndAT();
	}

	private void thenEmptyEndpointsList() {
		assertEquals(endpoints.size(), 0);
	}
}
