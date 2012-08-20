package pl.cyfronet.coin.impl.action;

import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

/**
 * Get user workflows.
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetUserWorkflowsAction extends AirAction<List<WorkflowBaseInfo>> {

	private String username;

	GetUserWorkflowsAction(AirClient air, String username) {
		super(air);
		this.username = username;
	}

	@Override
	public List<WorkflowBaseInfo> execute() throws CloudFacadeException {
		List<WorkflowDetail> workflowDetails = getAir().getUserWorkflows(
				username);
		List<WorkflowBaseInfo> workflows = new ArrayList<WorkflowBaseInfo>();
		for (WorkflowDetail workflowDetail : workflowDetails) {
			if (workflowDetail.getState() == Status.running) {
				WorkflowBaseInfo info = new WorkflowBaseInfo();
				info.setId(workflowDetail.getId());
				info.setName(workflowDetail.getName());
				info.setType(workflowDetail.getWorkflow_type());
				workflows.add(info);
			}
		}

		return workflows;
	}

	@Override
	public void rollback() {
		// read only action, no rollback needed
	}

}
