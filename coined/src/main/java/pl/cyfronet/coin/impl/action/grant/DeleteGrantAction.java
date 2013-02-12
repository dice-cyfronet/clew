package pl.cyfronet.coin.impl.action.grant;

import javax.ws.rs.WebApplicationException;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.GrantNotFoundException;
import pl.cyfronet.coin.impl.action.AirAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

public class DeleteGrantAction extends AirAction<Class<Void>> {

	private String name;

	public DeleteGrantAction(AirClient air, String name) {
		super(air);
		this.name = name;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		try {
			getAir().deleteGrant(name);
		} catch (WebApplicationException e) {
			throw new GrantNotFoundException();
		}

		return Void.TYPE;
	}

	@Override
	public void rollback() {

	}

}
