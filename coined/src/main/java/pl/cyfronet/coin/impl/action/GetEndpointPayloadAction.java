package pl.cyfronet.coin.impl.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.EndpointNotFoundException;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

public class GetEndpointPayloadAction extends AirAction<String> {

	private static final Logger logger = LoggerFactory
			.getLogger(GetEndpointPayloadAction.class);

	private String atomicServiceId;
	private int servicePort;
	private String invocationPath;

	GetEndpointPayloadAction(AirClient air, String atomicServiceId, int servicePort,
			String invocationPath) {
		super(air);
		this.atomicServiceId = atomicServiceId;
		this.servicePort = servicePort;
		this.invocationPath = invocationPath;
	}

	@Override
	public String execute() throws CloudFacadeException {
		ATEndpoint endpoint = getEndpoint(atomicServiceId, servicePort,
				invocationPath);
		String endpointId = endpoint.getId();
		return getAir().getEndpointDescriptor(endpointId);
	}

	private ATEndpoint getEndpoint(String atomicServiceId, int servicePort,
			String invocationPath) throws AtomicServiceNotFoundException,
			EndpointNotFoundException {

		ApplianceType type = getApplianceType(atomicServiceId);

		for (ATEndpoint endpoint : type.getEndpoints()) {
			int endpointPort = endpoint.getPort();
			String endpointInvocationPath = endpoint.getInvocation_path();
			logger.debug("Comparing {}=={} and {}=={}", new Object[] {
					endpointPort, servicePort, endpointInvocationPath,
					invocationPath });
			if (endpointPort == servicePort
					&& endpointPathEquals(endpointInvocationPath,
							invocationPath)) {
				return endpoint;
			}
		}

		logger.debug("Endpoint {}:{}/{} not found", new Object[] {
				atomicServiceId, servicePort, invocationPath });
		throw new EndpointNotFoundException();
	}

	private boolean endpointPathEquals(String endpointPath,
			String invocationPath) {
		String endpointPathWithouldSlashAtTheBeginning = removeSlashesFromTheBeginning(endpointPath);
		String invocationPathWithouldSlashAtTheBeginning = removeSlashesFromTheBeginning(invocationPath);
		return endpointPathWithouldSlashAtTheBeginning
				.equalsIgnoreCase(invocationPathWithouldSlashAtTheBeginning);
	}

	private String removeSlashesFromTheBeginning(String str) {
		return str.replaceAll("^/+", "");
	}

	@Override
	public void rollback() {
		// read only action no rollback needed
	}

}
