package pl.cyfronet.coin.clew.client.controller.cf.devmodepropertyset;

import org.fusesource.restygwt.client.Json;

import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;

public class DevelopmentModePropertySet extends ApplianceType {
	@Json(name = "appliance_id")
	private String applianceId;
	
	public String getApplianceId() {
		return applianceId;
	}
	public void setApplianceId(String applianceId) {
		this.applianceId = applianceId;
	}
	
	@Override
	public String toString() {
		return "DevelopmentModePropertySet [applianceId=" + applianceId
				+ ", getId()=" + getId() + ", toString()=" + super.toString()
				+ ", getName()=" + getName() + ", getDescription()="
				+ getDescription() + ", isShared()=" + isShared()
				+ ", isScalable()=" + isScalable() + ", getPreferenceCpu()="
				+ getPreferenceCpu() + ", getPreferenceMemory()="
				+ getPreferenceMemory() + ", getPreferenceDisk()="
				+ getPreferenceDisk() + ", getSecurityProxy()="
				+ getSecurityProxy() + ", getApplianceIds()="
				+ getApplianceIds() + ", getPortMappingTemplateIds()="
				+ getPortMappingTemplateIds()
				+ ", getApplianceConfigurationTemplateIds()="
				+ getApplianceConfigurationTemplateIds()
				+ ", getVirtualMachineTemplateIds()="
				+ getVirtualMachineTemplateIds() + ", isActive()=" + isActive()
				+ ", getAuthorId()=" + getAuthorId()
				+ ", getSecurityProxyId()=" + getSecurityProxyId()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ "]";
	}
}