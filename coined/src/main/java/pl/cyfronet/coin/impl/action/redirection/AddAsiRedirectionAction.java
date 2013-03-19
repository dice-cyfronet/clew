package pl.cyfronet.coin.impl.action.redirection;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.portmapping.AddPortMappingAction;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.dyrealla.api.DyReAllaException;
import pl.cyfronet.dyrealla.api.VirtualMachineNotFoundException;
import pl.cyfronet.dyrealla.api.dnat.DyReAllaDNATManagerService;
import pl.cyfronet.dyrealla.api.dnat.Protocol;
import pl.cyfronet.dyrealla.api.proxy.DyReAllaProxyManagerService;

public class AddAsiRedirectionAction extends AsiRedirectionAction<String> {

	private static final Logger logger = LoggerFactory
			.getLogger(AddAsiRedirectionAction.class);

	private String serviceName;
	private int port;
	private boolean http;
	private WorkflowDetail wd;

	public AddAsiRedirectionAction(ActionFactory actionFactory,
			DyReAllaProxyManagerService httpRedirectionService,
			DyReAllaDNATManagerService dnatRedirectionService, String username,
			String contextId, String asiId) {
		super(actionFactory, httpRedirectionService, dnatRedirectionService,
				username, contextId, asiId);
	}

	public void setRedirectionDetails(String serviceName, int port, boolean http) {
		this.serviceName = serviceName;
		this.port = port;
		this.http = http;
	}

	@Override
	public String execute() throws CloudFacadeException {
		wd = getUserWorkflow(getContextId(), getUsername());

		checkIfWorkflowInDevelopmentMode();

		String atId = getAsiApplianceType();
		logger.debug("Adding port mapping into {} AT: {} port {} http {}",
				new Object[] { atId, serviceName, port, http });
		String redirectionId = new AddPortMappingAction(getActionFactory(),
				atId, serviceName, port, http).execute();

		logger.debug("Added redirection id {}", redirectionId);

		if (http) {
			addHttpRedirection();
		} else {
			addDnatRedirection();
		}

		return redirectionId;
	}

	private String getAsiApplianceType() {
		List<Vms> vms = wd.getVms();
		if (vms != null) {
			for (Vms asi : vms) {
				if (asi.getVms_id().equals(getAsiId())) {
					return asi.getAppliance_type();
				}
			}
		}
		throw new AtomicServiceInstanceNotFoundException();
	}

	private void checkIfWorkflowInDevelopmentMode() {
		if (wd.getWorkflow_type() != WorkflowType.development) {
			throw new WorkflowNotInDevelopmentModeException();
		}
	}

	private void addHttpRedirection() {
		try {
			logger.debug("Adding http redirection using DyReAlla");
			getHttpRedirectionService().registerHttpService(getContextId(),
					getAsiId(), port, serviceName);
		} catch (VirtualMachineNotFoundException e) {
			logger.debug("Error while adding http redirection", e);
			throw new AtomicServiceInstanceNotFoundException();
		} catch (DyReAllaException e) {
			logger.debug("Error while adding http redirection", e);
			throw new CloudFacadeException(e.getMessage());
		}
	}

	private void addDnatRedirection() {
		try {
			logger.debug("Adding dnat redirection using DyReAlla");
			getDnatRedirectionService().addPortRedirection(getAsiId(), port,
					Protocol.TCP, serviceName);
		} catch (VirtualMachineNotFoundException e) {
			logger.debug("Error while adding dnat redirection", e);
			throw new AtomicServiceInstanceNotFoundException();
		} catch (DyReAllaException e) {
			logger.debug("Error while adding dnat redirection", e);
			throw new CloudFacadeException(e.getMessage());
		}
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}
}
