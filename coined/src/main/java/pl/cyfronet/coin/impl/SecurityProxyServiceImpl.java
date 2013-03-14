package pl.cyfronet.coin.impl;

import java.util.List;

import pl.cyfronet.coin.api.OwnedPayloadService;
import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.beans.OwnedPayload;
import pl.cyfronet.coin.api.exception.AlreadyExistsException;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.auth.annotation.Public;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;

public class SecurityProxyServiceImpl extends UsernameAwareService implements
		OwnedPayloadService {

	private ActionFactory actionFactory;
	
	@Public
	@Override
	public List<String> list() {
		Action<List<String>> action = actionFactory
				.createListSecurityProxiesAction();
		return action.execute();
	}

	@Public
	@Override
	public NamedOwnedPayload get(String name) throws NotFoundException {
		Action<NamedOwnedPayload> action = actionFactory
				.createGetSecurityProxyAction(name);
		return action.execute();
	}

	@Public
	@Override
	public String getPayload(String name) {
		Action<String> action = actionFactory
				.createGetSecurityProxyPayloadAction(name);
		return action.execute();
	}

	@Override
	public void create(NamedOwnedPayload ownedPayload)
			throws AlreadyExistsException {
		Action<Class<Void>> action = actionFactory
				.createNewSecurityProxyAction(getUsername(), ownedPayload);
		action.execute();
	}

	@Override
	public void update(String name, OwnedPayload ownedPayload)
			throws AlreadyExistsException {
		Action<Class<Void>> action = actionFactory
				.createUpdateSecurityProxyAction(getUsername(), name,
						ownedPayload);
		action.execute();
	}

	@Override
	public void delete(String name) {
		Action<Class<Void>> action = actionFactory
				.createDeleteSecurityProxyAction(getUsername(), name);
		action.execute();
	}

	public void setActionFactory(ActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}
}
