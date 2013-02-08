package pl.cyfronet.coin.impl.action.grant;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.AirAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

public class DeleteGrantAction extends AirAction<Class<Void>> {

	public DeleteGrantAction(AirClient air, String name) {
		super(air);
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		return null;
	}

	@Override
	public void rollback() {
		
	}

}
