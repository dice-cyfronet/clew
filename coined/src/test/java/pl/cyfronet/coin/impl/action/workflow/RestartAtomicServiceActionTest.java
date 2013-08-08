package pl.cyfronet.coin.impl.action.workflow;

import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.air.client.Vms;

public class RestartAtomicServiceActionTest extends WorkflowActionTest {

	private String asiId = "asiIdOrInitConfId";

	@Test
	public void shouldRestartAtomicService() throws Exception {
		givenWorkflowWithAsiInDevelopmentMode();
		whenRestartAsi();
		thenAsiRestarted();
	}

	private void givenWorkflowWithAsiInDevelopmentMode() {
		givenWorkflowStarted(WorkflowType.development);
		Vms vm = new Vms();
		vm.setVms_id(asiId);
		workflowDetails.setVms(Arrays.asList(vm));
	}

	private void whenRestartAsi() {
		Action<Class<Void>> action = actionFactory.createRestartAsiAction(
				username, contextId, asiId);
		action.execute();
	}

	private void thenAsiRestarted() throws Exception {
		verify(atmosphere).restartAppliance(asiId);
	}

	@Test
	public void shouldThrowExceptionWhenWorkflowInProduction() throws Exception {
		givenWorkflowInProductionMode();
		try {
			whenRestartAsi();
		} catch (WorkflowNotInDevelopmentModeException e) {
			// OK should be thrown
		}
	}

	private void givenWorkflowInProductionMode() {
		givenWorkflowStarted(WorkflowType.workflow);
	}
}
