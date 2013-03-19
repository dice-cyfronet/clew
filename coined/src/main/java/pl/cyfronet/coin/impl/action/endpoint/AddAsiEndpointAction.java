package pl.cyfronet.coin.impl.action.endpoint;

import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.AsiAction;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

public class AddAsiEndpointAction extends AsiAction<String> {

	private Endpoint endpoint;

	public AddAsiEndpointAction(ActionFactory actionFactory, String username,
			String contextId, String asiId, Endpoint endpoint) {
		super(actionFactory, username, contextId, asiId);
		this.endpoint = endpoint;
	}

	@Override
	public String execute() throws CloudFacadeException {
		WorkflowDetail wd = getUserWorkflow(getContextId(), getUsername());
		ensureDevelopmentWorklow(wd);

		String atId = getAsiApplianceTypeId(wd);

		return getAir().addEndpoint("rest", atId, endpoint.getInvocationPath(),
				endpoint.getType(), endpoint.getDescription(),
				endpoint.getDescriptor(), endpoint.getPort());
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}
}
