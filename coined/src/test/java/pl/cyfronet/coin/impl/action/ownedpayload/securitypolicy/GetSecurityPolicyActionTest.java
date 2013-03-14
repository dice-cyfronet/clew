package pl.cyfronet.coin.impl.action.ownedpayload.securitypolicy;

import pl.cyfronet.coin.impl.action.ownedpayload.GetOwnedPayloadActionTest;
import pl.cyfronet.coin.impl.action.ownedpayload.MethodProvider;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetSecurityPolicyActionTest extends GetOwnedPayloadActionTest {

	@Override
	protected MethodProvider getMethodProvider() {
		return new SecurityPolicyMethodProvider(air, actionFactory);
	}
}
