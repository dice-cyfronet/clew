package pl.cyfronet.coin.impl.action.securitypolicy;

import java.util.List;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.ReadOnlyAirAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetSecurityPolicyAction extends
		ReadOnlyAirAction<NamedOwnedPayload> {

	private String policyName;
	private String username;

	public GetSecurityPolicyAction(AirClient air, String policyName) {
		super(air);
		this.policyName = policyName;
	}

	public GetSecurityPolicyAction(AirClient air, String username,
			String policyName) {
		super(air);
		this.policyName = policyName;
		this.username = username;
	}

	@Override
	public NamedOwnedPayload execute() throws CloudFacadeException {
		List<NamedOwnedPayload> policies = getAir().getSecurityPolicies(
				username, policyName);
		if (policies == null || policies.size() < 1) {
			throw new NotFoundException();
		}
		return policies.get(0);
	}
}
