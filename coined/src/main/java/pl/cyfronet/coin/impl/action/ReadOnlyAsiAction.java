package pl.cyfronet.coin.impl.action;


/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 * @param <T>
 */
public abstract class ReadOnlyAsiAction<T> extends AsiAction<T> {

	public ReadOnlyAsiAction(ActionFactory actionFactory, String username,
			String contextId, String asiId) {
		super(actionFactory, username, contextId, asiId);
	}

	@Override
	public void rollback() {
		// Readonly action - no rollback needed.
	}
}
