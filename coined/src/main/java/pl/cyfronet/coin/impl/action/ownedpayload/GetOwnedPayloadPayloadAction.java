package pl.cyfronet.coin.impl.action.ownedpayload;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.ReadOnlyAirAction;
import pl.cyfronet.coin.impl.action.securitypolicy.GetSecurityPolicyAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public abstract class GetOwnedPayloadPayloadAction extends
		ReadOnlyAirAction<String> {

	private String ownedPayloadName;

	public GetOwnedPayloadPayloadAction(AirClient air, String ownedPayloadName) {
		super(air);
		this.ownedPayloadName = ownedPayloadName;
	}

	@Override
	public String execute() throws CloudFacadeException {
		GetSecurityPolicyAction action = getOwnedPayloadAction(ownedPayloadName);
		return action.execute().getPayload();
	}

	protected abstract GetSecurityPolicyAction getOwnedPayloadAction(
			String ownedPayloadName);
}
