package pl.cyfronet.coin.impl.action.portmapping;

import javax.ws.rs.WebApplicationException;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.RedirectionNotFoundException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.AirAction;

public class RemovePortMappingAction extends AirAction<Class<Void>> {

	private String redirectionId;

	public RemovePortMappingAction(ActionFactory actionFactory,
			String redirectionId) {
		super(actionFactory);
		this.redirectionId = redirectionId;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		try {
			getAir().removePortMapping(redirectionId);
		} catch (WebApplicationException e) {
			if (e.getResponse().getStatus() == 404) {
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
