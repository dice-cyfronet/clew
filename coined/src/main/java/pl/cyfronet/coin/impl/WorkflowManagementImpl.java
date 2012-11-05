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

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.RedirectionType;
import pl.cyfronet.coin.api.WorkflowManagement;
import pl.cyfronet.coin.api.beans.Redirection;
import pl.cyfronet.coin.api.beans.UserWorkflows;
import pl.cyfronet.coin.api.beans.Workflow;
import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.RedirectionNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.api.exception.WorkflowStartException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.GetAsiRedirectionsAction;
import pl.cyfronet.coin.impl.action.GetUserWorkflowAction;
import pl.cyfronet.coin.impl.action.GetUserWorkflowsAction;
import pl.cyfronet.coin.impl.action.RemoveASIFromWorkflowAction;
import pl.cyfronet.coin.impl.action.RemoveAtomicServiceFromWorkflowAction;
import pl.cyfronet.coin.impl.action.StartAtomicServiceAction;
import pl.cyfronet.coin.impl.action.StartWorkflowAction;
import pl.cyfronet.coin.impl.action.StopWorkflowAction;

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
		StartWorkflowAction action = actionFactory.createStartWorkflowAction(
				workflow, getUsername());
		return action.execute();
	}

	@Override
	public void stopWorkflow(String workflowId) {
		StopWorkflowAction action = actionFactory.createStopWorkflowAction(
				workflowId, getUsername());
		try {
			action.execute();
		} catch (WorkflowNotFoundException e) {
			throw new WebApplicationException(404);
		}
	}

	@Override
	public void addAtomicServiceToWorkflow(String contextId, String asId,
			String name) {
		StartAtomicServiceAction action = actionFactory
				.createStartAtomicServiceAction(asId, name, contextId,
						getUsername());
		try {
			action.execute();
		} catch (WorkflowNotFoundException e) {
			throw new WebApplicationException(404);
		}
	}

	@Override
	public void removeAtomicServiceFromWorkflow(String workflowId,
			String asConfigId) {
		String username = getUsername();
		logger.debug("Remove atomic service [{}] from workflow [{}] for user {}",
				new Object[] {asConfigId, workflowId, username}); 
		RemoveAtomicServiceFromWorkflowAction action = actionFactory
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

		RemoveASIFromWorkflowAction action = actionFactory
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

		GetUserWorkflowsAction action = actionFactory
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
		GetUserWorkflowAction action = actionFactory
				.createGetUserWorkflowAction(workflowId, getUsername());
		try {
			return action.execute();
		} catch (WorkflowNotFoundException e) {
			throw new WebApplicationException(404);
		}
	}

	@Override
	public List<Redirection> getRedirections(String contextId, String asiId)
			throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException {
		GetAsiRedirectionsAction action = actionFactory
				.createGetAsiRedirectionsAction(contextId, asiId);
		return action.execute();
	}

	@Override
	public Redirection addRedirection(String contextId, String asiId,
			String name, int port, RedirectionType type)
			throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException,
			WorkflowNotInDevelopmentModeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Redirection getRedirection(String contextId, String asiId,
			String redirectionName) throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException,
			RedirectionNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Redirection updateRedirection(String contextId, String asiId,
			String name, int port, RedirectionType type)
			throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException,
			WorkflowNotInDevelopmentModeException, RedirectionNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRedirection(@PathParam("contextId") String contextId,
			@PathParam("asiId") String asiId, @FormParam("name") String name)
			throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException,
			WorkflowNotInDevelopmentModeException, RedirectionNotFoundException {
		// TODO Auto-generated method stub
	}

	public void setActionFactory(ActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}
}
