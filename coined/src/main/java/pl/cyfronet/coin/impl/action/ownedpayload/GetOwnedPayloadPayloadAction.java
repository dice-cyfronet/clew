package pl.cyfronet.coin.impl.action.ownedpayload;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ReadOnlyAirAction;
import pl.cyfronet.coin.impl.action.ownedpayload.provider.OwnedPayloadActions;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetOwnedPayloadPayloadAction extends ReadOnlyAirAction<String> {

	private String ownedPayloadName;
	private OwnedPayloadActions actions;

	public GetOwnedPayloadPayloadAction(AirClient air, String ownedPayloadName,
			OwnedPayloadActions actions) {
		super(air);
		this.ownedPayloadName = ownedPayloadName;
		this.actions = actions;
	}

	@Override
	public String execute() throws CloudFacadeException {
		Action<NamedOwnedPayload> action = new GetOwnedPayloadAction(getAir(),
				ownedPayloadName, actions);
		return action.execute().getPayload();
	}
}
