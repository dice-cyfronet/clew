package pl.cyfronet.coin.impl.action;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.dyrealla.allocation.ManagerResponse;
import pl.cyfronet.dyrealla.allocation.OperationStatus;
import pl.cyfronet.dyrealla.core.DyReAllaManagerService;

public abstract class WorkflowAction<T> extends AirAction<T> {

	private DyReAllaManagerService atmosphere;
	private String username;

	WorkflowAction(AirClient air, DyReAllaManagerService atmosphere,
			String username) {
		super(air);
		this.atmosphere = atmosphere;
		this.username = username;
	}

	protected DyReAllaManagerService getAtmosphere() {
		return atmosphere;
	}

	protected String getUsername() {
		return username;
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
		if (response.getOperationStatus() == OperationStatus.FAILED) {
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
