package pl.cyfronet.coin.clew.client.controller.cf.appliancetype;

import java.util.List;

import org.fusesource.restygwt.client.Json;

public class ApplianceType extends NewApplianceType {
	private String id;
	
	@Json(name = "compute_site_ids")
	private List<String> computeSiteIds;
	
	private String applianceInstanceId;
	
	private boolean saving;

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public List<String> getComputeSiteIds() {
		return computeSiteIds;
	}
	
	public void setComputeSiteIds(List<String> computeSiteIds) {
		this.computeSiteIds = computeSiteIds;
	}
	
	public String getApplianceInstanceId() {
		return applianceInstanceId;
	}
	
	public void setApplianceInstanceId(String applianceInstanceId) {
		this.applianceInstanceId = applianceInstanceId;
	}
	
	public boolean isSaving() {
		return saving;
	}
	
	public void setSaving(boolean saving) {
		this.saving = saving;
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