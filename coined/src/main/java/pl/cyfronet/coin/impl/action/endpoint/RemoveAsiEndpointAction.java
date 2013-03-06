package pl.cyfronet.coin.impl.action.endpoint;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.EndpointNotFoundException;
import pl.cyfronet.coin.impl.action.AsiAction;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

public class RemoveAsiEndpointAction extends AsiAction<Class<Void>> {

	private static final Logger logger = LoggerFactory
			.getLogger(RemoveAsiEndpointAction.class);

	private String endpointId;

	public RemoveAsiEndpointAction(AirClient air, String username,
			String contextId, String asiId, String endpointId) {
		super(air, username, contextId, asiId);
		this.endpointId = endpointId;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		WorkflowDetail wd = getUserWorkflow(getContextId(), getUsername());
		ensureDevelopmentWorklow(wd);

		getAsiApplianceTypeId(wd);
		try {
			logger.debug("Removing endpoint {} for {} {}", new Object[] {
					endpointId, getContextId(), getAsiId() });
			getAir().removeEndpoint(endpointId);
		} catch (ServerWebApplicationException e) {
			if (e.getStatus() == 400) {
				throw new EndpointNotFoundException();
			}
			throw new CloudFacadeException(e.getMessage());
		}
		return Void.TYPE;
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
	}

}