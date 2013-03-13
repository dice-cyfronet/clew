package pl.cyfronet.coin.impl.action.securitypolicy;

import java.util.List;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.impl.action.ownedpayload.NewOwnedPayloadAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

public class NewSecurityPolicyAction extends NewOwnedPayloadAction {

	public NewSecurityPolicyAction(AirClient air, String username,
			NamedOwnedPayload newPolicy) {
		super(air, username, newPolicy);
	}

	@Override
	protected void addOwnedPayload(String name, String payload, List<String> owners) {
		getAir().addSecurityPolicy(name, payload, owners);
	}
	
	@Override
	protected DeleteSecurityPolicyAction getDeleteAction(String username, String ownedPayloadName) {
		return new DeleteSecurityPolicyAction(
				getAir(), username, ownedPayloadName);
	}
}