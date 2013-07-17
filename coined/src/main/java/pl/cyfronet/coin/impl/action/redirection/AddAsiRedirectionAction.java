package pl.cyfronet.coin.impl.action.redirection;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.RedirectionType;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.dyrealla.api.DyReAllaException;
import pl.cyfronet.dyrealla.api.VirtualMachineNotFoundException;
import pl.cyfronet.dyrealla.api.dnat.DyReAllaDNATManagerService;
import pl.cyfronet.dyrealla.api.dnat.Protocol;
import pl.cyfronet.dyrealla.api.proxy.DyReAllaProxyManagerService;
import pl.cyfronet.dyrealla.api.proxy.HttpProtocol;

public class AddAsiRedirectionAction extends AsiRedirectionAction<String> {

	private static final Logger logger = LoggerFactory
			.getLogger(AddAsiRedirectionAction.class);

	private String serviceName;
	private int port;
	private WorkflowDetail wd;
	private RedirectionType type;

	public AddAsiRedirectionAction(ActionFactory actionFactory,
			DyReAllaProxyManagerService httpRedirectionService,
			DyReAllaDNATManagerService dnatRedirectionService, String username,
			String contextId, String asiId, String serviceName, int port,
			RedirectionType type) {
		super(actionFactory, httpRedirectionService, dnatRedirectionService,
				username, contextId, asiId);
		this.serviceName = serviceName;
		this.port = port;
		this.type = type;
	}

	@Override
	public String execute() throws CloudFacadeException {
		wd = getUserWorkflow(getContextId(), getUsername());

		checkIfWorkflowInDevelopmentMode();

		String atId = getAsiApplianceType();
		logger.debug("Adding port mapping into {} AT: {} port {} type {}",
				new Object[] { atId, serviceName, port, type });

		String redirectionId = getActionFactory().createAddPortMappingAction(
				atId, serviceName, port, isHttp(), isHttps()).execute();

		logger.debug("Added redirection id {}", redirectionId);

		if (isHttp() || isHttps()) {
			addHttpRedirection();
		} else {
			addDnatRedirection();
		}

		return redirectionId;
	}

	private boolean isHttp() {
		return type == RedirectionType.HTTP
				|| type == RedirectionType.HTTP_AND_HTTPS;
	}

	private boolean isHttps() {
		return type == RedirectionType.HTTPS
				|| type == RedirectionType.HTTP_AND_HTTPS;
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
		logger.warn("ASI {} not found in workflow {}", getAsiId(),
				getContextId());
		throw new AtomicServiceInstanceNotFoundException();
	}

	private void checkIfWorkflowInDevelopmentMode() {
		if (wd.getWorkflow_type() != WorkflowType.development) {
			logger.warn(
					"Trying to add redirection for workflow {} started in production mode",
					getContextId());
			throw new WorkflowNotInDevelopmentModeException();
		}
	}

	private void addHttpRedirection() {
		try {
			logger.debug("Adding http redirection using DyReAlla");
			if (isHttp()) {
				getHttpRedirectionService().registerHttpService(getContextId(),
						getAsiId(), port, serviceName, HttpProtocol.HTTP);
			}
			if (isHttps()) {
				getHttpRedirectionService().registerHttpService(getContextId(),
						getAsiId(), port, serviceName, HttpProtocol.HTTPS);
			}
		} catch (VirtualMachineNotFoundException e) {
			logger.error("VM for ASI not found", e);
			throw new AtomicServiceInstanceNotFoundException();
		} catch (DyReAllaException e) {
			logger.error("Error while adding http redirection", e);
			throw new CloudFacadeException(e.getMessage());
		}
	}

	private void addDnatRedirection() {
		try {
			logger.debug("Adding dnat redirection using DyReAlla");
			getDnatRedirectionService().addPortRedirection(getAsiId(), port,
					Protocol.TCP, serviceName);
		} catch (VirtualMachineNotFoundException e) {
			logger.error("VM for ASI not found", e);
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
