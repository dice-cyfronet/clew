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
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.beans.WorkflowStatus;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.manager.exception.ApplianceTypeNotFound;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public interface CloudManager {

	List<AtomicService> getAtomicServices() throws CloudFacadeException;

	String startAtomicService(String atomicServiceId, String name,
			String contextId) throws AtomicServiceNotFoundException,
			CloudFacadeException;

	void createAtomicService(String atomicServiceInstanceId,
			AtomicService atomicService)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException;

	/**
	 * @param workflow
	 * @param username
	 * @return
	 */
	String startWorkflow(WorkflowStartRequest workflow, String username);

	void stopWorkflow(String contextId);

	WorkflowStatus getWorkflowStatus(String contextId);

	List<WorkflowBaseInfo> getWorkflows(String username);
	
	/**
	 * @param atomicServiceId
	 * @return
	 */
	List<InitialConfiguration> getInitialConfigurations(String atomicServiceId)
			throws ApplianceTypeNotFound;
}
