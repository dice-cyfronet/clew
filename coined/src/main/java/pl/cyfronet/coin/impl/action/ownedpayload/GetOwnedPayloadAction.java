package pl.cyfronet.coin.impl.action.ownedpayload;

import java.util.List;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.ownedpayload.provider.OwnedPayloadActions;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetOwnedPayloadAction extends
		OwnedPayloadAction<NamedOwnedPayload> {

	private String ownedPayloadName;
	private String username;
	private OwnedPayloadActions actions;

	public GetOwnedPayloadAction(OwnedPayloadActionFactory actionFactory,
			String ownedPayloadName, OwnedPayloadActions actions) {
		this(actionFactory, null, ownedPayloadName, actions);
	}

	public GetOwnedPayloadAction(OwnedPayloadActionFactory actionFactory,
			String username, String ownedPayloadName,
			OwnedPayloadActions actions) {
		super(actionFactory);
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