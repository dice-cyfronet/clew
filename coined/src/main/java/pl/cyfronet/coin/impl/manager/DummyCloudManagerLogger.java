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

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.Workflow;
import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.beans.WorkflowStatus;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class DummyCloudManagerLogger implements CloudManager {

	private static final Logger logger = LoggerFactory
			.getLogger(DummyCloudManagerLogger.class);

	@Override
	public List<AtomicService> getAtomicServices() throws CloudFacadeException {
		logger.info("Get atomic services - returning two dummy atomic services");
		return Arrays
				.asList(new AtomicService("as1"), new AtomicService("as2"));
	}

	@Override
	public String startAtomicService(String atomicServiceId, String name,
			String contextId, String username)
			throws AtomicServiceNotFoundException, CloudFacadeException {
		logger.info(
				"Start atomic service [{}, name {}] in {} context - generated id returned",
				new Object[] { atomicServiceId, name, contextId });

		if ("404".equals(atomicServiceId)) {
			throw new AtomicServiceNotFoundException();
		}

		return System.currentTimeMillis() + "";
	}

	@Override
	public String createAtomicService(String atomicServiceInstanceId,
			AtomicService atomicService, String username)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException {
		logger.info("Create atomic service from {}", atomicServiceInstanceId);
		if (atomicService != null) {
			logger.info("Atomic service details: name -> {}",
					atomicService.getName());
			if ("404".equals(atomicService.getName())) {
				throw new AtomicServiceInstanceNotFoundException();
			}
		} else {
			logger.info("Atomic service metadata empty");
		}
		return "id";
	}

	@Override
	public String startWorkflow(WorkflowStartRequest workflow, String username) {
		logger.debug("starting workflow {} for {} user", workflow, username);
		return "workflowId";
	}

	@Override
	public void stopWorkflow(String contextId, String username) {
		logger.debug("stopping workflow {}", contextId);
	}

	@Override
	public WorkflowStatus getWorkflowStatus(String contextId, String username) {
		logger.debug("Get workflow status {}", contextId);
		return null;
	}

	@Override
	public List<InitialConfiguration> getInitialConfigurations(
			String atomicServiceId) {
		return null;
	}

	@Override
	public List<WorkflowBaseInfo> getWorkflows(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see pl.cyfronet.coin.impl.manager.CloudManager#getWorkflow(java.lang.String, java.lang.String)
	 */
	@Override
	public Workflow getWorkflow(String workflowId, String username)
			throws WorkflowNotFoundException {
		return null;
	}

	/* (non-Javadoc)
	 * @see pl.cyfronet.coin.impl.manager.CloudManager#addInitialConfiguration(java.lang.String, pl.cyfronet.coin.api.beans.InitialConfiguration)
	 */
	@Override
	public String addInitialConfiguration(String atomicServiceId,
			InitialConfiguration initialConfiguration) {
		// TODO Auto-generated method stub
		return null;
	}

}
