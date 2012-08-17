/*
 * Copyright 2011 ACC CYFRONET AGH
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

package pl.cyfronet.coin.impl.manager;

import java.util.List;

import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.Workflow;
import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowStartException;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public interface CloudManager {

	/**
	 * Start atomic service in defined context. If the request is send into
	 * development context than new Atomic Service Instance will be spawn.
	 * Otherwise new Atomic Service required for context is registered and
	 * Atmosphere will take care of the whole optimization process. The
	 * optimization process is specific for every Atomic Service (e.g. some of
	 * the Atomic Services can be shared/scaled other not).
	 * @param atomicServiceId Atomic Service id.
	 * @param name New instance name.
	 * @param contextId Context id.
	 * @param username User name.
	 * @return Atomic Service Instance id.
	 * @throws AtomicServiceNotFoundException Thrown when atomic service is not
	 *             found.
	 * @throws CloudFacadeException
	 * @throws WorkflowNotFoundException Thrown when workflow is not found or
	 *             workflow with defined context id belongs to other user.
	 */
	String startAtomicService(String atomicServiceId, String name,
			String contextId, String username)
			throws AtomicServiceNotFoundException, CloudFacadeException,
			WorkflowNotFoundException;

	/**
	 * Start workflow. There can be many workflow type Workflows but only one
	 * portal and development workflow.
	 * @param workflow Workflow start request. It contains information about
	 *            required Atomic Services.
	 * @param username Workflow owner username.
	 * @param username User name.
	 * @return Workflow context id.
	 * @throws WorkflowStartException Thrown when workflow cannot be started.
	 *             E.g. user tries to start second development or portal
	 *             workflow.
	 */
	String startWorkflow(WorkflowStartRequest workflow, String username)
			throws WorkflowStartException;

	/**
	 * Stop workflow.
	 * @param contextId Workflow context id.
	 * @param username User name.
	 * @throws WorkflowNotFoundException Thrown when workflow is not found or
	 *             workflow with defined context id belongs to other user.
	 */
	void stopWorkflow(String contextId, String username)
			throws WorkflowNotFoundException, CloudFacadeException;

	/**
	 * Get user workflows.
	 * @param username User name.
	 * @return List of user workflows.
	 * @throws WorkflowNotFoundException Thrown when workflow is not found or
	 *             workflow with defined context id belongs to other user.
	 */
	List<WorkflowBaseInfo> getWorkflows(String username);

	/**
	 * @param workflowId
	 * @param username
	 * @return
	 */
	Workflow getWorkflow(String workflowId, String username)
			throws WorkflowNotFoundException;

	List<Endpoint> getEndpoints();
}
