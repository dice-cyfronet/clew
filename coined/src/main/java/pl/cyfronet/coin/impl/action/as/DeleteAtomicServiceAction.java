package pl.cyfronet.coin.impl.action.as;

import java.util.Map;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.NotAcceptableException;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.dyrealla.api.DyReAllaException;
import pl.cyfronet.dyrealla.api.DyReAllaManagerService;
import pl.cyfronet.dyrealla.api.RemoveTemplatesResponse;
import pl.cyfronet.dyrealla.api.allocation.OperationStatus;

public class DeleteAtomicServiceAction implements Action<Class<Void>> {

	ActionFactory actionFactory;
	private DyReAllaManagerService atmosphere;
	private String asId;
	private String username;
	private boolean admin;

	public DeleteAtomicServiceAction(ActionFactory actionFactory,
			DyReAllaManagerService atmosphere, String username, String asId,
			boolean admin) {
		this.actionFactory = actionFactory;
		this.atmosphere = atmosphere;
		this.username = username;
		this.asId = asId;
		this.admin = admin;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		checkIfCanRemoveAS();
		try {
			RemoveTemplatesResponse response = atmosphere
					.removeTemplatesOfApplianceType(asId);
			if (response.getOperationStatus() == OperationStatus.SUCCESSFUL) {
				actionFactory.createDeleteAtomicServiceFromAirAction(asId)
						.execute();
			} else {
				throw new NotAcceptableException(getErrorsMsg(response));
			}
		} catch (DyReAllaException e) {
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
