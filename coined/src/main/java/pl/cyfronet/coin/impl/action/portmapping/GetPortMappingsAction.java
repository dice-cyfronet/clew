package pl.cyfronet.coin.impl.action.portmapping;

import java.util.List;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.ReadOnlyAsiAction;
import pl.cyfronet.coin.impl.air.client.ATPortMapping;

public class GetPortMappingsAction extends
		ReadOnlyAsiAction<List<ATPortMapping>> {

	public GetPortMappingsAction(ActionFactory actionFactory, String username,
			String contextId, String asiId) {
		super(actionFactory, username, contextId, asiId);
	}

	@Override
	public List<ATPortMapping> execute() throws CloudFacadeException {
		return getApplianceType(false).getPort_mappings();
	}
}
