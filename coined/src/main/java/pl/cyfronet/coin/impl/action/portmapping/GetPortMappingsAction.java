package pl.cyfronet.coin.impl.action.portmapping;

import java.util.List;

import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.WorkflowAction;
import pl.cyfronet.coin.impl.air.client.ATPortMapping;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

public class GetPortMappingsAction extends WorkflowAction<List<ATPortMapping>> {

	private String contextId;
	private String asiId;

	public GetPortMappingsAction(AirClient air, String username,
			String contextId, String asiId) {
		super(air, null, username);
		this.contextId = contextId;
		this.asiId = asiId;
	}

	@Override
	public List<ATPortMapping> execute() throws CloudFacadeException {
		return getApplianceType(getAsiApplianceType()).getPort_mappings();
	}

	private String getAsiApplianceType() {
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

	@Override
	public void rollback() {
		// Read only action, no rollback needed.
	}
}
