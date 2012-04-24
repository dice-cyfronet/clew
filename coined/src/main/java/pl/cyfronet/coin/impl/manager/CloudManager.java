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

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.Workflow;
import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.EndpointNotFoundException;
import pl.cyfronet.coin.api.exception.InitialConfigurationAlreadyExistException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowStartException;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public interface CloudManager {

	/**
	 * Get list of all registered atomic services.
	 * @return List of all registered atomic services.
	 * @throws CloudFacadeException
	 */
	List<AtomicService> getAtomicServices();

	/**
	 * Get atomic service.
	 * @param atomicServiceId Atomic service instance. Right now it is equals
	 *            into atomic service name.
	 * @return Atomic service
	 * @throws AtomicServiceNotFoundException Thrown if atomic service with
	 *             given id is not registered in atmosphere.
	 */
	AtomicService getAtomicService(String atomicServiceId)
			throws AtomicServiceNotFoundException;

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
	 * Create Atomic Service from running Atomic Service Instance.
	 * @param atomicServiceInstanceId Atomic Service Instance id.
	 * @param atomicService Information about new Atomic Service.
	 * @param username User name.
	 * @return New created atomic service id.
	 * @throws AtomicServiceInstanceNotFoundException
	 * @throws CloudFacadeException
	 * @throws WorkflowNotFoundException Thrown when workflow is not found or
	 *             workflow with defined context id belongs to other user.
	 */
	String createAtomicService(String atomicServiceInstanceId,
			AtomicService atomicService, String username)
			throws AtomicServiceInstanceNotFoundException,
			CloudFacadeException, WorkflowNotFoundException;

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
			throws WorkflowNotFoundException;

	/**
	 * Get user workflows.
	 * @param username User name.
	 * @return List of user workflows.
	 * @throws WorkflowNotFoundException Thrown when workflow is not found or
	 *             workflow with defined context id belongs to other user.
	 */
	List<WorkflowBaseInfo> getWorkflows(String username);

	/**
	 * Get Atomic Service initial configurations.
	 * @param atomicServiceId Atomic Service id.
	 * @return List of Atomic Service configurations.
	 */
	List<InitialConfiguration> getInitialConfigurations(String atomicServiceId)
			throws AtomicServiceNotFoundException;

	/**
	 * @param workflowId
	 * @param username
	 * @return
	 */
	Workflow getWorkflow(String workflowId, String username)
			throws WorkflowNotFoundException;

	/**
	 * @param atomicServiceId
	 * @param initialConfiguration
	 * @return
	 */
	String addInitialConfiguration(String atomicServiceId,
			InitialConfiguration initialConfiguration)
			throws AtomicServiceInstanceNotFoundException,
			InitialConfigurationAlreadyExistException, CloudFacadeException;

	String getEndpointPayload(String atomicServiceId, int servicePort,
			String invocationPath)
			throws AtomicServiceInstanceNotFoundException,
			EndpointNotFoundException;

	List<Endpoint> getEndpoints();
}
