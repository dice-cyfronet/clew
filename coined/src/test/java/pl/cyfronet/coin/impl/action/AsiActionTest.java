package pl.cyfronet.coin.impl.action;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

public class AsiActionTest {

	private String asiId = "asiId";
	private String atId = "atId";
	private AsiAction<String> action;
	private String receivedATId;

	@Test
	public void shouldGetATIdForASIStartedInDevelopment() throws Exception {
		givenWorkflowInDevelopmentWithASI();
		whenGetASIApplicanceTypeId();
		thenApplianceTypeIdReceived();
	}

	private void givenWorkflowInDevelopmentWithASI() {
		action = new AsiAction<String>(null, null, null, asiId) {

			@Override
			protected WorkflowDetail getUserWorkflow(String contextId,
					String username) {
				WorkflowDetail wd = new WorkflowDetail();
				wd.setWorkflow_type(WorkflowType.development);

				Vms vm = new Vms();
				vm.setVms_id(asiId);
				vm.setAppliance_type(atId);

				wd.setVms(Arrays.asList(vm));

				return wd;
			}

			@Override
			public String execute() throws CloudFacadeException {
				return null;
			}

			@Override
			public void rollback() {

			}
		};
	}

	private void whenGetASIApplicanceTypeId() {
		receivedATId = action.getAsiApplianceTypeId();
	}

	private void thenApplianceTypeIdReceived() {
		assertEquals(receivedATId, atId);
	}

	@Test
	public void shouldGetATIdForASIStartedInProduction() throws Exception {
		givenWorkflowInProductionWithASI();
		whenGetASIApplicanceTypeId();
		thenApplianceTypeIdReceived();

	}

	private void givenWorkflowInProductionWithASI() {
		action = new AsiAction<String>(null, null, null, asiId) {

			@Override
			protected WorkflowDetail getUserWorkflow(String contextId,
					String username) {
				WorkflowDetail wd = new WorkflowDetail();
				wd.setWorkflow_type(WorkflowType.portal);

				Vms vm = new Vms();
				vm.setConfiguration(asiId);
				vm.setAppliance_type(atId);

				wd.setVms(Arrays.asList(vm));

				return wd;
			}

			@Override
			public String execute() throws CloudFacadeException {
				return null;
			}

			@Override
			public void rollback() {

			}
		};
	}
}
