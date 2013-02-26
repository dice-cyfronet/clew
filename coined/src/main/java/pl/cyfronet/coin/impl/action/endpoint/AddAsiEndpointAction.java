package pl.cyfronet.coin.impl.action.endpoint;

import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.impl.action.AsiAction;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

public class AddAsiEndpointAction extends AsiAction<String> {

	private Endpoint endpoint;

	public AddAsiEndpointAction(AirClient air, String username,
			String contextId, String asiId, Endpoint endpoint) {
		super(air, username, contextId, asiId);
		this.endpoint = endpoint;
	}

	@Override
	public String execute() throws CloudFacadeException {
		WorkflowDetail wd = getUserWorkflow(getContextId(), getUsername());
		if (wd.getWorkflow_type() != WorkflowType.development) {
			throw new WorkflowNotInDevelopmentModeException();
		}

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
