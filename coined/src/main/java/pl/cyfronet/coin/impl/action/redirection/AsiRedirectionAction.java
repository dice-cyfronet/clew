package pl.cyfronet.coin.impl.action.redirection;

import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.workflow.WorkflowAction;
import pl.cyfronet.dyrealla.api.dnat.DyReAllaDNATManagerService;
import pl.cyfronet.dyrealla.api.proxy.DyReAllaProxyManagerService;

public abstract class AsiRedirectionAction<T> extends WorkflowAction<T> {

	private DyReAllaProxyManagerService httpRedirectionService;
	private DyReAllaDNATManagerService dnatRedirectionService;
	private String contextId;
	private String asiId;

	public AsiRedirectionAction(ActionFactory actionFactory,
			DyReAllaProxyManagerService httpRedirectionService,
			DyReAllaDNATManagerService dnatRedirectionService, String username,
			String contextId, String asiId) {
		super(actionFactory, username);
		this.httpRedirectionService = httpRedirectionService;
		this.dnatRedirectionService = dnatRedirectionService;
		this.contextId = contextId;
		this.asiId = asiId;
	}

	protected String getContextId() {
		return contextId;
	}

	protected String getAsiId() {
		return asiId;
	}

	protected DyReAllaProxyManagerService getHttpRedirectionService() {
		return httpRedirectionService;
	}

	protected DyReAllaDNATManagerService getDnatRedirectionService() {
		return dnatRedirectionService;
	}
}
