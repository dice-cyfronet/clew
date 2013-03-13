package pl.cyfronet.coin.impl.action.ownedpayload.provider;

import java.util.List;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.impl.air.client.AirClient;

public class SecurityProxyActions implements OwnedPayloadActions {

	private AirClient air;

	public SecurityProxyActions(AirClient air) {
		this.air = air;
	}

	@Override
	public List<NamedOwnedPayload> getOwnedPayloads(String username,
			String ownedPayloadName) {
		return air.getSecurityProxies(username, ownedPayloadName);
	}

	@Override
	public void addOwnedPayload(String name, String payload, List<String> owners) {
		air.addSecurityProxy(name, payload, owners);
	}

	@Override
	public void updateOwnedPayload(String username, String ownedPayloadName,
			List<String> owners, String ownedPayloadToUpdate) {
		air.updateSecurityProxy(username, ownedPayloadName,
				ownedPayloadToUpdate, owners);
	}

	@Override
	public void deleteOwnedPayload(String username, String ownedPayloadName) {
		air.deleteSecurityProxy(username, ownedPayloadName);
	}
}
