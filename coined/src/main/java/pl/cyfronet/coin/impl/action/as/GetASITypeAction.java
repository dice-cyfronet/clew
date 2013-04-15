package pl.cyfronet.coin.impl.action.as;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.ReadOnlyAirAction;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

public class GetASITypeAction extends ReadOnlyAirAction<ApplianceType> {

	private static final Logger logger = LoggerFactory.getLogger(GetASITypeAction.class);
	
	private String instanceId;

	public GetASITypeAction(ActionFactory actionFactory, String instanceId) {
		super(actionFactory);
		this.instanceId = instanceId;
	}

	@Override
	public ApplianceType execute() throws CloudFacadeException {
		try {
			return getAir().getTypeFromVM(instanceId);
		} catch (WebApplicationException e) {
			logger.warn("ASI with {} id not found in AIR", instanceId);
			throw new AtomicServiceInstanceNotFoundException();
		}
	}

}
