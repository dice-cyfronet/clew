package pl.cyfronet.coin.impl.action.endpoint;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.beans.redirection.HttpRedirection;
import pl.cyfronet.coin.api.beans.redirection.Redirections;
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
		at.setEndpoints(Arrays.asList(getATEndpoint("e1", "ws", 80),
				getATEndpoint("e2", "rest", 81),
				getATEndpoint("e3", "webapp", 82)));
	}

	@SuppressWarnings("unchecked")
	private void givenAsiAndAT() {
		givenWorkflowStarted(WorkflowType.development);

		Vms asi = new Vms();
		asi.setVms_id(asiId);
		asi.setAppliance_type(atId);

		workflowDetails.setVms(Arrays.asList(asi));

		at = new ApplianceType();
		at.setId(atId);

		when(air.getApplianceTypes(atId, true)).thenReturn(Arrays.asList(at));

		Action<Redirections> action = mock(Action.class);
		Redirections redirections = new Redirections();
		HttpRedirection r1 = new HttpRedirection();
		r1.setName("e1");
		r1.setToPort(80);
		r1.setUrls(Arrays.asList("http://redirection/http"));

		HttpRedirection r2 = new HttpRedirection();
		r2.setName("e2");
		r2.setToPort(81);
		r2.setUrls(Arrays.asList("https://redirection/https"));

		HttpRedirection r3 = new HttpRedirection();
		r3.setName("e3");
		r3.setToPort(82);
		r3.setUrls(Arrays.asList("http://redirection/http",
				"https://redirection/https"));

		redirections.setHttp(Arrays.asList(r1, r2, r3));

		when(action.execute()).thenReturn(redirections);
		when(
				actionFactory.createGetAsiRedirectionsAction(username,
						contextId, asiId)).thenReturn(action);
	}

	private ATEndpoint getATEndpoint(String name, String type, int port) {
		ATEndpoint endpoint = new ATEndpoint();
		endpoint.setDescription(name + " description");
		endpoint.setPort(port);
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
	public void shouldGetEndpointWithDirectUrls() throws Exception {
		givenAsiWithEndpoints();
		whenGetAsiEndpoints();
		thenEndpointWithDirectUrlsLoaded();
	}

	private void thenEndpointWithDirectUrlsLoaded() {
		assertEquals(endpoints.get(0).getUrls().get(0),
				"http://redirection/http/e1");
		assertEquals(endpoints.get(1).getUrls().get(0),
				"https://redirection/https/e2");
		assertEquals(endpoints.get(2).getUrls().get(0),
				"http://redirection/http/e3");
		assertEquals(endpoints.get(2).getUrls().get(1),
				"https://redirection/https/e3");
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
