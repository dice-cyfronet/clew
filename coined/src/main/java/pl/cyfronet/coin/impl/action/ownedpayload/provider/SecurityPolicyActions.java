package pl.cyfronet.coin.impl.action.ownedpayload.provider;

import java.util.List;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.impl.air.client.AirClient;

public class SecurityPolicyActions implements OwnedPayloadActions {

	private AirClient air;

	public SecurityPolicyActions(AirClient air) {
		this.air = air;
	}

	@Override
	public List<NamedOwnedPayload> getOwnedPayloads(String username,
			String ownedPayloadName) {
		return air.getSecurityPolicies(username, ownedPayloadName);
	}	

	@Override
	public void addOwnedPayload(String name, String payload, List<String> owners) {
		air.addSecurityPolicy(name, payload, owners);
	}

	@Override
	public void updateOwnedPayload(String username, String ownedPayloadName,
			List<String> owners, String ownedPayloadToUpdate) {
		air.updateSecurityPolicy(username, ownedPayloadName,
				ownedPayloadToUpdate, owners);
	}
	
	@Override
	public void deleteOwnedPayload(String username, String ownedPayloadName) {
		air.deleteSecurityPolicy(username, ownedPayloadName);
	}
}
