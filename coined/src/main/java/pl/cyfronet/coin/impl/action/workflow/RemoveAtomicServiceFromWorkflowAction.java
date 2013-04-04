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

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotInProductionModeException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.dyrealla.api.allocation.impl.RemoveRequiredAppliancesRequestImpl;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class RemoveAtomicServiceFromWorkflowAction extends
		WorkflowAction<Class<Void>> {

	private static final Logger logger = LoggerFactory
			.getLogger(RemoveAtomicServiceFromWorkflowAction.class);

	private String contextId;
	private String asConfigId;

	public RemoveAtomicServiceFromWorkflowAction(ActionFactory actionFactory,
			String username, String contextId, String asConfigId) {
		super(actionFactory, username);
		this.contextId = contextId;
		this.asConfigId = asConfigId;
	}

	/*
	 * (non-Javadoc)
	 * @see pl.cyfronet.coin.impl.action.Action#execute()
	 */
	@Override
	public Class<Void> execute() throws CloudFacadeException {
		logger.debug("Removing {} AS from {} context", asConfigId, contextId);
		if (workflowInProductionModeHasAS()) {
			RemoveRequiredAppliancesRequestImpl request = new RemoveRequiredAppliancesRequestImpl();
			request.setApplicationId(contextId);
			request.setInitConfigIds(Arrays.asList(asConfigId));
			try {
				getAtmosphere().removeRequiredAppliances(request);
			} catch (Exception e) {
				logger.error("Unexpected error was thrown from Atmosphere", e);
				throw new CloudFacadeException(
						"Exception was thrown by Atmosphere, plese contact administrator");
			}
		} else {
			throw new AtomicServiceNotFoundException();
		}

		return Void.TYPE;
	}

	private boolean workflowInProductionModeHasAS()
			throws WorkflowNotInProductionModeException {
		WorkflowDetail workflowDetails = getUserWorkflow(contextId,
				getUsername());
		logger.debug(
				"Checking if workflow is in production. Workflow type: {} with following VMS {}",
				workflowDetails.getWorkflow_type(), workflowDetails.getVms());
		if (workflowDetails.getWorkflow_type() == WorkflowType.development) {
			logger.warn(
					"Trying to remove {} AS from workflow {} which is not in production mode",
					asConfigId, contextId);
			throw new WorkflowNotInProductionModeException();
		} else if (workflowDetails.getVms() != null) {
			for (Vms vm : workflowDetails.getVms()) {
				if (asConfigId.equals(vm.getConfiguration_id())) {
					return true;
				}
			}
		}

		logger.warn(
				"AS {} does not belong to {} workflow. Following ASes belongs to the workflow {}. Removing AS from workflow failed.",
				new Object[] { asConfigId, contextId, workflowDetails.getVms() });
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see pl.cyfronet.coin.impl.action.Action#rollback()
	 */
	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}

}
