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

import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowStartException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.AtomicServiceWorkflowAction;

/**
 * Start workflow. There can be many workflow type Workflows but only one portal
 * and development workflow.
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class StartWorkflowAction extends AtomicServiceWorkflowAction<String> {

	private static final Logger logger = LoggerFactory
			.getLogger(StartWorkflowAction.class);

	private WorkflowStartRequest workflow;
	private Integer defaultPriority;

	private String contextId;

	/**
	 * @param air Air client.
	 * @param atmosphere Atmosphere client
	 * @param defaultPriority Default workflow priority.
	 * @param username Workflow owner username.
	 * @param workflow Workflow start request. It contains information about
	 *            required Atomic Services.
	 */
	public StartWorkflowAction(ActionFactory actionFactory,
			Integer defaultPriority, String username,
			WorkflowStartRequest workflow) {
		super(actionFactory, username);
		this.workflow = workflow;
		this.defaultPriority = defaultPriority;
	}

	/**
	 * @return Workflow context id.
	 * @throws WorkflowStartException Thrown when workflow cannot be started.
	 *             E.g. user tries to start second development or portal
	 *             workflow.
	 */
	@Override
	public String execute() throws CloudFacadeException {
		logger.debug("starting workflow {} for {} user", workflow,
				getUsername());

		Integer priority = workflow.getPriority();
		if (priority == null) {
			priority = defaultPriority;
		}

		// issue #1311
		if (workflow.getType() == null) {
			workflow.setType(WorkflowType.workflow);
		}

		WorkflowType type = workflow.getType();
		if (type == WorkflowType.portal || type == WorkflowType.development) {
			try {
				List<WorkflowBaseInfo> workflows = getWorkflows();
				for (WorkflowBaseInfo wInfo : workflows) {
					if (wInfo.getType() == type) {
						throw new WorkflowStartException(String.format(
								"Cannot start two %s workflows", type));
					}
				}
			} catch (WebApplicationException e) {
				// 400 is thrown if user is not know by AIR. Most probably user
				// is starting workflow for the first time.
				if (e.getResponse().getStatus() != 400) {
					if (e instanceof WorkflowStartException) {
						throw e;
					}
					throw new WorkflowStartException(
							"Unable to register new workflow in AIR");
				}
			}
		}

		// FIXME error handling
		contextId = getAir().startWorkflow(workflow.getName(), getUsername(),
				workflow.getDescription(), priority, workflow.getType());
		List<String> ids = workflow.getAsConfigIds();

		if (ids != null && ids.size() > 0) {
			try {
				Action<String> startASAction = getActionFactory()
						.createStartAtomicServiceAction(getUsername(), ids,
								ids, contextId, priority, workflow.getKeyId());
				startASAction.execute();
			} catch (CloudFacadeException e) {
				rollback();
				throw new WorkflowStartException();
			}
		}

		return contextId;
	}

	private List<WorkflowBaseInfo> getWorkflows() {
		GetUserWorkflowsAction action = new GetUserWorkflowsAction(
				getActionFactory(), getUsername());
		return action.execute();
	}

	@Override
	public void rollback() {
		try {
			stopWorkflow();
		} catch (Exception e) {

		}
	}

	private void stopWorkflow() {
		new StopWorkflowAction(getActionFactory(), contextId, getUsername())
				.execute();
	}
}
