package pl.cyfronet.coin.impl.action;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

public class GetUserWorkflowsActionTest extends WorkflowActionTest {

	private List<WorkflowBaseInfo> infos;

	@Test
	public void shouldGetRunningUserWorkflows() throws Exception {
		given2WorkingAnd1StoppedUserWorkflows();
		whenGetUserWorkflows();
		thanGetOnlyRunningWorkflows();
	}

	private void given2WorkingAnd1StoppedUserWorkflows() {
		WorkflowDetail w1 = new WorkflowDetail();
		w1.setName("w1");
		w1.setId("id1");
		w1.setState(Status.running);
		w1.setWorkflow_type(WorkflowType.development);

		WorkflowDetail w2 = new WorkflowDetail();
		w2.setName("w2");
		w2.setId("id2");
		w2.setState(Status.running);
		w2.setWorkflow_type(WorkflowType.workflow);

		WorkflowDetail w3 = new WorkflowDetail();
		w3.setState(Status.stopped);

		when(air.getUserWorkflows(username)).thenReturn(Arrays.asList(w1, w2));
	}

	private void whenGetUserWorkflows() {
		GetUserWorkflowsAction action = actionFactory
				.createGetUserWorkflowsAction(username);
		infos = action.execute();
	}

	private void thanGetOnlyRunningWorkflows() {
		assertEquals(2, infos.size());
		assertEquals("id1", infos.get(0).getId());
		assertEquals("w1", infos.get(0).getName());
		assertEquals(WorkflowType.development, infos.get(0).getType());
		assertEquals("id2", infos.get(1).getId());
		assertEquals("w2", infos.get(1).getName());
		assertEquals(WorkflowType.workflow, infos.get(1).getType());
	}
}
