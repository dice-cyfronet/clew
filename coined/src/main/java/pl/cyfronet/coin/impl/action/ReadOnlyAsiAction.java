package pl.cyfronet.coin.impl.action;

import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 * @param <T>
 */
public abstract class ReadOnlyAsiAction<T> extends AsiAction<T> {

	public ReadOnlyAsiAction(AirClient air, String username, String contextId,
			String asiId) {
		super(air, username, contextId, asiId);
	}

	@Override
	public void rollback() {
		// Readonly action - no rollback needed.
	}
}
