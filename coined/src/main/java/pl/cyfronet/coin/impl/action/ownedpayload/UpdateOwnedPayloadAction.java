package pl.cyfronet.coin.impl.action.ownedpayload;

import java.util.List;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.beans.OwnedPayload;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.AirAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

public abstract class UpdateOwnedPayloadAction extends AirAction<Class<Void>> {

	private String username;
	private String ownedPayloadName;
	private OwnedPayload ownedPayload;
	private OwnedPayload oldPayload;

	public UpdateOwnedPayloadAction(AirClient air, String username,
			String ownedPayloadName, OwnedPayload ownedPayload) {
		super(air);
		this.ownedPayloadName = ownedPayloadName;
		this.ownedPayload = ownedPayload;
		this.username = username;
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
		updateOwnedPayload(username, ownedPayloadName, owners, payloadToUpdate);
	}

	protected abstract void updateOwnedPayload(String username,
			String ownedPayloadName, List<String> owners,
			String ownedPayloadToUpdate);

	private void loadOldOwnedPayload() {
		oldPayload = getOwnedPayloadAction(ownedPayloadName).execute();
	}

	protected abstract Action<NamedOwnedPayload> getOwnedPayloadAction(
			String ownedPolicyName);

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
