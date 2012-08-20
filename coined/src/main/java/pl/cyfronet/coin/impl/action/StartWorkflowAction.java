package pl.cyfronet.coin.impl.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowStartException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.dyrealla.allocation.ManagerResponse;
import pl.cyfronet.dyrealla.allocation.impl.AddRequiredAppliancesRequestImpl;
import pl.cyfronet.dyrealla.allocation.impl.ApplianceIdentityImpl;
import pl.cyfronet.dyrealla.core.DyReAllaManagerService;

/**
 * Start workflow. There can be many workflow type Workflows but only one
 * portal and development workflow.
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class StartWorkflowAction extends WorkflowAction<String> {

	private WorkflowStartRequest workflow;
	private Integer defaultPriority;

	private String contextId;

	/**
	 * @param air Air client.
	 * @param atmosphere Atmosphere client
	 * @param defaultPriority Default workflow priority.
	 * @param username Workflow owner username.
	 * @param workflow Workflow start request. It contains information about
	 *            required Atomic Services.
	 */
	StartWorkflowAction(AirClient air, DyReAllaManagerService atmosphere,
			Integer defaultPriority, String username,
			WorkflowStartRequest workflow) {
		super(air, atmosphere, username);
		this.workflow = workflow;
		this.defaultPriority = defaultPriority;
	}

	/**
	 * @return Workflow context id.
	 * @throws WorkflowStartException Thrown when workflow cannot be started.
	 *             E.g. user tries to start second development or portal
	 *             workflow.
	 */
	@Override
	public String execute() throws CloudFacadeException {
		logger.debug("starting workflow {} for {} user", workflow,
				getUsername());

		Integer priority = workflow.getPriority();
		if (priority == null) {
			priority = defaultPriority;
		}

		WorkflowType type = workflow.getType();
		if (type == WorkflowType.portal || type == WorkflowType.development) {
			try {
				List<WorkflowBaseInfo> workflows = getWorkflows();
				for (WorkflowBaseInfo wInfo : workflows) {
					if (wInfo.getType() == type) {
						throw new WorkflowStartException(String.format(
								"Cannot start two %s workflows", type));
					}
				}
			} catch (WebApplicationException e) {
				// 400 is thrown if user is not know by AIR. Most probably user
				// is starting workflow for the first time.
				if (e.getResponse().getStatus() != 400) {
					if (e instanceof WorkflowStartException) {
						throw e;
					}
					throw new WorkflowStartException(
							"Unable to register new workflow in AIR");
				}
			}
		}

		// FIXME error handling
		contextId = getAir().startWorkflow(workflow.getName(), getUsername(),
				workflow.getDescription(), priority, workflow.getType());
		List<String> ids = workflow.getAsConfigIds();

		try {
			registerVms(contextId, ids, null, priority, type);
		} catch (CloudFacadeException e) {
			rollback();
			throw new WorkflowStartException();
		}

		return contextId;
	}

	private List<WorkflowBaseInfo> getWorkflows() {
		GetUserWorkflowsAction action = new GetUserWorkflowsAction(getAir(),
				getUsername());
		return action.execute();
	}

	/**
	 * Register appliance types (with specific configuration id) for workflow.
	 * @param contextId Context id (e.k.a. workflow id).
	 * @param configIds List of appliance types configurations ids.
	 * @param priority Workflow priority.
	 */
	private void registerVms(String contextId, List<String> configIds,
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
	
	@Override
	public void rollback() {
		try {
			stopWorkflow();
		} catch (Exception e) {

		}
	}

	private void stopWorkflow() {
		new StopWorkflowAction(getAir(), getAtmosphere(), contextId,
				getUsername()).execute();
	}
}
