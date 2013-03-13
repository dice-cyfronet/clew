package pl.cyfronet.coin.impl.action.ownedpayload;

import java.util.Arrays;
import java.util.List;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.AlreadyExistsException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.AirAction;
import pl.cyfronet.coin.impl.action.securitypolicy.DeleteSecurityPolicyAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

public abstract class NewOwnedPayloadAction extends AirAction<Class<Void>> {

	NamedOwnedPayload newPolicy;
	private String username;

	public NewOwnedPayloadAction(AirClient air, String username,
			NamedOwnedPayload ownedPayload) {
		super(air);
		this.username = username;
		this.newPolicy = ownedPayload;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		addDefaultOwnerIfOwnerListEmpty();
		try {
			addOwnedPayload(newPolicy.getName(),
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

	protected abstract void addOwnedPayload(String name, String payload, List<String> owners);
	
	
	private void addDefaultOwnerIfOwnerListEmpty() {
		List<String> owners = newPolicy.getOwners();
		if (owners == null || owners.size() == 0) {
			newPolicy.setOwners(Arrays.asList(username));
		}
	}

	@Override
	public void rollback() {
		try {
			Action<Class<Void>> action = getDeleteAction(username, newPolicy.getName());
			action.execute();
		} catch (Exception e) {
			// Best effort.
		}
	}

	protected abstract DeleteSecurityPolicyAction getDeleteAction(String username, String ownedPayloadName);
}
