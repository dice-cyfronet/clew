package pl.cyfronet.coin.impl.action.portmapping;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.AirAction;

public class AddPortMappingAction extends AirAction<String> {

	private static final Logger logger = LoggerFactory
			.getLogger(AddPortMappingAction.class);

	private String asId;
	private String serviceName;
	private int port;
	private boolean http;

	public AddPortMappingAction(ActionFactory actionFactory, String asId,
			String serviceName, int port, boolean http) {
		super(actionFactory);
		this.asId = asId;
		this.serviceName = serviceName;
		this.port = port;
		this.http = http;
	}

	@Override
	public String execute() throws CloudFacadeException {
		try {
			logger.debug("Adding port mapping {} port {} http {} for {}",
					new Object[] { serviceName, port, http, asId });
			return getAir().addPortMapping("rest", asId, serviceName, port,
					http);
		} catch (WebApplicationException e) {
			if (e.getResponse().getStatus() == 404) {
				logger.warn("Atomic Service {} not found", asId);
				throw new AtomicServiceNotFoundException();
			}
			logger.error("Error while contacting AIR", e);
			throw new CloudFacadeException(e.getMessage());
		}
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}
}
