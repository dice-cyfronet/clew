package pl.cyfronet.coin.impl;

import pl.cyfronet.coin.api.SecurityPolicyService;

public class SecurityPolicyServiceImpl implements SecurityPolicyService {

	@Override
	public String getSecurityPolicy(String policyName) {
		return String.format("role=%s", policyName);
	}

}
