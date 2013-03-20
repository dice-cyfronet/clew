package pl.cyfronet.coin.impl.action.ownedpayload;

import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.ownedpayload.provider.OwnedPayloadActions;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class ListOwnedPayloadAction extends OwnedPayloadAction<List<String>> {

	private OwnedPayloadActions actions;

	public ListOwnedPayloadAction(OwnedPayloadActionFactory actionFactory,
			OwnedPayloadActions actions) {
		super(actionFactory);
		this.actions = actions;
	}

	@Override
	public List<String> execute() throws CloudFacadeException {
		List<NamedOwnedPayload> policies = actions.getOwnedPayloads(null, null);
		List<String> policyNames = new ArrayList<String>();
		for (NamedOwnedPayload policy : policies) {
			policyNames.add(policy.getName());
		}
		return policyNames;
	}
}
