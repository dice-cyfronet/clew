package pl.cyfronet.coin.clew.client.controller.overlay;

import java.util.Map;

import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.computesite.ComputeSite;
import pl.cyfronet.coin.clew.client.controller.cf.user.User;

public class OwnedApplianceType {
	private ApplianceType applianceType;
	
	private User user;
	
	private Map<String, ComputeSite> computeSites;
	
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

	public Map<String, ComputeSite> getComputeSites() {
		return computeSites;
	}

	public void setComputeSites(Map<String, ComputeSite> computeSites) {
		this.computeSites = computeSites;
	}
}