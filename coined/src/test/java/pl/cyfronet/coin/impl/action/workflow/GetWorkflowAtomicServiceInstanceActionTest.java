package pl.cyfronet.coin.impl.action.workflow;

import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import java.util.Arrays;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

public class GetWorkflowAtomicServiceInstanceActionTest extends
		WorkflowActionTest {

	private WorkflowDetail airWorkflow;
	private String asiId = "asiId";
	private AtomicServiceInstance asInstance;

	@Test
	public void shouldGetWorkflowASI() throws Exception {
		givenWorkflowWithASI();
		whenGetWorkflowASI();
		thenInformationAboutASIFetched();
	}

	private void givenWorkflowWithASI() {
		givenWorkflowWithASI(asiId);
	}

	private void givenWorkflowWithASI(String asiId) {
		airWorkflow = new WorkflowDetail();
		airWorkflow.setVph_username(username);
		airWorkflow.setName("wName");
		airWorkflow.setWorkflow_type(WorkflowType.workflow);

		Vms vm = new Vms();
		vm.setAppliance_type("type1");
		vm.setAppliance_type_name("type1 name");
		vm.setName("vm1");
		vm.setState(Status.booting);
		vm.setVms_id(asiId);
		vm.setConfiguration("initConf1");
		vm.setSite_id("cyfronet-folsom");

		airWorkflow.setVms(Arrays.asList(vm));

		when(air.getWorkflow(contextId)).thenReturn(airWorkflow);
	}

	private void whenGetWorkflowASI() {
		Action<AtomicServiceInstance> action = actionFactory
				.createGetWorkflowAtomicServiceInstanceAction(username,
						contextId, asiId);
		asInstance = action.execute();
	}

	private void thenInformationAboutASIFetched() {
		equals(airWorkflow.getVms().get(0), asInstance);
	}

	@Test
	public void shouldThrow404WhenASINotFound() throws Exception {
		givenWorkflowWithoutASI();
		try {
			whenGetWorkflowASI();
			fail();
		} catch (AtomicServiceInstanceNotFoundException e) {
			// OK should be thrown
		}
	}

	private void givenWorkflowWithoutASI() {
		givenWorkflowWithASI("differentAsiId");
	}
}
