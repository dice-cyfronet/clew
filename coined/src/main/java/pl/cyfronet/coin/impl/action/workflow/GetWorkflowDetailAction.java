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

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.ReadOnlyAirAction;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

/**
 * Get workflow identified by context id. It also checks if the workflow belongs
 * to current user. If not than exception is thrown.
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetWorkflowDetailAction extends ReadOnlyAirAction<WorkflowDetail> {

	private static final Logger logger = LoggerFactory
			.getLogger(GetWorkflowDetailAction.class);

	private String contextId;
	private String username;

	/**
	 * @param air Air client.
	 * @param contextId Workflow context id.
	 * @param username Workflow owner username.
	 */
	public GetWorkflowDetailAction(ActionFactory actionFactory,
			String contextId, String username) {
		super(actionFactory);
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
				logger.warn("Workflow {} is not found for user {}", contextId,
						username);
				// workflow is not found or it depends to other user.
				throw new WorkflowNotFoundException();
			}
		} catch (WebApplicationException e) {
			if (e.getResponse().getStatus() == 404) {
				logger.warn("Workflow {} not found", contextId);
				throw new WorkflowNotFoundException();				
			} else {
				logger.error("AIR throws exception", e);
				throw new CloudFacadeException(e.getMessage());
			}
		}
	}
}
