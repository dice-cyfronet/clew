package pl.cyfronet.coin.impl.action.portmapping;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.AirAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

public class AddPortMappingAction extends AirAction<String> {

	private String asId;
	private String serviceName;
	private int port;
	private boolean http;

	public AddPortMappingAction(AirClient air, String asId, String serviceName,
			int port, boolean http) {
		super(air);
		this.asId = asId;
		this.serviceName = serviceName;
		this.port = port;
		this.http = http;
	}

	@Override
	public String execute() throws CloudFacadeException {
		try {
			return getAir().addPortMapping(asId, serviceName, port, http);
		} catch (ServerWebApplicationException e) {
			if (e.getStatus() == 500) {
				throw new AtomicServiceNotFoundException();
			}
			throw new CloudFacadeException();
		}
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}
}
