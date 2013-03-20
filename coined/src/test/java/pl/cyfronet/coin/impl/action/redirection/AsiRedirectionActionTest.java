package pl.cyfronet.coin.impl.action.redirection;

import static org.mockito.Mockito.mock;
import pl.cyfronet.coin.impl.action.workflow.WorkflowActionTest;
import pl.cyfronet.dyrealla.api.dnat.DyReAllaDNATManagerService;
import pl.cyfronet.dyrealla.api.proxy.DyReAllaProxyManagerService;

public abstract class AsiRedirectionActionTest extends WorkflowActionTest {

	protected DyReAllaProxyManagerService httpRedirectionService;
	protected DyReAllaDNATManagerService dnatRedirectionService;
	
	protected String asiId = "asId";
	protected int port = 123;
	
	@Override
	protected void postSetUp() {
		httpRedirectionService = mock(DyReAllaProxyManagerService.class);
		dnatRedirectionService = mock(DyReAllaDNATManagerService.class);

		actionFactory.setHttpRedirectionService(httpRedirectionService);
		actionFactory.setDnatRedirectionService(dnatRedirectionService);
	}
}
