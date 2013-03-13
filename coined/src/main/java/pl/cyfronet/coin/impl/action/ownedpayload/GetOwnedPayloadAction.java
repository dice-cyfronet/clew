package pl.cyfronet.coin.impl.action.ownedpayload;

import java.util.List;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.ReadOnlyAirAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public abstract class GetOwnedPayloadAction extends
		ReadOnlyAirAction<NamedOwnedPayload> {

	private String ownedPayloadName;
	private String username;

	public GetOwnedPayloadAction(AirClient air, String ownedPayloadName) {
		super(air);
		this.ownedPayloadName = ownedPayloadName;
	}

	public GetOwnedPayloadAction(AirClient air, String username,
			String ownedPayloadName) {
		super(air);
		this.ownedPayloadName = ownedPayloadName;
		this.username = username;
	}

	@Override
	public NamedOwnedPayload execute() throws CloudFacadeException {
		List<NamedOwnedPayload> payloads = getOwnedPayload(username,
				ownedPayloadName);
		if (payloads == null || payloads.size() < 1) {
			throw new NotFoundException();
		}
		return payloads.get(0);
	}

	protected abstract List<NamedOwnedPayload> getOwnedPayload(String username,
			String ownedPayloadName);
}