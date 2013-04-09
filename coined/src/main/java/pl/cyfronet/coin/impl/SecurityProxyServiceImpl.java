package pl.cyfronet.coin.impl;

import java.util.List;

import javax.ws.rs.core.Response;

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
		Action<List<String>> action = actionFactory.getProxiesActionFactory()
				.createListAction();
		return action.execute();
	}

	@Public
	@Override
	public NamedOwnedPayload get(String name) throws NotFoundException {
		Action<NamedOwnedPayload> action = actionFactory
				.getProxiesActionFactory().createGetAction(name);
		return action.execute();
	}

	@Public
	@Override
	public String getPayload(String name) {
		Action<String> action = actionFactory.getProxiesActionFactory()
				.createGetPayloadAction(name);
		return action.execute();
	}

	@Override
	public Response create(NamedOwnedPayload ownedPayload)
			throws AlreadyExistsException {
		Action<Class<Void>> action = actionFactory.getProxiesActionFactory()
				.createNewAction(getUsername(), ownedPayload);
		action.execute();

		return Response.status(Response.Status.CREATED).build();
	}

	@Override
	public Response update(String name, OwnedPayload ownedPayload)
			throws AlreadyExistsException {
		Action<Class<Void>> action = actionFactory.getProxiesActionFactory()
				.createUpdateAction(getUsername(), name, ownedPayload);
		action.execute();

		return Response.ok().build();
	}

	@Override
	public Response delete(String name) {
		Action<Class<Void>> action = actionFactory.getProxiesActionFactory()
				.createDeleteAction(getUsername(), name);
		action.execute();

		return Response.ok().build();
	}

	public void setActionFactory(ActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}
}
