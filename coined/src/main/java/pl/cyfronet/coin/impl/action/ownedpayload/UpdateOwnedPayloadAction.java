package pl.cyfronet.coin.impl.action.ownedpayload;

import java.util.List;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.beans.OwnedPayload;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.AirAction;
import pl.cyfronet.coin.impl.action.ownedpayload.provider.OwnedPayloadActions;

public class UpdateOwnedPayloadAction extends AirAction<Class<Void>> {

	private String username;
	private String ownedPayloadName;
	private OwnedPayload ownedPayload;
	private OwnedPayload oldPayload;
	private OwnedPayloadActions actions;

	public UpdateOwnedPayloadAction(ActionFactory actionFactory,
			String username, String ownedPayloadName,
			OwnedPayload ownedPayload, OwnedPayloadActions actions) {
		super(actionFactory);
		this.ownedPayloadName = ownedPayloadName;
		this.ownedPayload = ownedPayload;
		this.username = username;
		this.actions = actions;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		loadOldOwnedPayload();
		try {
			update(ownedPayload);
		} catch (ServerWebApplicationException e) {
			if (e.getStatus() == 404) {
				throw new NotAllowedException();
			}
			throw new CloudFacadeException(
					"Error while deleting security policy from Air, response code "
							+ e.getStatus());
		}

		return Void.TYPE;
	}

	private void update(OwnedPayload payload) {
		List<String> owners = payload.getOwners() == null ? oldPayload
				.getOwners() : payload.getOwners();
		String payloadToUpdate = payload.getPayload() == null ? oldPayload
				.getPayload() : payload.getPayload();
		actions.updateOwnedPayload(username, ownedPayloadName, owners,
				payloadToUpdate);
	}

	private void loadOldOwnedPayload() {
		oldPayload = new GetOwnedPayloadAction(getActionFactory(),
				ownedPayloadName, actions).execute();
	}

	@Override
	public void rollback() {
		try {
			if (oldPayload != null) {
				update(oldPayload);
			}
		} catch (Exception e) {
			// best effort.
		}
	}
}