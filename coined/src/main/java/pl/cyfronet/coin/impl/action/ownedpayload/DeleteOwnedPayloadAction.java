package pl.cyfronet.coin.impl.action.ownedpayload;

import javax.ws.rs.WebApplicationException;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ownedpayload.provider.OwnedPayloadActions;

public class DeleteOwnedPayloadAction extends OwnedPayloadAction<Class<Void>> {

	private String ownedPayloadName;
	private String username;

	private NamedOwnedPayload payload;
	private OwnedPayloadActions actions;

	public DeleteOwnedPayloadAction(OwnedPayloadActionFactory actionFactory,
			String username, String ownedPayloadName,
			OwnedPayloadActions actions) {
		super(actionFactory);
		this.username = username;
		this.ownedPayloadName = ownedPayloadName;
		this.actions = actions;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		loadOldPolicyPayload();
		try {
			actions.deleteOwnedPayload(username, ownedPayloadName);
		} catch (WebApplicationException e) {
			if (e.getResponse().getStatus() == 404) {
				throw new NotAllowedException();
			}
			throw new CloudFacadeException(
					"Error while deleting security policy from Air, response code"
							+ e.getResponse().getStatus());
		}

		return Void.TYPE;
	}

	@Override
	public void rollback() {
		try {
			if (payload != null) {
				Action<Class<Void>> action = new NewOwnedPayloadAction(
						getActionFactory(), username, payload, actions);
				action.execute();
			}
		} catch (Exception e) {
			// best effort.
		}
	}

	private void loadOldPolicyPayload() throws NotFoundException {
		Action<NamedOwnedPayload> getPolicyAction = new GetOwnedPayloadAction(
				getActionFactory(), null, ownedPayloadName, actions);
		payload = getPolicyAction.execute();
	}
}
