package pl.cyfronet.coin.impl.action.as;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

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
		oldAS = getApplianceType(asId);

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
				getAir().updateAtomicService(asId, updatedAsRequest);
			} catch (ServerWebApplicationException e) {
				if (e.getStatus() == 400) {
					throw new NotAllowedException(e.getMessage());
				}
				throw new CloudFacadeException(e.getMessage());
			}
		} else {
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
