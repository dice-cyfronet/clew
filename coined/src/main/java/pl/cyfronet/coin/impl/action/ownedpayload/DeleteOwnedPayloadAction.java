package pl.cyfronet.coin.impl.action.ownedpayload;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.AirAction;
import pl.cyfronet.coin.impl.action.securitypolicy.GetSecurityPolicyAction;
import pl.cyfronet.coin.impl.action.securitypolicy.NewSecurityPolicyAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

public abstract class DeleteOwnedPayloadAction extends AirAction<Class<Void>> {

	private String ownedPayloadName;
	private String username;

	private NamedOwnedPayload payload;

	public DeleteOwnedPayloadAction(AirClient air, String username,
			String ownedPayloadName) {
		super(air);
		this.username = username;
		this.ownedPayloadName = ownedPayloadName;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		loadOldPolicyPayload();
		try {
			deleteOwnedPayload(username, ownedPayloadName);
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

	protected abstract void deleteOwnedPayload(String username,
			String ownedPayloadName);

	@Override
	public void rollback() {
		try {
			if (payload != null) {
				Action<Class<Void>> action = getNewOwnedPayloadAction(username,
						payload);
				action.execute();
			}
		} catch (Exception e) {
			// best effort.
		}
	}

	protected abstract NewSecurityPolicyAction getNewOwnedPayloadAction(
			String username, NamedOwnedPayload ownedPayload);

	private void loadOldPolicyPayload() throws NotFoundException {
		Action<NamedOwnedPayload> getPolicyAction = getOwnedPayloadAction(null,
				ownedPayloadName);
		payload = getPolicyAction.execute();
	}

	protected abstract GetSecurityPolicyAction getOwnedPayloadAction(
			String username, String ownedPolicyName);
}
