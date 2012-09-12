package pl.cyfronet.coin.impl;

import java.util.List;

import pl.cyfronet.coin.api.SecurityPolicyService;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.DeleteSecurityPolicyAction;
import pl.cyfronet.coin.impl.action.GetSecurityPolicyAction;
import pl.cyfronet.coin.impl.action.ListSecurityPoliciesAction;
import pl.cyfronet.coin.impl.action.UploadSecurityPolicyAction;

public class SecurityPolicyServiceImpl implements SecurityPolicyService {

	private ActionFactory actionFactory;

	@Override
	public String getSecurityPolicy(String policyName) {
		GetSecurityPolicyAction action = actionFactory
				.createGetSecurityPolicyAction(policyName);
		return action.execute();
	}

	@Override
	public List<String> getPoliciesNames() {
		ListSecurityPoliciesAction action = actionFactory
				.createListSecurityPoliciesAction();
		return action.execute();
	}

	@Override
	public void updateSecurityPolicy(String policyName, String policyContent,
			boolean overwrite) {
		UploadSecurityPolicyAction action = actionFactory
				.createUploadSecurityPolicyAction(policyName, policyContent,
						overwrite);
		action.execute();
	}

	@Override
	public void deleteSecurityPolicy(String policyName) {
		DeleteSecurityPolicyAction action = actionFactory
				.createDeleteSecurityPolicyAction(policyName);
		action.execute();
	}

	public void setActionFactory(ActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}
}
