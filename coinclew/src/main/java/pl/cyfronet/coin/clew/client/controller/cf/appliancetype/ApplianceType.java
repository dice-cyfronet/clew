package pl.cyfronet.coin.clew.client.controller.cf.appliancetype;


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
				+ ", getPreferenceCpu()=" + getPreferenceCpu()
				+ ", getPreferenceMemory()=" + getPreferenceMemory()
				+ ", getPreferenceDisk()=" + getPreferenceDisk()
				+ ", getSecurityProxy()=" + getSecurityProxy()
				+ ", getApplianceIds()=" + getApplianceIds()
				+ ", getPortMappingTemplateIds()="
				+ getPortMappingTemplateIds()
				+ ", getApplianceConfigurationTemplateIds()="
				+ getApplianceConfigurationTemplateIds()
				+ ", getVirtualMachineTemplateIds()="
				+ getVirtualMachineTemplateIds() + ", toString()="
				+ super.toString() + ", isActive()=" + isActive()
				+ ", getAuthorId()=" + getAuthorId()
				+ ", getSecurityProxyId()=" + getSecurityProxyId()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ "]";
	}
}