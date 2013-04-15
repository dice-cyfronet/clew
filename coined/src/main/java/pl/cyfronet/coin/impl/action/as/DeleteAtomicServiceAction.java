package pl.cyfronet.coin.impl.action.as;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.NotAcceptableException;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.BaseAction;
import pl.cyfronet.dyrealla.api.DyReAllaException;
import pl.cyfronet.dyrealla.api.RemoveTemplatesResponse;
import pl.cyfronet.dyrealla.api.allocation.OperationStatus;

public class DeleteAtomicServiceAction extends BaseAction<Class<Void>> {

	private static final Logger logger = LoggerFactory
			.getLogger(DeleteAtomicServiceAction.class);

	ActionFactory actionFactory;
	private String asId;
	private String username;
	private boolean admin;

	public DeleteAtomicServiceAction(ActionFactory actionFactory,
			String username, String asId, boolean admin) {
		super(actionFactory);
		this.actionFactory = actionFactory;
		this.username = username;
		this.asId = asId;
		this.admin = admin;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		checkIfCanRemoveAS();
		try {
			RemoveTemplatesResponse response = getAtmosphere()
					.removeTemplatesOfApplianceType(asId);
			if (response.getOperationStatus() == OperationStatus.SUCCESSFUL) {
				actionFactory.createDeleteAtomicServiceFromAirAction(asId)
						.execute();
			} else {
				String errorMsg = getErrorsMsg(response);
				logger.error("Unable to remove template: {}", errorMsg);
				throw new NotAcceptableException(errorMsg);
			}
		} catch (DyReAllaException e) {
			logger.error("Internal dyrealla exception thrown", e);
			throw new CloudFacadeException(
					"Unable to remove template from Atmosphere");
		}

		return Void.TYPE;
	}

	private void checkIfCanRemoveAS() {
		if (!admin) {
			AtomicService as = actionFactory.createGetAtomicServiceAction(asId)
					.execute();
			if (!username.equals(as.getOwner())) {
				logger.warn("User {} is trying to remove not owned Atomic Service {}", username, asId);
				throw new NotAllowedException(
						"You are not allowed to delete this Atomic Service");
			}
		}
	}

	private String getErrorsMsg(RemoveTemplatesResponse response) {
		StringBuilder sb = new StringBuilder();
		Map<String, String> errors = response.getErrors();
		if (errors != null && errors.size() > 0) {
			for (String error : errors.values()) {
				sb.append(",").append(error);
			}
			return sb.substring(1);
		}
		return null;
	}

	@Override
	public void rollback() {
		// Not supported. Removing template cannot be undone.
	}

}
