package pl.cyfronet.coin.impl.action.ownedpayload;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.AlreadyExistsException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ownedpayload.provider.OwnedPayloadActions;

public class NewOwnedPayloadAction extends OwnedPayloadAction<Class<Void>> {

	NamedOwnedPayload newPolicy;
	private String username;
	private OwnedPayloadActions actions;

	public NewOwnedPayloadAction(OwnedPayloadActionFactory actionFactory,
			String username, NamedOwnedPayload ownedPayload,
			OwnedPayloadActions actions) {
		super(actionFactory);
		this.username = username;
		this.newPolicy = ownedPayload;
		this.actions = actions;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		addDefaultOwnerIfOwnerListEmpty();
		try {
			actions.addOwnedPayload(newPolicy.getName(),
					newPolicy.getPayload(), newPolicy.getOwners());
		} catch (WebApplicationException e) {
			if (e.getResponse().getStatus() == 400) {
				throw new AlreadyExistsException(e.getMessage());
			} else {
				throw new CloudFacadeException(e.getMessage());
			}
		}

		return Void.TYPE;
	}

	private void addDefaultOwnerIfOwnerListEmpty() {
		List<String> owners = newPolicy.getOwners();
		if (owners == null || owners.size() == 0) {
			newPolicy.setOwners(Arrays.asList(username));
		}
	}

	@Override
	public void rollback() {
		try {
			Action<Class<Void>> action = new DeleteOwnedPayloadAction(
					getActionFactory(), username, newPolicy.getName(), actions);
			action.execute();
		} catch (Exception e) {
			// Best effort.
		}
	}
}
