package pl.cyfronet.coin.impl.action.portmapping;

import java.util.List;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.ReadOnlyAsiAction;
import pl.cyfronet.coin.impl.air.client.ATPortMapping;
import pl.cyfronet.coin.impl.air.client.AirClient;

public class GetPortMappingsAction extends ReadOnlyAsiAction<List<ATPortMapping>> {

	public GetPortMappingsAction(AirClient air, String username,
			String contextId, String asiId) {
		super(air, username, contextId, asiId);
	}

	@Override
	public List<ATPortMapping> execute() throws CloudFacadeException {
		return getApplianceType().getPort_mappings();
	}
}
