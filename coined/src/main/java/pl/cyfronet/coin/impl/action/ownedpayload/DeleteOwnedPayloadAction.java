package pl.cyfronet.coin.impl.action.ownedpayload;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ownedpayload.provider.OwnedPayloadActions;

public class DeleteOwnedPayloadAction extends OwnedPayloadAction<Class<Void>> {

	private static final Logger logger = LoggerFactory
			.getLogger(DeleteOwnedPayloadAction.class);

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
				logger.warn("User {} is not allows to delete this {} payload",
						username, ownedPayloadName);
				throw new NotAllowedException();
			}
			logger.error("Error while deleting owned payload from AIR", e);
			throw new CloudFacadeException(
					"Error while deleting owned payload from Air, response code"
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
