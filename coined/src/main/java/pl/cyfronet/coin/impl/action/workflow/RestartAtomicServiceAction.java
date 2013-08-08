package pl.cyfronet.coin.impl.action.workflow;

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.dyrealla.api.InternalDyReAllaException;
import pl.cyfronet.dyrealla.api.VMSavingException;

public class RestartAtomicServiceAction extends WorkflowAction<Class<Void>> {

	private String contextId;
	private String asiId;

	public RestartAtomicServiceAction(ActionFactory actionFactory,
			String username, String contextId, String asiId) {
		super(actionFactory, username);
		this.contextId = contextId;
		this.asiId = asiId;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		WorkflowDetail workflowDetails = getUserWorkflow(contextId,
				getUsername());
		if (workflowDetails.getWorkflow_type() == WorkflowType.development) {
			Vms asi = getAsi(getUsername(), contextId, asiId);
			try {
				getAtmosphere().restartAppliance(asi.getVms_id());
			} catch (InternalDyReAllaException | VMSavingException e) {
				throw new CloudFacadeException(e.getMessage());
			}
		} else {
			throw new WorkflowNotInDevelopmentModeException();
		}
		return null;
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}

}
