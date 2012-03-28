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

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.WorkflowManagement;
import pl.cyfronet.coin.api.beans.AtomicServiceStatus;
import pl.cyfronet.coin.api.beans.RedirectionInfo;
import pl.cyfronet.coin.api.beans.UserWorkflows;
import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.beans.WorkflowStatus;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowStartException;
import pl.cyfronet.coin.impl.manager.CloudManager;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class WorkflowManagementImpl extends UsernameAwareService implements
		WorkflowManagement {

	// throw new WebApplicationException(403);

	private CloudManager manager;

	/**
	 * Logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(WorkflowManagementImpl.class);

	@Override
	public String startWorkflow(WorkflowStartRequest workflow)
			throws WorkflowStartException {
		return manager.startWorkflow(workflow, getUsername());
	}

	@Override
	public void stopWorkflow(String workflowId) {
		try {
			manager.stopWorkflow(workflowId, getUsername());
		} catch (WorkflowNotFoundException e) {
			throw new WebApplicationException(404);
		}
	}

	@Override
	public WorkflowStatus getStatus(String contextId) {
		try {
			return manager.getWorkflowStatus(contextId, getUsername());
		} catch (WorkflowNotFoundException e) {
			throw new WebApplicationException(404);
		}
	}

	@Override
	public void addAtomicServiceToWorkflow(String contextId, String asId,
			String name) {
		try {
			manager.startAtomicService(asId, name, contextId, getUsername());
		} catch (WorkflowNotFoundException e) {
			throw new WebApplicationException(404);
		}
	}

	@Override
	public void removeAtomicServiceFromWorkflow(String workflowId, String asId) {
		logger.debug("Remove atomic service [{}] from workflow [{}]", asId,
				workflowId);
		throw new WebApplicationException(501);
	}

	@Override
	public AtomicServiceStatus getStatus(String workflowId, String asId) {
		logger.debug("Get atomic service [{}] for workflow [{}]", asId,
				workflowId);

		WorkflowStatus workflowStatus;
		try {
			workflowStatus = manager.getWorkflowStatus(workflowId,
					getUsername());
			List<AtomicServiceStatus> asStatuses = workflowStatus.getAses();
			if (asStatuses != null) {
				for (AtomicServiceStatus atomicServiceStatus : asStatuses) {
					if (atomicServiceStatus.getId().equals(asId)) {
						return atomicServiceStatus;
					}
				}
			}
			throw new WebApplicationException(404);
		} catch (WorkflowNotFoundException e) {
			throw new WebApplicationException(404);
		}
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
		try {
			List<WorkflowBaseInfo> workflows = manager.getWorkflows(username);
			wrapper.setWorkflows(workflows);
		} catch (Exception e) {
			wrapper.setWorkflows(new ArrayList<WorkflowBaseInfo>());
		}
		return wrapper;
	}

	@Override
	public String addRedirection(String contextId, String asiId,
			RedirectionInfo redirectionInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param manager the manager to set
	 */
	public void setManager(CloudManager manager) {
		this.manager = manager;
	}
}
