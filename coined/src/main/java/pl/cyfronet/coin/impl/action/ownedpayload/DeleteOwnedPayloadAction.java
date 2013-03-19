package pl.cyfronet.coin.impl.action.ownedpayload;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.AirAction;
import pl.cyfronet.coin.impl.action.ownedpayload.provider.OwnedPayloadActions;

public class DeleteOwnedPayloadAction extends AirAction<Class<Void>> {

	private String ownedPayloadName;
	private String username;

	private NamedOwnedPayload payload;
	private OwnedPayloadActions actions;

	public DeleteOwnedPayloadAction(ActionFactory actionFactory,
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
		} catch (ServerWebApplicationException e) {
			if (e.getStatus() == 404) {
				throw new NotAllowedException();
			}
			throw new CloudFacadeException(
					"Error while deleting security policy from Air, response code"
							+ e.getStatus());
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
