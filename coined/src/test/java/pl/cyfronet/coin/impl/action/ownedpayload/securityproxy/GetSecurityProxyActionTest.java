package pl.cyfronet.coin.impl.action.ownedpayload.securityproxy;

import pl.cyfronet.coin.impl.action.ownedpayload.GetOwnedPayloadActionTest;
import pl.cyfronet.coin.impl.action.ownedpayload.MethodProvider;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetSecurityProxyActionTest extends GetOwnedPayloadActionTest {

	@Override
	protected MethodProvider getMethodProvider() {
		return new SecurityProxyMethodProvider(air, actionFactory);
	}
}
