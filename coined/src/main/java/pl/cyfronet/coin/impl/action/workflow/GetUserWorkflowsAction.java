/*
 * Copyright 2012 ACC CYFRONET AGH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package pl.cyfronet.coin.impl.action.workflow;

import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.AirAction;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

/**
 * Get user workflows.
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetUserWorkflowsAction extends AirAction<List<WorkflowBaseInfo>> {

	private String username;

	public GetUserWorkflowsAction(ActionFactory actionFactory, String username) {
		super(actionFactory);
		this.username = username;
	}

	@Override
	public List<WorkflowBaseInfo> execute() throws CloudFacadeException {
		List<WorkflowDetail> workflowDetails = getAir().getUserWorkflows(
				username);
		List<WorkflowBaseInfo> workflows = new ArrayList<WorkflowBaseInfo>();
		for (WorkflowDetail workflowDetail : workflowDetails) {
			WorkflowBaseInfo info = new WorkflowBaseInfo();
			info.setId(workflowDetail.getId());
			info.setName(workflowDetail.getName());
			info.setType(workflowDetail.getWorkflow_type());
			workflows.add(info);
		}

		return workflows;
	}

	@Override
	public void rollback() {
		// read only action, no rollback needed
	}

}
