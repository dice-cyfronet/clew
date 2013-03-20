package pl.cyfronet.coin.impl.action.ownedpayload;

import java.util.List;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.beans.OwnedPayload;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ownedpayload.provider.OwnedPayloadActions;

public class OwnedPayloadActionFactory {

	private OwnedPayloadActions actions;

	public OwnedPayloadActionFactory(OwnedPayloadActions actions) {
		this.actions = actions;
	}

	public Action<List<String>> createListAction() {
		return new ListOwnedPayloadAction(this, actions);
	}

	public Action<NamedOwnedPayload> createGetAction(String name) {
		return new GetOwnedPayloadAction(this, name, actions);
	}

	public Action<String> createGetPayloadAction(String name) {
		return new GetOwnedPayloadPayloadAction(this, name, actions);
	}

	public Action<Class<Void>> createNewAction(String username,
			NamedOwnedPayload securityPolicy) {
		return new NewOwnedPayloadAction(this, username, securityPolicy,
				actions);
	}

	public Action<Class<Void>> createUpdateAction(String username, String name,
			OwnedPayload ownedPayload) {
		return new UpdateOwnedPayloadAction(this, username, name, ownedPayload,
				actions);
	}

	public Action<Class<Void>> createDeleteAction(String username, String name) {
		return new DeleteOwnedPayloadAction(this, username, name, actions);
	}
}
