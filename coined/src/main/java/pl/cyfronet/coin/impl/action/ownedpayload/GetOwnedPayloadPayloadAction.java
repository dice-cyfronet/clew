package pl.cyfronet.coin.impl.action.ownedpayload;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.ReadOnlyAirAction;
import pl.cyfronet.coin.impl.action.ownedpayload.provider.OwnedPayloadActions;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetOwnedPayloadPayloadAction extends ReadOnlyAirAction<String> {

	private String ownedPayloadName;
	private OwnedPayloadActions actions;

	public GetOwnedPayloadPayloadAction(ActionFactory actionFactory,
			String ownedPayloadName, OwnedPayloadActions actions) {
		super(actionFactory);
		this.ownedPayloadName = ownedPayloadName;
		this.actions = actions;
	}

	@Override
	public String execute() throws CloudFacadeException {
		Action<NamedOwnedPayload> action = new GetOwnedPayloadAction(
				getActionFactory(), ownedPayloadName, actions);
		return action.execute().getPayload();
	}
}
