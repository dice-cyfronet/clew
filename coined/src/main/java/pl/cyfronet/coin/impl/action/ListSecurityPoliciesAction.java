package pl.cyfronet.coin.impl.action;

import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.SecurityPolicy;

public class ListSecurityPoliciesAction extends AirAction<List<String>> {

	ListSecurityPoliciesAction(AirClient air) {
		super(air);
	}

	@Override
	public List<String> execute() throws CloudFacadeException {
		List<SecurityPolicy> policies = getAir().getSecurityPolicies();
		List<String> policyNames = new ArrayList<String>();
		for (SecurityPolicy policy : policies) {
			policyNames.add(policy.getPolicy_name());
		}
		return policyNames;
	}

	@Override
	public void rollback() {
		// readonly action - no rollback needed.
	}

}
