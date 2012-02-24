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

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.WorkflowManagement;
import pl.cyfronet.coin.api.beans.AtomicServiceInstanceStatus;
import pl.cyfronet.coin.api.beans.AtomicServiceStatus;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.beans.WorkflowStatus;
import pl.cyfronet.coin.impl.manager.CloudManager;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class WorkflowManagementImpl implements WorkflowManagement {

	//throw new WebApplicationException(403);
	
	private CloudManager manager;

	/**
	 * Logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(WorkflowManagementImpl.class);
	
	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.api.WorkflowManagement#startWorkflow(pl.cyfronet.coin
	 * .api.beans.Workflow)
	 */
	@Override
	public String startWorkflow(WorkflowStartRequest workflow) {		
		return manager.startWorkflow(workflow, "username");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.api.WorkflowManagement#deleteWorkflow(java.lang.String)
	 */
	@Override
	public void stopWorkflow(String workflowId) {
		manager.stopWorkflow(workflowId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see pl.cyfronet.coin.api.WorkflowManagement#getStatus(java.lang.String)
	 */
	@Override
	public WorkflowStatus getStatus(String contextId) {
		return manager.getWorkflowStatus(contextId);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.api.WorkflowManagement#addAtomicServiceToWorkflow(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public void addAtomicServiceToWorkflow(String contextId, String asId) {		
		manager.startAtomicService(asId, null, contextId);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.api.WorkflowManagement#removeAtomicServiceToWorkflow
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void removeAtomicServiceFromWorkflow(String workflowId, String asId) {
		logger.debug("Remove atomic service [{}] from workflow [{}]", asId, workflowId);
	}

	/*
	 * (non-Javadoc)
	 * @see pl.cyfronet.coin.api.WorkflowManagement#getStatus(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public AtomicServiceStatus getStatus(String workflowId, String asId) {
		logger.debug("Get atomic service [{}] for workflow [{}]", asId, workflowId);
		
		AtomicServiceStatus as1s = new AtomicServiceStatus();
		as1s.setId("configId");
		as1s.setStatus(Status.running);
		as1s.setMessage("Up and running");
		
		AtomicServiceInstanceStatus asi1s = new AtomicServiceInstanceStatus();
		asi1s.setId("1");
		asi1s.setStatus(Status.running);
		asi1s.setMessage("my message");
		
		as1s.setInstances(Arrays.asList(asi1s));
		
		return as1s;
	}
	
	/**
	 * @param manager the manager to set
	 */
	public void setManager(CloudManager manager) {
		this.manager = manager;
	}

	/* (non-Javadoc)
	 * @see pl.cyfronet.coin.api.WorkflowManagement#getInitialConfigurations(java.lang.String)
	 */
	@Override
	public List<InitialConfiguration> getInitialConfigurations(
			String atomicServiceId) {
		// TODO Auto-generated method stub
		return null;
	}
}
