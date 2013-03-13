package pl.cyfronet.coin.impl.action.ownedpayload.securityproxy;

import pl.cyfronet.coin.impl.action.ownedpayload.MethodProvider;
import pl.cyfronet.coin.impl.action.ownedpayload.UpdateOwnedPayloadActionTest;

public class UpdateSecurityProxyActionTest extends
		UpdateOwnedPayloadActionTest {

	@Override
	protected MethodProvider getMethodProvider() {
		return new SecurityProxyMethodProvider(air, actionFactory);
	}
}
