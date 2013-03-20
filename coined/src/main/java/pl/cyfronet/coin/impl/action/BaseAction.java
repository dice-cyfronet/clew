package pl.cyfronet.coin.impl.action;

import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.dyrealla.api.DyReAllaManagerService;

public abstract class BaseAction<T> implements Action<T> {

	private ActionFactory actionFactory;

	public BaseAction(ActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}

	protected AirClient getAir() {
		return actionFactory.getAir();
	}

	protected DyReAllaManagerService getAtmosphere() {
		return actionFactory.getAtmosphere();
	}

	protected ActionFactory getActionFactory() {
		return actionFactory;
	}
}
