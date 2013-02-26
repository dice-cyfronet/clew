package pl.cyfronet.coin.impl.action;

import java.util.List;

import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 * @param <T>
 */
public abstract class ReadOnlyAsiAction<T> extends WorkflowAction<T> {

	private String contextId;
	private String asiId;

	public ReadOnlyAsiAction(AirClient air, String username, String contextId,
			String asiId) {
		super(air, null, username);
		this.contextId = contextId;
		this.asiId = asiId;
	}

	protected ApplianceType getApplianceType() {
		logger.debug("Getting AT for {} ASI", getAsiId());
		ApplianceType at = getApplianceType(getAsiApplianceTypeId());
		logger.debug("Following AT {} received for {} ASI", at, getAsiId());
		return at;
	}

	private String getAsiApplianceTypeId() {
		WorkflowDetail wd = getUserWorkflow(contextId, getUsername());
		List<Vms> vms = wd.getVms();
		if (vms != null) {
			for (Vms vm : vms) {
				if (vm.getVms_id().equals(asiId)) {
					return vm.getAppliance_type();
				}
			}
		}
		throw new AtomicServiceInstanceNotFoundException();
	}

	protected String getContextId() {
		return contextId;
	}

	protected String getAsiId() {
		return asiId;
	}

	@Override
	public void rollback() {
		// Readonly action - no rollback needed.
	}
}
