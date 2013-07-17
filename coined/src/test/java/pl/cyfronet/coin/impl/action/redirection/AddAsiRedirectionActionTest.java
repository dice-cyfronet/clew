package pl.cyfronet.coin.impl.action.redirection;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Arrays;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.RedirectionType;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.dyrealla.api.dnat.Protocol;
import pl.cyfronet.dyrealla.api.proxy.HttpProtocol;

public class AddAsiRedirectionActionTest extends AsiRedirectionActionTest {

	private String serviceName = "myRedirection";
	private String atId = "developmentApplianceTypeId";
	private String givenRedirectionId = "rId";
	private String redirectionId;

	@DataProvider
	Object[][] getHttpType() {
		return new Object[][] { { RedirectionType.HTTP },
				{ RedirectionType.HTTPS } };
	}

	@Test(dataProvider = "getHttpType")
	public void shouldCreateHttpRedirection(RedirectionType type)
			throws Exception {
		givenAsiInDevelopmentMode();
		whenAddAsiRedirection(type);
		thenRedirectionAddedToAirAndRegisteredInProxy(type);
	}

	private void givenAsiInDevelopmentMode() {
		givenWorkflowStarted(WorkflowType.development);

		Vms asi = new Vms();
		asi.setVms_id(asiId);
		asi.setAppliance_type(atId);
		workflowDetails.setVms(Arrays.asList(asi));

		when(
				air.addPortMapping(eq("rest"), eq(atId), eq(serviceName),
						eq(port), anyBoolean(), anyBoolean())).thenReturn(
				givenRedirectionId);
	}

	private void whenAddAsiHttpRedirection() {
		whenAddAsiRedirection(RedirectionType.HTTP);
	}

	private void whenAddAsiRedirection(RedirectionType type) {
		Action<String> action = actionFactory.createAddAsiRedirectionAction(
				username, contextId, asiId, serviceName, port, type);
		redirectionId = action.execute();
	}

	private void thenRedirectionAddedToAirAndRegisteredInProxy(
			RedirectionType type) throws Exception {
		verify(air, times(1)).addPortMapping("rest", atId, serviceName, port,
				type == RedirectionType.HTTP, type == RedirectionType.HTTPS);
		verify(httpRedirectionService, times(1)).registerHttpService(contextId,
				asiId, port, serviceName, getHttpProtocol(type));
		assertEquals(redirectionId, givenRedirectionId);
	}

	private HttpProtocol getHttpProtocol(RedirectionType type) {
		if (type == RedirectionType.HTTPS) {
			return HttpProtocol.HTTPS;
		} else {
			return HttpProtocol.HTTP;
		}
	}

	@Test
	public void shouldCreateTCPRedirection() throws Exception {
		givenAsiInDevelopmentMode();
		whenAddAsiTCPRedirection();
		thenRedirectionAddedToAirAndRegisteredInDnat();
	}

	private void whenAddAsiTCPRedirection() {
		whenAddAsiRedirection(RedirectionType.TCP);
	}

	private void thenRedirectionAddedToAirAndRegisteredInDnat()
			throws Exception {
		verify(air, times(1)).addPortMapping("rest", atId, serviceName, port,
				false, false);
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
