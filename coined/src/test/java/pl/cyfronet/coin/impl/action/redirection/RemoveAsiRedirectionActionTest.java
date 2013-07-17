package pl.cyfronet.coin.impl.action.redirection;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.air.client.ATPortMapping;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.dyrealla.api.dnat.Protocol;
import pl.cyfronet.dyrealla.api.proxy.HttpProtocol;

public class RemoveAsiRedirectionActionTest extends AsiRedirectionActionTest {

	private String redirectionId = "redirectionId";
	private String atId = "applianceTypeId";
	private String serviceName = "serviceName";

	@Test
	public void shouldRemoveHttpRedirection() throws Exception {
		givenAsiInDevelopmentModeWithHttpRedirection();
		whenRemoveRedirection();
		thenHttpRedirectionRemoved();
	}

	private void givenAsiInDevelopmentModeWithHttpRedirection() {
		givenAsiInDevelopmentModeWithRedirection(true, false);
	}

	private void givenAsiInDevelopmentModeWithRedirection(boolean http, boolean https) {
		givenWorkflowStarted(WorkflowType.development);

		Vms vm = new Vms();
		vm.setVms_id(asiId);
		vm.setAppliance_type(atId);
		workflowDetails.setVms(Arrays.asList(vm));

		ApplianceType at = new ApplianceType();
		at.setId(atId);

		ATPortMapping portMapping = new ATPortMapping();
		portMapping.setHttp(http);
		portMapping.setHttps(https);
		portMapping.setId(redirectionId);
		portMapping.setPort(port);
		portMapping.setService_name(serviceName);

		at.setPort_mappings(Arrays.asList(portMapping));

		when(air.getApplianceTypes(atId, false)).thenReturn(Arrays.asList(at));
	}

	private void whenRemoveRedirection() {
		Action<Class<Void>> action = actionFactory
				.createRemoveAsiRedirectionAction(username, contextId, asiId,
						redirectionId);
		action.execute();
	}

	private void thenHttpRedirectionRemoved() throws Exception {
		verify(air).removePortMapping(redirectionId);
		verify(httpRedirectionService).unregisterHttpService(contextId, asiId,
				serviceName, port, HttpProtocol.HTTP);
	}

	@Test
	public void shouldRemoveHttpsRedirection() throws Exception {
		givenAsiInDevelopmentModeWithHttpsRedirection();
		whenRemoveRedirection();
		thenHttpsRedirectionRemoved();
	}
	
	private void givenAsiInDevelopmentModeWithHttpsRedirection() {
		givenAsiInDevelopmentModeWithRedirection(false, true);
	}

	private void thenHttpsRedirectionRemoved() throws Exception {
		verify(air).removePortMapping(redirectionId);
		verify(httpRedirectionService).unregisterHttpService(contextId, asiId,
				serviceName, port, HttpProtocol.HTTPS);
	}

	@Test
	public void shouldRemoveHttpAndHttpsRemoved() throws Exception {
		givenAsiInDevelopmentModeWithHttpAndHttpsRedirection();
		whenRemoveRedirection();
		thenHttpRedirectionRemoved();
		thenHttpsRedirectionRemoved();
	}
	
	private void givenAsiInDevelopmentModeWithHttpAndHttpsRedirection() {
		givenAsiInDevelopmentModeWithRedirection(true, true);
	}

	@Test
	public void shouldRemoveTCPRedirection() throws Exception {
		givenAsiInDevelopmentModeWithTCPRedirection();
		whenRemoveRedirection();
		thenTCPRedirectionRemoved();
	}

	private void givenAsiInDevelopmentModeWithTCPRedirection() {
		givenAsiInDevelopmentModeWithRedirection(false, false);
	}

	private void thenTCPRedirectionRemoved() throws Exception {
		verify(air).removePortMapping(redirectionId);
		verify(dnatRedirectionService).removePortRedirection(asiId, port,
				Protocol.TCP);
	}
}
