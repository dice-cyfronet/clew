package pl.cyfronet.coin.impl.action;

import javax.ws.rs.WebApplicationException;

import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

public class GetASITypeAction extends ReadOnlyAirAction<ApplianceType> {

	private String instanceId;

	public GetASITypeAction(AirClient air, String instanceId) {
		super(air);
		this.instanceId = instanceId;
	}

	@Override
	public ApplianceType execute() throws CloudFacadeException {
		try {
			return getAir().getTypeFromVM(instanceId);
		} catch (WebApplicationException e) {
			throw new AtomicServiceInstanceNotFoundException();
		}
	}

}
