package pl.cyfronet.coin.impl.action;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.SecurityPolicyNotFoundException;
import pl.cyfronet.coin.impl.air.client.AirClient;

public class GetSecurityPolicyAction extends AirAction<String> {

	private String policyName;

	GetSecurityPolicyAction(AirClient air, String policyName) {
		super(air);
		this.policyName = policyName;
	}

	@Override
	public String execute() throws CloudFacadeException {
		try {
			return getAir().getSecurityPolicy(policyName);
		} catch (ServerWebApplicationException e) {
			if (e.getStatus() == 400) {
				throw new SecurityPolicyNotFoundException();
			}
			throw new CloudFacadeException(
					"Error while geting security policy from Air, response code"
							+ e.getStatus());
		}
	}

	@Override
	public void rollback() {
		// read only action - no rollback needed.
	}

}
