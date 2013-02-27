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

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.dyrealla.api.DyReAllaManagerService;
import pl.cyfronet.dyrealla.api.allocation.ManagerResponse;
import pl.cyfronet.dyrealla.api.allocation.OperationStatus;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 *
 * @param <T> Object returned by the action.
 */
public abstract class WorkflowAction<T> extends AtmosphereAndAirAction<T> {

	public WorkflowAction(AirClient air, DyReAllaManagerService atmosphere,
			String username) {
		super(air, atmosphere, username);
	}

	protected WorkflowDetail getUserWorkflow(String contextId, String username) {
		return new GetWorkflowDetailAction(getAir(), contextId, username)
				.execute();
	}

	/**
	 * @return Workflow detail.
	 * @throws WorkflowNotFoundException Thrown when workflow is not found or it
	 *             does not belong to the user.
	 */
	protected void checkIfWorkflowBelongsToUser(String contextId,
			String username) throws WorkflowNotFoundException {
		getUserWorkflow(contextId, username);
	}

	protected void parseResponseAndThrowExceptionsWhenNeeded(
			ManagerResponse response) throws CloudFacadeException {
		logger.debug("Atmosphere action response {}", response);
		if (OperationStatus.FAILED == response.getOperationStatus()) {
			String errorMessage = getErrorMessage(response);
			throw new CloudFacadeException(errorMessage);
		}
	}

	private String getErrorMessage(ManagerResponse response) {
		if (response.getErrors() != null) {
			return response.getErrors().toString();
		}

		return null;
	}
}
