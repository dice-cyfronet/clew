package pl.cyfronet.coin.impl.action.ownedpayload;

import java.util.List;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.beans.OwnedPayload;
import pl.cyfronet.coin.impl.action.Action;

public interface MethodProvider {
	void mockGetOwnedPayload(String username, String payloadName,
			List<NamedOwnedPayload> result);

	void verifyGetOwnedPayload(int times, String username, String payloadName);

	void verifyOwnedPayloadDeleted(int times, String username,
			String payloadName);

	void verifyAddOwnedPayload(int times, String payloadName,
			String payloadText, List<String> owners);

	void throwDeleteOwnedPayloadException(int status, String differentUser,
			String payloadName);

	void thenVerifyAirAskedAboutOwnedPayload();

	void whenGetOwnedPayload(String username, String ownedPayloadName,
			List<NamedOwnedPayload> ownedPayloads);

	void mockOwnedPayloadNotExistsInAir(String policyName);

	void throwExceptionWhileAddingOwnedPayload(int status, String payloadName,
			String payloadText, List<String> owners);

	void verifyUpdateOwnedPayload(int i, String username, String policyName,
			String newPayload, List<String> newOwners);

	void throwUpdateOwnedPayloadException(int i, String differentUser,
			String policyName, String newPayload, List<String> newOwners);
	
	Action<Class<Void>> getDeleteOwnedPayloadAction(String username,
			String payloadName);

	Action<NamedOwnedPayload> getGetOwnedPayloadAction(String payloadName);

	Action<String> getOwnedPayloadPayloadAction(String payloadName);

	Action<List<String>> getListOwnedPayloadAction();

	Action<Class<Void>> getNewOwnedPayloadAction(String username,
			NamedOwnedPayload newPolicy);

	Action<Class<Void>> createUpdateOwnedPayloadAction(String username,
			String payloadName, OwnedPayload newPayload);

}
