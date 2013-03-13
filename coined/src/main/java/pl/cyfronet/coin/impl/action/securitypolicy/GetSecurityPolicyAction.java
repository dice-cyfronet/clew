package pl.cyfronet.coin.impl.action.securitypolicy;

import java.util.List;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.impl.action.ownedpayload.GetOwnedPayloadAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetSecurityPolicyAction extends GetOwnedPayloadAction {

	public GetSecurityPolicyAction(AirClient air, String policyName) {
		super(air, policyName);
	}

	public GetSecurityPolicyAction(AirClient air, String username,
			String policyName) {
		super(air, username, policyName);
	}
	
	@Override
	protected List<NamedOwnedPayload> getOwnedPayload(String username, String ownedPayloadName) {
		return getAir().getSecurityPolicies(
				username, ownedPayloadName);
	}
}