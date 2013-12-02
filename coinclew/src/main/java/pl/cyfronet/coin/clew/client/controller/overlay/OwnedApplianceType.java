package pl.cyfronet.coin.clew.client.controller.overlay;

import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.user.User;

public class OwnedApplianceType {
	private ApplianceType applianceType;
	private User user;
	
	public ApplianceType getApplianceType() {
		return applianceType;
	}
	public void setApplianceType(ApplianceType applianceType) {
		this.applianceType = applianceType;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}