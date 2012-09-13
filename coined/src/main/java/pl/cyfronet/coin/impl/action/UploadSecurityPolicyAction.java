package pl.cyfronet.coin.impl.action;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.SecurityPolicyAlreadyExistException;
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

		try {
			getAir().uploadSecurityPolicy(policyName, policyText, overwrite);
		} catch (ServerWebApplicationException e) {
			if (e.getStatus() == 400) {
				throw new SecurityPolicyAlreadyExistException();
			}
			throw new CloudFacadeException(
					"Error while deleting security policy from Air, response code"
							+ e.getStatus());
		}

		return Void.TYPE;
	}

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
