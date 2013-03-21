package pl.cyfronet.coin.impl.mock.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import pl.cyfronet.coin.api.beans.AtomicServiceRequest;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.ApplianceTypeRequest;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class UpdateAtomicServiceMatcher extends
		BaseMatcher<ApplianceTypeRequest> {

	private AtomicServiceRequest updatedAs;
	private ApplianceType original;

	public UpdateAtomicServiceMatcher(ApplianceType original,
			AtomicServiceRequest updatedAs) {
		this.original = original;
		this.updatedAs = updatedAs;
	}

	@Override
	public boolean matches(Object arg0) {
		ApplianceTypeRequest request = (ApplianceTypeRequest) arg0;
		return updatedAs.getName().equals(request.getName())
				&& updatedAs.getDescription().equals(request.getDescription())
				&& updatedAs.getProxyConfigurationName().equals(
						request.getProxy_conf_name())
				&& flagEquals(original.isPublished(), updatedAs.getPublished(),
						request.getPublished())
				&& flagEquals(original.isScalable(), updatedAs.getScalable(),
						request.getScalable())
				&& flagEquals(original.isShared(), updatedAs.getShared(),
						request.getShared());
	}

	private boolean flagEquals(Boolean original, Boolean update, Boolean request) {
		if (update == null) {
			return original == request;
		} else {
			return update == request;
		}
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(updatedAs.toString() + "\n"
				+ original.toString());
	}

}
