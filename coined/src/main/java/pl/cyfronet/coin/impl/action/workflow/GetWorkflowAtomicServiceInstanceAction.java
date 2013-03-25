package pl.cyfronet.coin.impl.action.workflow;

import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.beans.Workflow;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.BaseAction;

public class GetWorkflowAtomicServiceInstanceAction extends
		BaseAction<AtomicServiceInstance> {

	private String username;
	private String workflowId;
	private String asiId;

	public GetWorkflowAtomicServiceInstanceAction(ActionFactory actionFactory,
			String username, String workflowId, String asiId) {
		super(actionFactory);
		this.username = username;
		this.workflowId = workflowId;
		this.asiId = asiId;
	}

	@Override
	public AtomicServiceInstance execute() throws CloudFacadeException {
		Workflow workflow = getActionFactory().createGetUserWorkflowAction(
				workflowId, username).execute();
		if (workflow.getAtomicServiceInstances() != null) {
			for (AtomicServiceInstance instance : workflow
					.getAtomicServiceInstances()) {
				if (asiId.equals(instance.getId())) {
					return instance;
				}
			}
		}
		throw new AtomicServiceInstanceNotFoundException();
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}

}
