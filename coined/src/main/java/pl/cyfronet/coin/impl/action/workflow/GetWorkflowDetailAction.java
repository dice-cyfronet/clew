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

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.action.ReadOnlyAirAction;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

/**
 * Get workflow identified by context id. It also checks if the workflow
 * belongs to current user. If not than exception is thrown.
 * 
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetWorkflowDetailAction extends ReadOnlyAirAction<WorkflowDetail>{

	
	private String contextId;
	private String username;

	/**
	 * @param air Air client.
	 * @param contextId Workflow context id.
	 * @param username Workflow owner username.
	 */
	public GetWorkflowDetailAction(AirClient air, String contextId, String username) {
		super(air);
		this.contextId = contextId;
		this.username = username;
	}

	@Override
	public WorkflowDetail execute() throws CloudFacadeException {
		try {
			WorkflowDetail detail = getAir().getWorkflow(contextId);
			if (detail != null && detail.getVph_username().equals(username)) {
				return detail;
			} else {
				// workflow is not found or it depends to other user.
				throw new WorkflowNotFoundException();
			}
		} catch (ServerWebApplicationException e) {
			if (e.getResponse().getStatus() == 404) {
				throw new WorkflowNotFoundException();
			} else {
				throw new CloudFacadeException(e.getMessage());
			}
		}
	}
}
