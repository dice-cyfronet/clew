package pl.cyfronet.coin.impl.action.as;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.AtomicServiceRequest;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.AirAction;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.ApplianceTypeRequest;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class UpdateAtomicServiceAction extends AirAction<Class<Void>> {

	private static final Logger logger = LoggerFactory
			.getLogger(UpdateAtomicServiceAction.class);

	private String username;
	private String asId;
	private AtomicServiceRequest updatedAs;
	private boolean admin;
	private ApplianceType oldAS;

	public UpdateAtomicServiceAction(ActionFactory actionFactory,
			String username, String asId, AtomicServiceRequest updatedAs,
			boolean admin) {
		super(actionFactory);
		this.username = username;
		this.asId = asId;
		this.updatedAs = updatedAs;
		this.admin = admin;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		oldAS = getApplianceType(asId, false);

		if (canUpdate()) {
			ApplianceTypeRequest updatedAsRequest = new ApplianceTypeRequest();
			updatedAsRequest.setName(updatedAs.getName());
			updatedAsRequest.setDescription(updatedAs.getDescription());
			updatedAsRequest.setProxy_conf_name(updatedAs
					.getProxyConfigurationName());
			updatedAsRequest.setPublished(getFlag(oldAS.isPublished(),
					updatedAs.getPublished()));
			updatedAsRequest.setScalable(getFlag(oldAS.isScalable(),
					updatedAs.getScalable()));
			updatedAsRequest.setShared(getFlag(oldAS.isShared(),
					updatedAs.getShared()));
			try {
				getAir().updateAtomicService(asId, updatedAsRequest,
						updatedAs.getCpu(), updatedAs.getDisk(),
						updatedAs.getMemory());
			} catch (WebApplicationException e) {
				if (e.getResponse().getStatus() == 400) {
					logger.warn(
							"User {} is not allowes to update {} AS because of {}",
							new Object[] { username, asId, e.getMessage() });
					throw new NotAllowedException(e.getMessage());
				}
				logger.error("Unknown error thrown from AIR while removing AS",
						e);
				throw new CloudFacadeException(e.getMessage());
			}
		} else {
			logger.warn(
					"User {} tries to remove Atomic Service {} without admin or owner rights",
					username, asId);
			throw new NotAllowedException(String.format(
					"%s is not allowed to modify %s", username, asId));
		}
		return Void.TYPE;
	}

	private boolean canUpdate() {
		return admin || username.equals(oldAS.getAuthor());
	}

	private Boolean getFlag(boolean original, Boolean updated) {
		return updated == null ? original : updated;
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
	}

}
