package pl.cyfronet.coin.impl.action.ownedpayload;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.AlreadyExistsException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ownedpayload.provider.OwnedPayloadActions;

public class NewOwnedPayloadAction extends OwnedPayloadAction<Class<Void>> {

	private static final Logger logger = LoggerFactory
			.getLogger(NewOwnedPayloadAction.class);

	private NamedOwnedPayload newPayload;
	private String username;
	private OwnedPayloadActions actions;

	public NewOwnedPayloadAction(OwnedPayloadActionFactory actionFactory,
			String username, NamedOwnedPayload ownedPayload,
			OwnedPayloadActions actions) {
		super(actionFactory);
		this.username = username;
		this.newPayload = ownedPayload;
		this.actions = actions;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		addDefaultOwnerIfOwnerListEmpty();
		try {
			actions.addOwnedPayload(newPayload.getName(),
					newPayload.getPayload(), newPayload.getOwners());
		} catch (WebApplicationException e) {
			if (e.getResponse().getStatus() == 400) {
				logger.warn("Payload with {} name already exist in AIR",
						newPayload.getName());
				throw new AlreadyExistsException(e.getResponse().getEntity()
						+ "");
			} else {
				logger.error("Error while contacting AIR", e);
				throw new CloudFacadeException(e.getResponse().getEntity() + "");
			}
		}

		return Void.TYPE;
	}

	private void addDefaultOwnerIfOwnerListEmpty() {
		List<String> owners = newPayload.getOwners();
		if (owners == null || owners.size() == 0) {
			newPayload.setOwners(Arrays.asList(username));
		}
	}

	@Override
	public void rollback() {
		try {
			Action<Class<Void>> action = new DeleteOwnedPayloadAction(
					getActionFactory(), username, newPayload.getName(), actions);
			action.execute();
		} catch (Exception e) {
			// Best effort.
		}
	}
}
