package pl.cyfronet.coin.impl.action.endpoint;

import static pl.cyfronet.coin.impl.BeanConverter.getEndpoints;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.redirection.HttpRedirection;
import pl.cyfronet.coin.api.beans.redirection.Redirections;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.Action;
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
		List<Endpoint> endpoints = getEndpoints(getApplianceType(true));

		Action<Redirections> rAction = getActionFactory()
				.createGetAsiRedirectionsAction(getUsername(), getContextId(),
						getAsiId());
		Redirections redirections = rAction.execute();

		for (Endpoint endpoint : endpoints) {
			HttpRedirection httpRedirection = getHttpRedirection(redirections,
					endpoint.getPort());
			if(httpRedirection != null) {
				List<String> urls = new ArrayList<>();
				for (String url : httpRedirection.getUrls()) {
					url = url.trim();
					String invocationPath = endpoint.getInvocationPath().trim();
					String separator = url.endsWith("/") || invocationPath.startsWith("/") ? "" : "/";
					urls.add(String.format("%s%s%s", url, separator, invocationPath));
				}
				endpoint.setUrls(urls);
			}
		}

		return endpoints;
	}

	private HttpRedirection getHttpRedirection(Redirections redirections,
			Integer port) {
		if (redirections.getHttp() != null) {
			for (HttpRedirection httpRedirection : redirections.getHttp()) {
				if(httpRedirection.getToPort().equals(port)) {
					return httpRedirection;
				}
			}
		}
		return null;
	}
}
