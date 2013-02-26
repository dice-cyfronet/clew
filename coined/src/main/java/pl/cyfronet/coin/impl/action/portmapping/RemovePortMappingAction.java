package pl.cyfronet.coin.impl.action.portmapping;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.RedirectionNotFoundException;
import pl.cyfronet.coin.impl.action.AirAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

public class RemovePortMappingAction extends AirAction<Class<Void>> {

	private String redirectionId;

	public RemovePortMappingAction(AirClient air, String redirectionId) {
		super(air);
		this.redirectionId = redirectionId;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		try {
			getAir().removePortMapping(redirectionId);
		} catch (ServerWebApplicationException e) {
			if (e.getStatus() == 404) {
				throw new RedirectionNotFoundException();
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
