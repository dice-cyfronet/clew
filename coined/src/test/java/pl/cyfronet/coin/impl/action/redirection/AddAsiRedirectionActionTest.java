package pl.cyfronet.coin.impl.action.redirection;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Arrays;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.RedirectionType;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.WorkflowActionTest;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.dyrealla.api.dnat.DyReAllaDNATManagerService;
import pl.cyfronet.dyrealla.api.dnat.Protocol;
import pl.cyfronet.dyrealla.api.proxy.DyReAllaProxyManagerService;

public class AddAsiRedirectionActionTest extends WorkflowActionTest {

	private DyReAllaProxyManagerService httpRedirectionService;
	private DyReAllaDNATManagerService dnatRedirectionService;

	private String asiId = "asId";
	private String serviceName = "myRedirection";
	private int port = 123;
	private String atId = "developmentApplianceTypeId";
	private String givenRedirectionId = "rId";
	private String redirectionId;

	@Override
	protected void postSetUp() {
		httpRedirectionService = mock(DyReAllaProxyManagerService.class);
		dnatRedirectionService = mock(DyReAllaDNATManagerService.class);

		actionFactory.setHttpRedirectionService(httpRedirectionService);
		actionFactory.setDnatRedirectionService(dnatRedirectionService);
	}

	@Test
	public void shouldCreateHttpRedirection() throws Exception {
		givenAsiInDevelopmentMode();
		whenAddAsiHttpRedirection();
		thenRedirectionAddedToAirAndRegisteredInProxy();
	}

	private void givenAsiInDevelopmentMode() {
		givenWorkflowStarted(WorkflowType.development);

		Vms asi = new Vms();
		asi.setVms_id(asiId);
		asi.setAppliance_type(atId);
		workflowDetails.setVms(Arrays.asList(asi));

		when(
				air.addPortMapping(eq("rest"), eq(atId), eq(serviceName), eq(port),
						anyBoolean())).thenReturn(givenRedirectionId);
	}

	private void whenAddAsiHttpRedirection() {
		addAsiRedirection(RedirectionType.HTTP);
	}

	private void addAsiRedirection(RedirectionType type) {
		Action<String> action = actionFactory.createAddAsiRedirectionAction(
				username, contextId, asiId, serviceName, port, type);
		redirectionId = action.execute();
	}

	private void thenRedirectionAddedToAirAndRegisteredInProxy()
			throws Exception {
		verify(air, times(1)).addPortMapping("rest", atId, serviceName, port, true);
		verify(httpRedirectionService, times(1)).registerHttpService(contextId,
				asiId, port, serviceName);
		assertEquals(redirectionId, givenRedirectionId);
	}

	@Test
	public void shouldCreateTCPRedirection() throws Exception {
		givenAsiInDevelopmentMode();
		whenAddAsiTCPRedirection();
		thenRedirectionAddedToAirAndRegisteredInDnat();
	}

	private void whenAddAsiTCPRedirection() {
		addAsiRedirection(RedirectionType.TCP);
	}

	private void thenRedirectionAddedToAirAndRegisteredInDnat()
			throws Exception {
		verify(air, times(1)).addPortMapping("rest", atId, serviceName, port, false);
		verify(dnatRedirectionService, times(1)).addPortRedirection(asiId,
				port, Protocol.TCP, serviceName);
		assertEquals(redirectionId, givenRedirectionId);
	}

	@Test
	public void shouldThrowWhenAddingRedirectionIntoNonDevelopmentAsi()
			throws Exception {
		givenWorkflowNotInDevelopmentMode();
		try {
			whenAddAsiHttpRedirection();
			fail();
		} catch (WorkflowNotInDevelopmentModeException e) {
			// OK should be thrown
		}
	}

	private void givenWorkflowNotInDevelopmentMode() {
		givenWorkflowStarted(WorkflowType.portal);
	}

	@Test
	public void shouldThrown404WhenAsiNotFound() throws Exception {
		givenWorkflowWithoutAsi();
		try {
			whenAddAsiHttpRedirection();
			fail();
		} catch (AtomicServiceInstanceNotFoundException e) {
			// OK should be thrown
		}
	}

	private void givenWorkflowWithoutAsi() {
		givenWorkflowStarted(WorkflowType.development);
	}
}
