package pl.cyfronet.coin.impl.action;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.InitialConfigurationAlreadyExistException;
import pl.cyfronet.coin.impl.air.client.AirClient;

public class AddInitialConfigurationAction extends AirAction<String> {

	private String atomicServiceId;
	private InitialConfiguration initialConfiguration;

	AddInitialConfigurationAction(AirClient air, String atomicServiceId,
			InitialConfiguration initialConfiguration) {
		super(air);
		this.atomicServiceId = atomicServiceId;
		this.initialConfiguration = initialConfiguration;
	}

	@Override
	public String execute() throws CloudFacadeException {
		try {
			String addedConfigurationId = getAir().addInitialConfiguration(
					initialConfiguration.getName(), atomicServiceId,
					initialConfiguration.getPayload());

			return addedConfigurationId;
		} catch (ServerWebApplicationException e) {
			if (e.getMessage() != null) {
				if (e.getMessage().contains("not found in AIR")) {
					throw new AtomicServiceNotFoundException();
				} else if (e.getMessage().contains("duplicated configuration")) {
					throw new InitialConfigurationAlreadyExistException();
				}
			}
			throw new CloudFacadeException(e.getMessage());
		}
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}

}
