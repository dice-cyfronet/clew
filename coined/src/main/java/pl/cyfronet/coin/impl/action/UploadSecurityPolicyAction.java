package pl.cyfronet.coin.impl.action;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.SecurityPolicyNotFoundException;
import pl.cyfronet.coin.impl.air.client.AirClient;

public class UploadSecurityPolicyAction extends AirAction<Class<Void>> {

	private String policyName;
	private boolean overwrite;
	private String policyText;

	private String oldPolicyPayload = null;

	UploadSecurityPolicyAction(AirClient air, String policyName,
			String policyText, boolean overwrite) {
		super(air);
		this.policyName = policyName;
		this.overwrite = overwrite;
		this.policyText = policyText;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		if (overwrite) {
			loadOldPolicyPayload();
		}

		getAir().uploadSecurityPolicy(policyName, policyText, overwrite);

		return Void.TYPE;
	}

	/**
	 * 
	 */
	private void loadOldPolicyPayload() {
		GetSecurityPolicyAction getPolicyAction = new GetSecurityPolicyAction(
				getAir(), policyName);
		try {
			oldPolicyPayload = getPolicyAction.execute();
		} catch (SecurityPolicyNotFoundException e) {
			// ok policy does not exist
		}
	}

	@Override
	public void rollback() {
		try {
			if (oldPolicyPayload != null) {
				UploadSecurityPolicyAction action = new UploadSecurityPolicyAction(
						getAir(), policyName, oldPolicyPayload, true);
				action.execute();
			} else {
				DeleteSecurityPolicyAction action = new DeleteSecurityPolicyAction(
						getAir(), policyName);
				action.execute();
			}
		} catch (Exception e) {
			// best effort.
		}
	}

}
