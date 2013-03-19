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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.as.DeleteAtomicServiceFromAirAction;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

/**
 * Stop workflow action.
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class StopWorkflowAction extends WorkflowAction<Class<Void>> {

	private static final Logger logger = LoggerFactory
			.getLogger(StopWorkflowAction.class);

	private String contextId;

	/**
	 * @param air Air client.
	 * @param atmosphere Atmosphere client.
	 * @param contextId Workflow id.
	 * @param username User which wants to stop the workflow.
	 */
	public StopWorkflowAction(ActionFactory actionFactory, String contextId,
			String username) {
		super(actionFactory, username);
		this.contextId = contextId;
	}

	/**
	 * @throws WorkflowNotFoundException Thrown when workflow is not found or
	 *             workflow with defined context id belongs to other user.
	 */
	@Override
	public Class<Void> execute() throws CloudFacadeException {
		logger.debug("stopping workflow {}", contextId);
		WorkflowDetail wd = getUserWorkflow(contextId, getUsername());
		// ManagerResponse response =
		getAtmosphere().removeRequiredAppliances(contextId);

		// FIXME waiting for atmo improvement
		// parseResponseAndThrowExceptionsWhenNeeded(response);
		getAir().stopWorkflow(contextId);

		List<Vms> vms = wd.getVms();
		if (wd.getWorkflow_type() == WorkflowType.development && vms != null) {
			List<String> asesToRemove = new ArrayList<>();
			for (Vms vm : vms) {
				asesToRemove.add(vm.getAppliance_type());
			}
			logger.debug("Removing following ASes {}", asesToRemove);
			DeleteAtomicServiceFromAirAction deleteASAction = new DeleteAtomicServiceFromAirAction(
					getActionFactory(), asesToRemove);
			deleteASAction.execute();
		}

		return Void.TYPE;
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
	}

}
