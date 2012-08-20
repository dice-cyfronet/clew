package pl.cyfronet.coin.impl.action;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.dyrealla.core.DyReAllaManagerService;

/**
 * Stop workflow action
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class StopWorkflowAction extends WorkflowAction<Class<Void>> {

	private String contextId;

	/**
	 * @param air Air client.
	 * @param atmosphere Atmosphere client.	
	 * @param contextId Workflow id.
	 * @param username User which wants to stop the workflow.
	 */
	public StopWorkflowAction(AirClient air, DyReAllaManagerService atmosphere,
			String contextId, String username) {
		super(air, atmosphere, username);
		this.contextId = contextId;
	}

	/**
	 * @throws WorkflowNotFoundException Thrown when workflow is not found or
	 *             workflow with defined context id belongs to other user.
	 */
	@Override
	public Class<Void> execute() throws CloudFacadeException {
		logger.debug("stopping workflow {}", contextId);
		checkIfWorkflowBelongsToUser(contextId, getUsername());
		// ManagerResponse response =
		getAtmosphere().removeRequiredAppliances(contextId);

		// FIXME waiting for atmo improvement
		// parseResponseAndThrowExceptionsWhenNeeded(response);
		getAir().stopWorkflow(contextId);

		return Void.TYPE;
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
	}

}
