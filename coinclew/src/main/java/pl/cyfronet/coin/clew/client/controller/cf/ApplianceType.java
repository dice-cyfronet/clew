package pl.cyfronet.coin.clew.client.controller.cf;


public class ApplianceType extends NewApplianceType {
	private String id;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "ApplianceType [id=" + id + ", getName()=" + getName()
				+ ", getDescription()=" + getDescription() + ", isShared()="
				+ isShared() + ", isScalable()=" + isScalable()
				+ ", getVisibility()=" + getVisibility()
				+ ", getPreferenceCpu()=" + getPreferenceCpu()
				+ ", getPreferenceMemory()=" + getPreferenceMemory()
				+ ", getPreferenceDisk()=" + getPreferenceDisk()
				+ ", getAuthor()=" + getAuthor() + ", getSecurityProxy()="
				+ getSecurityProxy() + ", getApplianceIds()="
				+ getApplianceIds() + ", getPortMappingTemplateIds()="
				+ getPortMappingTemplateIds()
				+ ", getApplianceConfigurationTemplateIds()="
				+ getApplianceConfigurationTemplateIds()
				+ ", getVirtualMachineTemplateIds()="
				+ getVirtualMachineTemplateIds() + ", toString()="
				+ super.toString() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + "]";
	}
}