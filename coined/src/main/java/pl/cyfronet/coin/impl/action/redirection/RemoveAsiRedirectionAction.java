package pl.cyfronet.coin.impl.action.redirection;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.RedirectionNotFoundException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.air.client.ATPortMapping;
import pl.cyfronet.dyrealla.api.DyReAllaException;
import pl.cyfronet.dyrealla.api.VirtualMachineNotFoundException;
import pl.cyfronet.dyrealla.api.dnat.DyReAllaDNATManagerService;
import pl.cyfronet.dyrealla.api.dnat.Protocol;
import pl.cyfronet.dyrealla.api.proxy.DyReAllaProxyManagerService;
import pl.cyfronet.dyrealla.api.proxy.HttpProtocol;

public class RemoveAsiRedirectionAction extends
		AsiRedirectionAction<Class<Void>> {

	private static final Logger logger = LoggerFactory
			.getLogger(RemoveAsiRedirectionAction.class);

	private String redirectionId;

	public RemoveAsiRedirectionAction(ActionFactory actionFactory,
			DyReAllaProxyManagerService httpRedirectionService,
			DyReAllaDNATManagerService dnatRedirectionService, String username,
			String contextId, String asiId, String redirectionId) {
		super(actionFactory, httpRedirectionService, dnatRedirectionService,
				username, contextId, asiId);
		this.redirectionId = redirectionId;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		ATPortMapping portMapping = getPortMapping();
		int port = portMapping.getPort();
		String serviceName = portMapping.getService_name();
		logger.debug("Removing redirection: {} -> {}", serviceName, portMapping);

		logger.debug("Removing redirection using DyReAlla");
		if (portMapping.isHttp() || portMapping.isHttps()) {
			removeHttpPortMapping(serviceName, port, portMapping.isHttp(),
					portMapping.isHttps());
		} else {
			removeDnatPortMapping(port);
		}

		logger.debug("Removing redirection from air");
		getActionFactory().createRemovePortMappingAction(redirectionId)
				.execute();

		return Void.TYPE;
	}

	private void removeHttpPortMapping(String serviceName, int port,
			boolean http, boolean https) {
		if (http) {
			removeHttpPortMapping(serviceName, port, HttpProtocol.HTTP);
		}

		if (https) {
			removeHttpPortMapping(serviceName, port, HttpProtocol.HTTPS);
		}
	}

	private void removeHttpPortMapping(String serviceName, int port,
			HttpProtocol protocol) {
		try {
			getHttpRedirectionService().unregisterHttpService(getContextId(),
					getAsiId(), serviceName, port, protocol);
		} catch (VirtualMachineNotFoundException e) {
			logger.error("ASI VM not found", e);
			throw new AtomicServiceInstanceNotFoundException();
		} catch (DyReAllaException e) {
			logger.error("Error while adding http redirection", e);
			throw new CloudFacadeException(e.getMessage());
		}
	}

	private void removeDnatPortMapping(int port) {
		try {
			getDnatRedirectionService().removePortRedirection(getAsiId(), port,
					Protocol.TCP);
		} catch (VirtualMachineNotFoundException e) {
			logger.error("ASI VM not found", e);
			throw new AtomicServiceInstanceNotFoundException();
		} catch (DyReAllaException e) {
			logger.error("Error while removing dnat redirection", e);
			throw new CloudFacadeException(e.getMessage());
		}
	}

	private ATPortMapping getPortMapping() {
		List<ATPortMapping> portMappings = getActionFactory()
				.createGetPortMappingsAction(getUsername(), getContextId(),
						getAsiId()).execute();
		for (ATPortMapping portMapping : portMappings) {
			if (portMapping.getId().equals(redirectionId)) {
				return portMapping;
			}
		}
		throw new RedirectionNotFoundException();
	}

	@Override
	public void rollback() {
		// TODO
	}
}
