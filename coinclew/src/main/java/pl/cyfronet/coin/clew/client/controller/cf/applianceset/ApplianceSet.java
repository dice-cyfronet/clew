package pl.cyfronet.coin.clew.client.controller.cf.applianceset;

public class ApplianceSet extends NewApplianceSet {
	private String id;

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ApplianceSet [id=" + id + ", getType()=" + getType()
				+ ", getPriority()=" + getPriority() + ", getName()="
				+ getName() + "]";
	}
}