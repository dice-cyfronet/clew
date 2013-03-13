package pl.cyfronet.coin.impl.action.ownedpayload;

import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.ReadOnlyAirAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public abstract class ListOwnedPayloadAction extends
		ReadOnlyAirAction<List<String>> {

	public ListOwnedPayloadAction(AirClient air) {
		super(air);
	}

	@Override
	public List<String> execute() throws CloudFacadeException {
		List<NamedOwnedPayload> policies = getOwnedPayloads(null, null);
		List<String> policyNames = new ArrayList<String>();
		for (NamedOwnedPayload policy : policies) {
			policyNames.add(policy.getName());
		}
		return policyNames;
	}

	protected abstract List<NamedOwnedPayload> getOwnedPayloads(
			String username, String ownedPayloadName);
}
