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

package pl.cyfronet.coin.impl.action;

import java.util.Arrays;

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotInProductionModeException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.dyrealla.api.DyReAllaManagerService;
import pl.cyfronet.dyrealla.api.allocation.impl.RemoveRequiredAppliancesRequestImpl;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class RemoveAtomicServiceFromWorkflowAction extends
		WorkflowAction<Class<Void>> {

	private String contextId;
	private String asConfigId;

	RemoveAtomicServiceFromWorkflowAction(AirClient air,
			DyReAllaManagerService atmosphere, String username,
			String contextId, String asConfigId) {
		super(air, atmosphere, username);
		this.contextId = contextId;
		this.asConfigId = asConfigId;
	}

	/*
	 * (non-Javadoc)
	 * @see pl.cyfronet.coin.impl.action.Action#execute()
	 */
	@Override
	public Class<Void> execute() throws CloudFacadeException {
		if (workflowInProductionModeHasAS()) {
			RemoveRequiredAppliancesRequestImpl request = new RemoveRequiredAppliancesRequestImpl();
			request.setApplicationId(contextId);
			request.setInitConfigIds(Arrays.asList(asConfigId));
			getAtmosphere().removeRequiredAppliances(request);
		} else {
			throw new AtomicServiceNotFoundException();
		}

		return Void.TYPE;
	}

	private boolean workflowInProductionModeHasAS()
			throws WorkflowNotInProductionModeException {
		WorkflowDetail workflowDetails = getUserWorkflow(contextId,
				getUsername());
		if (workflowDetails.getWorkflow_type() == WorkflowType.development) {
			throw new WorkflowNotInProductionModeException();
		} else if (workflowDetails.getVms() != null) {
			for (Vms vm : workflowDetails.getVms()) {
				if (asConfigId.equals(vm.getConf_id())) {
					return true;
				}
			}
		}

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
