package pl.cyfronet.coin.impl.action.endpoint;

import static pl.cyfronet.coin.impl.BeanConverter.getEndpoints;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.ReadOnlyAsiAction;

public class ListAsiEndpointsAction extends ReadOnlyAsiAction<List<Endpoint>> {

	private static final Logger logger = LoggerFactory
			.getLogger(ListAsiEndpointsAction.class);

	public ListAsiEndpointsAction(ActionFactory actionFactory, String username,
			String contextId, String asiId) {
		super(actionFactory, username, contextId, asiId);
	}

	@Override
	public List<Endpoint> execute() throws CloudFacadeException {
		logger.debug("Getting endpoints for {} {}", getContextId(), getAsiId());
		return getEndpoints(getApplianceType());
	}
}
