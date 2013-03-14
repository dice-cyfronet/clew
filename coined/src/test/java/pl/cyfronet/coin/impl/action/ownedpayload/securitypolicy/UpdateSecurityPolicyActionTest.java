package pl.cyfronet.coin.impl.action.ownedpayload.securitypolicy;

import pl.cyfronet.coin.impl.action.ownedpayload.MethodProvider;
import pl.cyfronet.coin.impl.action.ownedpayload.UpdateOwnedPayloadActionTest;

public class UpdateSecurityPolicyActionTest extends
		UpdateOwnedPayloadActionTest {

	@Override
	protected MethodProvider getMethodProvider() {
		return new SecurityPolicyMethodProvider(air, actionFactory);
	}
}
