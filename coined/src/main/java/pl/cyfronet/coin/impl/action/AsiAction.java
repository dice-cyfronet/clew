package pl.cyfronet.coin.impl.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.impl.action.workflow.WorkflowAction;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

public abstract class AsiAction<T> extends WorkflowAction<T> {

	private static final Logger logger = LoggerFactory
			.getLogger(AsiAction.class);

	private String contextId;
	private String asiId;

	public AsiAction(ActionFactory actionFactory, String username,
			String contextId, String asiId) {
		super(actionFactory, username);
		this.contextId = contextId;
		this.asiId = asiId;
	}

	protected ApplianceType getApplianceType(boolean loadDescriptor) {
		logger.debug("Getting AT for {} ASI", getAsiId());
		ApplianceType at = getApplianceType(getAsiApplianceTypeId(),
				loadDescriptor);
		logger.debug("Following AT {} received for {} ASI", at, getAsiId());
		return at;
	}

	protected void ensureDevelopmentWorklow(WorkflowDetail wd) {
		if (wd.getWorkflow_type() != WorkflowType.development) {
			throw new WorkflowNotInDevelopmentModeException();
		}
	}

	protected String getAsiApplianceTypeId() {
		WorkflowDetail wd = getUserWorkflow(contextId, getUsername());
		List<Vms> vms = wd.getVms();
		if (vms != null) {
			for (Vms vm : vms) {
				if (asiId.equals(vm.getVms_id())
						|| asiId.equals(vm.getConfiguration())) {
					return vm.getAppliance_type();
				}
			}
		}
		throw new AtomicServiceInstanceNotFoundException();
	}

	protected String getAsiApplianceTypeId(WorkflowDetail wd) {
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
}