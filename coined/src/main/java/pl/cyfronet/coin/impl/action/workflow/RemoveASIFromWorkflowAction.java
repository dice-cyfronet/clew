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

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.as.GetASITypeAction;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.dyrealla.api.allocation.ManagerResponse;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class RemoveASIFromWorkflowAction extends WorkflowAction<Class<Void>> {

	private String contextId;
	private String asiId;

	/**
	 * @param air
	 * @param atmosphere
	 * @param username
	 */
	public RemoveASIFromWorkflowAction(ActionFactory actionFactory,
			String username, String contextId, String asiId) {
		super(actionFactory, username);
		this.contextId = contextId;
		this.asiId = asiId;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		if (workflowInDevelopmentModeHasASI()) {
			ApplianceType at = new GetASITypeAction(getActionFactory(), asiId)
					.execute();

			ManagerResponse response = getAtmosphere().removeAppliance(asiId);
			parseResponseAndThrowExceptionsWhenNeeded(response);

			if (at.isDevelopment()) {
				Action<Class<Void>> deleteASAction = getActionFactory()
						.createDeleteAtomicServiceFromAirAction(at.getId());
				deleteASAction.execute();
			}
		} else {
			throw new AtomicServiceInstanceNotFoundException();
		}
		return Void.TYPE;
	}

	private boolean workflowInDevelopmentModeHasASI() {
		WorkflowDetail wd = getUserWorkflow(contextId, getUsername());
		if (wd.getWorkflow_type() == WorkflowType.development) {
			if (wd.getVms() != null) {
				for (Vms vm : wd.getVms()) {
					if (vm.getVms_id().equals(asiId)) {
						return true;
					}
				}
			}
		} else {
			throw new WorkflowNotInDevelopmentModeException();
		}
		return false;
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
	}
}
