package pl.cyfronet.coin.impl.action.ownedpayload.provider;

import java.util.List;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;

public interface OwnedPayloadActions {

	void deleteOwnedPayload(String username, String ownedPayloadName);

	List<NamedOwnedPayload> getOwnedPayloads(String username,
			String ownedPayloadName);

	void addOwnedPayload(String name, String payload, List<String> owners);

	void updateOwnedPayload(String username, String ownedPayloadName,
			List<String> owners, String ownedPayloadToUpdate);
}
