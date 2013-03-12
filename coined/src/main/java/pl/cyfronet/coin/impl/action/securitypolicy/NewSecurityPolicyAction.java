package pl.cyfronet.coin.impl.action.securitypolicy;

import java.util.Arrays;
import java.util.List;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.AlreadyExistsException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.AirAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

public class NewSecurityPolicyAction extends AirAction<Class<Void>> {

	NamedOwnedPayload newPolicy;
	private String username;

	public NewSecurityPolicyAction(AirClient air, String username,
			NamedOwnedPayload newPolicy) {
		super(air);
		this.username = username;
		this.newPolicy = newPolicy;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		addDefaultOwnerIfOwnerListEmpty();
		try {
			getAir().addSecurityPolicy(newPolicy.getName(),
					newPolicy.getPayload(), newPolicy.getOwners());
		} catch (ServerWebApplicationException e) {
			if (e.getStatus() == 400) {
				throw new AlreadyExistsException(e.getMessage());
			} else {
				throw new CloudFacadeException(e.getMessage());
			}
		}

		return Void.TYPE;
	}

	private void addDefaultOwnerIfOwnerListEmpty() {
		List<String> owners = newPolicy.getOwners();
		if (owners == null || owners.size() == 0) {
			newPolicy.setOwners(Arrays.asList(username));
		}
	}

	@Override
	public void rollback() {
		try {
			DeleteSecurityPolicyAction action = new DeleteSecurityPolicyAction(
					getAir(), username, newPolicy.getName());
			action.execute();
		} catch (Exception e) {
			// Best effort.
		}
	}
}