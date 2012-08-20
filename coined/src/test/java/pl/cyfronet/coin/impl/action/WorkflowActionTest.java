package pl.cyfronet.coin.impl.action;

import static org.mockito.Mockito.when;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

public class WorkflowActionTest extends ActionTest {

	protected final String contextId = "contextId";
	protected final String username = "user";
	
	protected void mockGetWorkflow() {
		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username(username);
		when(air.getWorkflow(contextId)).thenReturn(wd);		
	}

	protected void mockGetNonExistingWorkflow(AirClient air, String contextId) {
		when(air.getWorkflow(contextId)).thenThrow(getAirException(404));
	}
}
