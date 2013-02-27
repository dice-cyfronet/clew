package pl.cyfronet.coin.impl.action.redirection;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.RedirectionNotFoundException;
import pl.cyfronet.coin.impl.action.portmapping.GetPortMappingsAction;
import pl.cyfronet.coin.impl.action.portmapping.RemovePortMappingAction;
import pl.cyfronet.coin.impl.air.client.ATPortMapping;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.dyrealla.api.DyReAllaException;
import pl.cyfronet.dyrealla.api.DyReAllaManagerService;
import pl.cyfronet.dyrealla.api.VirtualMachineNotFoundException;
import pl.cyfronet.dyrealla.api.dnat.DyReAllaDNATManagerService;
import pl.cyfronet.dyrealla.api.dnat.Protocol;
import pl.cyfronet.dyrealla.api.proxy.DyReAllaProxyManagerService;

public class RemoveAsiRedirectionAction extends
		AsiRedirectionAction<Class<Void>> {

	private static final Logger logger = LoggerFactory
			.getLogger(AddAsiRedirectionAction.class);

	private String redirectionId;

	public RemoveAsiRedirectionAction(AirClient air,
			DyReAllaManagerService atmosphere,
			DyReAllaProxyManagerService httpRedirectionService,
			DyReAllaDNATManagerService dnatRedirectionService, String username,
			String contextId, String asiId, String redirectionId) {
		super(air, atmosphere, httpRedirectionService, dnatRedirectionService,
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
		if (portMapping.isHttp()) {
			removeHttpPortMapping(serviceName, port);
		} else {
			removeDnatPortMapping(port);
		}

		logger.debug("Removing redirection from air");
		new RemovePortMappingAction(getAir(), redirectionId).execute();

		return Void.TYPE;
	}

	private void removeHttpPortMapping(String serviceName, int port) {
		try {
			getHttpRedirectionService().unregisterHttpService(getContextId(),
					getAsiId(), serviceName, port);
		} catch (VirtualMachineNotFoundException e) {
			logger.debug("Error while adding http redirection", e);
			throw new AtomicServiceInstanceNotFoundException();
		} catch (DyReAllaException e) {
			logger.debug("Error while adding http redirection", e);
			throw new CloudFacadeException(e.getMessage());
		}
	}

	private void removeDnatPortMapping(int port) {
		try {
			getDnatRedirectionService().removePortRedirection(getAsiId(), port,
					Protocol.TCP);
		} catch (VirtualMachineNotFoundException e) {
			logger.debug("Error while removing dnat redirection", e);
			throw new AtomicServiceInstanceNotFoundException();
		} catch (DyReAllaException e) {
			logger.debug("Error while removing dnat redirection", e);
			throw new CloudFacadeException(e.getMessage());
		}
	}

	private ATPortMapping getPortMapping() {
		List<ATPortMapping> portMappings = new GetPortMappingsAction(getAir(),
				getUsername(), getContextId(), getAsiId()).execute();
		for (ATPortMapping portMapping : portMappings) {
			if (portMapping.getId().equals(redirectionId)) {
				return portMapping;
			}
		}
		throw new RedirectionNotFoundException();
	}

	@Override
	public void rollback() {
		//TODO
	}
}
