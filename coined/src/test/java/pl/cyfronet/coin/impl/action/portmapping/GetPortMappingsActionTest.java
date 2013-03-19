package pl.cyfronet.coin.impl.action.portmapping;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.workflow.WorkflowActionTest;
import pl.cyfronet.coin.impl.air.client.ATPortMapping;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.Vms;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetPortMappingsActionTest extends WorkflowActionTest {

	private String asiId = "asiId";
	private String atId = "atId";
	private ApplianceType at;
	private List<ATPortMapping> portMapping;

	@Test
	public void shouldGetAsiRedirections() throws Exception {
		givenAsiStarted();
		whenGetAsiRedirections();
		thenAsiRedirectionsReceived();
	}

	private void givenAsiStarted() {
		givenWorkflowStarted();

		Vms vm = new Vms();
		vm.setVms_id(asiId);
		vm.setAppliance_type(atId);
		workflowDetails.setVms(Arrays.asList(vm));

		at = new ApplianceType();
		at.setId(atId);

		ATPortMapping at1 = new ATPortMapping();
		at1.setHttp(true);
		at1.setId("at1Id");
		at1.setPort(80);
		at1.setService_name("at1");

		ATPortMapping at2 = new ATPortMapping();
		at2.setHttp(false);
		at2.setId("at1Id");
		at2.setPort(22);
		at2.setService_name("at2");

		at.setPort_mappings(Arrays.asList(at1, at2));

		when(air.getApplianceTypes()).thenReturn(Arrays.asList(at));
	}

	private void whenGetAsiRedirections() {
		Action<List<ATPortMapping>> action = new GetPortMappingsAction(
				actionFactory, username, contextId, asiId);
		portMapping = action.execute();
	}

	private void thenAsiRedirectionsReceived() {
		assertEquals(portMapping, at.getPort_mappings());
	}

	@Test
	public void shouldThrowAsiNotExistWhenAsiNotFound() throws Exception {
		givenWorkflowWithoudAsiStarted();
		try {
			whenGetAsiRedirections();
			fail();
		} catch (AtomicServiceInstanceNotFoundException e) {
			// OK should be thrown.
		}
	}

	private void givenWorkflowWithoudAsiStarted() {
		givenWorkflowStarted();
	}
}
