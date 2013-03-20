package pl.cyfronet.coin.impl.action.ownedpayload;

import pl.cyfronet.coin.impl.action.Action;

public abstract class OwnedPayloadAction<T> implements Action<T>{

	private OwnedPayloadActionFactory actionFactory;

	public OwnedPayloadAction(OwnedPayloadActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}
	
	protected OwnedPayloadActionFactory getActionFactory() {
		return actionFactory;
	}

	@Override
	public void rollback() {

	}
}
