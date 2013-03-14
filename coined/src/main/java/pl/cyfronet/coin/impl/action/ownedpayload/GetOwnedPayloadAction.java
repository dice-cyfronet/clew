package pl.cyfronet.coin.impl.action.ownedpayload;

import java.util.List;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.ReadOnlyAirAction;
import pl.cyfronet.coin.impl.action.ownedpayload.provider.OwnedPayloadActions;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetOwnedPayloadAction extends ReadOnlyAirAction<NamedOwnedPayload> {

	private String ownedPayloadName;
	private String username;
	private OwnedPayloadActions actions;

	public GetOwnedPayloadAction(AirClient air, String ownedPayloadName,
			OwnedPayloadActions actions) {
		this(air, null, ownedPayloadName, actions);
	}

	public GetOwnedPayloadAction(AirClient air, String username,
			String ownedPayloadName, OwnedPayloadActions actions) {
		super(air);
		this.ownedPayloadName = ownedPayloadName;
		this.username = username;
		this.actions = actions;
	}

	@Override
	public NamedOwnedPayload execute() throws CloudFacadeException {
		List<NamedOwnedPayload> payloads = actions.getOwnedPayloads(username,
				ownedPayloadName);
		if (payloads == null || payloads.size() < 1) {
			throw new NotFoundException();
		}
		return payloads.get(0);
	}
}