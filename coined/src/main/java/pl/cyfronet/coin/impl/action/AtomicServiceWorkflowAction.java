package pl.cyfronet.coin.impl.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.dyrealla.api.DyReAllaManagerService;
import pl.cyfronet.dyrealla.api.allocation.ManagerResponse;
import pl.cyfronet.dyrealla.api.allocation.impl.AddRequiredAppliancesRequestImpl;
import pl.cyfronet.dyrealla.api.allocation.impl.ApplianceIdentityImpl;

public abstract class AtomicServiceWorkflowAction<T> extends WorkflowAction<T> {

	AtomicServiceWorkflowAction(AirClient air,
			DyReAllaManagerService atmosphere, String username) {
		super(air, atmosphere, username);
	}

	/**
	 * Register appliance types (with specific configuration id) for workflow.
	 * @param contextId Context id (e.k.a. workflow id).
	 * @param configIds List of appliance types configurations ids.
	 * @param priority Workflow priority.
	 */
	protected void registerVms(String contextId, List<String> configIds,
			List<String> names, Integer priority, WorkflowType workflowType)
			throws CloudFacadeException {
		if (configIds != null && configIds.size() > 0) {
			String[] ids = configIds.toArray(new String[0]);
			logger.debug(
					"Registering required atomic services in atmosphere {}",
					Arrays.toString(ids));

			AddRequiredAppliancesRequestImpl request = new AddRequiredAppliancesRequestImpl();
			request.setImportanceLevel(priority);
			request.setCorrelationId(contextId);
			request.setApplianceIdentities(getApplianceIdentities(configIds,
					names));
			// FIXME
			// request.setRunMode()

			ManagerResponse response = getAtmosphere()
					.addRequiredAppliances(request);
			parseResponseAndThrowExceptionsWhenNeeded(response);
		}
	}
	
	private List<ApplianceIdentityImpl> getApplianceIdentities(
			List<String> configIds, List<String> names) {
		List<ApplianceIdentityImpl> identities = new ArrayList<ApplianceIdentityImpl>();
		for (int i = 0; i < configIds.size(); i++) {
			String asId = configIds.get(i);
			ApplianceIdentityImpl identity = new ApplianceIdentityImpl();
			identity.setInitConfId(asId);
			String name = null;
			if (names != null && names.size() > i) {
				name = names.get(i);
			}
			identity.setName(name);
			identities.add(identity);
		}
		return identities;
	}
}
