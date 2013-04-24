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
package pl.cyfronet.coin.impl;

import static pl.cyfronet.coin.impl.utils.Validator.validateId;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.RedirectionType;
import pl.cyfronet.coin.api.WorkflowManagement;
import pl.cyfronet.coin.api.beans.AddAsWithKeyToWorkflow;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.Redirection;
import pl.cyfronet.coin.api.beans.UserWorkflows;
import pl.cyfronet.coin.api.beans.Workflow;
import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.EndpointNotFoundException;
import pl.cyfronet.coin.api.exception.RedirectionNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.api.exception.WorkflowStartException;
import pl.cyfronet.coin.auth.annotation.Role;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class WorkflowManagementImpl extends UsernameAwareService implements
		WorkflowManagement {

	// throw new WebApplicationException(403);

	private ActionFactory actionFactory;

	/**
	 * Logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(WorkflowManagementImpl.class);

	@Override
	public String startWorkflow(WorkflowStartRequest workflow)
			throws WorkflowStartException {
		Action<String> action = actionFactory.createStartWorkflowAction(
				workflow, getUsername());
		return action.execute();
	}

	@Override
	public void stopWorkflow(String workflowId) {
		logger.debug("Stopping workflow with {} workflowId", workflowId);
		validateId(workflowId);
		Action<Class<Void>> action = actionFactory.createStopWorkflowAction(
				workflowId, getUsername());
		try {
			action.execute();
		} catch (WorkflowNotFoundException e) {
			throw new WebApplicationException(404);
		}
	}

	@Override
	@Deprecated
	public void addAtomicServiceToWorkflow(String contextId, String asId,
			String name, String keyId) throws WorkflowNotFoundException,
			CloudFacadeException {
		AddAsWithKeyToWorkflow request = new AddAsWithKeyToWorkflow();
		request.setAsConfigId(asId);
		request.setName(name);
		request.setKeyId(keyId);
		addAtomicServiceToWorkflow(contextId, request);
	}

	@Override
	public void addAtomicServiceToWorkflow(String contextId,
			AddAsWithKeyToWorkflow request) throws WorkflowNotFoundException,
			CloudFacadeException {
		logger.debug("Adding atomic service {} to worklow {} [{}]",
				new Object[] { request.getAsConfigId(), contextId, request });
		validateId(contextId, request.getAsConfigId());
		if (request.getKeyId() != null) {
			validateId(request.getKeyId());
		}
		Action<String> action = actionFactory.createStartAtomicServiceAction(
				getUsername(), contextId, request);
		action.execute();
	}

	@Override
	public void removeAtomicServiceFromWorkflow(String workflowId,
			String asConfigId) {
		String username = getUsername();
		logger.debug(
				"Remove atomic service [{}] from workflow [{}] for user {}",
				new Object[] { asConfigId, workflowId, username });
		validateId(workflowId);
		Action<Class<Void>> action = actionFactory
				.createRemoveAtomicServiceFromWorkflowAction(getUsername(),
						workflowId, asConfigId);
		action.execute();
	}

	@Override
	public void removeAtomicServiceInstanceFromWorkflow(String workflowId,
			String asInstanceId) throws WorkflowNotFoundException,
			WorkflowNotInDevelopmentModeException, CloudFacadeException {
		String username = getUsername();
		logger.debug(
				"Remove atomic service instance [{}] from workflow [{}] for user {}",
				new Object[] { asInstanceId, workflowId, username });
		validateId(workflowId);
		Action<Class<Void>> action = actionFactory
				.createRemoveASIFromWorkflowAction(username, workflowId,
						asInstanceId);
		action.execute();
	}

	/*
	 * (non-Javadoc)
	 * @see pl.cyfronet.coin.api.WorkflowManagement#getWorkflows()
	 */
	@Override
	public UserWorkflows getWorkflows() {
		String username = getUsername();
		UserWorkflows wrapper = new UserWorkflows();
		wrapper.setUsername(username);

		Action<List<WorkflowBaseInfo>> action = actionFactory
				.createGetUserWorkflowsAction(username);
		try {
			List<WorkflowBaseInfo> workflows = action.execute();
			wrapper.setWorkflows(workflows);
		} catch (Exception e) {
			wrapper.setWorkflows(new ArrayList<WorkflowBaseInfo>());
		}
		return wrapper;
	}

	@Override
	public Workflow getWorkflow(String workflowId) {
		Action<Workflow> action = actionFactory.createGetUserWorkflowAction(
				workflowId, getUsername());
		validateId(workflowId);
		try {
			return action.execute();
		} catch (WorkflowNotFoundException e) {
			throw new WebApplicationException(404);
		}
	}

	@Override
	public AtomicServiceInstance getWorkflowAtomicServiceInstance(
			String workflowId, String asiId) throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException {
		validateId(workflowId, asiId);
		Action<AtomicServiceInstance> action = actionFactory
				.createGetWorkflowAtomicServiceInstanceAction(getUsername(),
						workflowId, asiId);
		return action.execute();
	}

	@Override
	public List<Redirection> getRedirections(String contextId, String asiId)
			throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException {
		validateId(contextId);
		Action<List<Redirection>> action = actionFactory
				.createGetAsiRedirectionsAction(contextId, getUsername(), asiId);
		return action.execute();
	}

	@Role(values = "developer")
	@Override
	public String addRedirection(String contextId, String asiId, String name,
			int port, RedirectionType type) throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException,
			WorkflowNotInDevelopmentModeException {
		validateId(contextId);
		Action<String> action = actionFactory.createAddAsiRedirectionAction(
				getUsername(), contextId, asiId, name, port, type);
		return action.execute();
	}

	@Role(values = "developer")
	@Override
	public void deleteRedirection(String contextId, String asiId,
			String redirectionId) throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException,
			WorkflowNotInDevelopmentModeException, RedirectionNotFoundException {
		validateId(contextId, redirectionId);
		Action<Class<Void>> action = actionFactory
				.createRemoveAsiRedirectionAction(getUsername(), contextId,
						asiId, redirectionId);
		action.execute();
	}

	@Override
	public List<Endpoint> getEndpoints(String contextId, String asiId)
			throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException {
		validateId(contextId);
		Action<List<Endpoint>> action = actionFactory
				.createListAsiEndpointsAction(getUsername(), contextId, asiId);
		return action.execute();
	}

	@Role(values = "developer")
	@Override
	public String addEndpoint(String contextId, String asiId, Endpoint endpoint)
			throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException,
			WorkflowNotInDevelopmentModeException {
		validateId(contextId);
		Action<String> action = actionFactory.createAddAsiEndpointAction(
				getUsername(), contextId, asiId, endpoint);
		return action.execute();
	}

	@Role(values = "developer")
	@Override
	public void deleteEndpoint(String contextId, String asiId, String endpointId)
			throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException,
			WorkflowNotInDevelopmentModeException, EndpointNotFoundException {
		validateId(contextId, endpointId);
		Action<Class<Void>> action = actionFactory
				.createRemoveAsiEndpointAction(getUsername(), contextId, asiId,
						endpointId);
		action.execute();
	}

	public void setActionFactory(ActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}
}
