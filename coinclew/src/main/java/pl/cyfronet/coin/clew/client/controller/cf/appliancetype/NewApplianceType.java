package pl.cyfronet.coin.clew.client.controller.cf.appliancetype;

import java.util.List;

import org.fusesource.restygwt.client.Json;

public class NewApplianceType {
	private String name;
	private String description;
	private boolean shared;
	private boolean scalable;
	@Json(name = "preference_cpu")
	private String preferenceCpu;
	@Json(name = "preference_memory")
	private String preferenceMemory;
	@Json(name = "preference_disk")
	private String preferenceDisk;
	@Json(name = "author_id")
	private String authorId;
	private boolean active;
	@Json(name = "security_proxy")
	private String securityProxy;
	@Json(name = "appliance_ids")
	private List<String> applianceIds;
	@Json(name = "port_mapping_template_ids")
	private List<String> portMappingTemplateIds;
	@Json(name = "appliance_configuration_template_ids")
	private List<String> applianceConfigurationTemplateIds;
	@Json(name = "virtual_machine_template_ids")
	private List<String> virtualMachineTemplateIds;
	@Json(name = "security_proxy_id")
	private String securityProxyId;
	@Json(name = "visible_to")
	private String visibleTo;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isShared() {
		return shared;
	}
	public void setShared(boolean shared) {
		this.shared = shared;
	}
	
	public boolean isScalable() {
		return scalable;
	}
	public void setScalable(boolean scalable) {
		this.scalable = scalable;
	}
	
	public String getPreferenceCpu() {
		return preferenceCpu;
	}
	public void setPreferenceCpu(String preferenceCpu) {
		this.preferenceCpu = preferenceCpu;
	}
	
	public String getPreferenceMemory() {
		return preferenceMemory;
	}
	public void setPreferenceMemory(String preferenceMemory) {
		this.preferenceMemory = preferenceMemory;
	}
	
	public String getPreferenceDisk() {
		return preferenceDisk;
	}
	public void setPreferenceDisk(String preferenceDisk) {
		this.preferenceDisk = preferenceDisk;
	}
	
	public String getSecurityProxy() {
		return securityProxy;
	}
	public void setSecurityProxy(String securityProxy) {
		this.securityProxy = securityProxy;
	}
	
	public List<String> getApplianceIds() {
		return applianceIds;
	}
	public void setApplianceIds(List<String> applianceIds) {
		this.applianceIds = applianceIds;
	}
	
	public List<String> getPortMappingTemplateIds() {
		return portMappingTemplateIds;
	}
	public void setPortMappingTemplateIds(List<String> portMappingTemplateIds) {
		this.portMappingTemplateIds = portMappingTemplateIds;
	}
	
	public List<String> getApplianceConfigurationTemplateIds() {
		return applianceConfigurationTemplateIds;
	}
	public void setApplianceConfigurationTemplateIds(
			List<String> applianceConfigurationTemplateIds) {
		this.applianceConfigurationTemplateIds = applianceConfigurationTemplateIds;
	}
	
	public List<String> getVirtualMachineTemplateIds() {
		return virtualMachineTemplateIds;
	}
	public void setVirtualMachineTemplateIds(List<String> virtualMachineTemplateIds) {
		this.virtualMachineTemplateIds = virtualMachineTemplateIds;
	}
	
	@Override
	public String toString() {
		return "NewApplianceType [name=" + name + ", description="
				+ description + ", shared=" + shared + ", scalable=" + scalable
				+ ", preferenceCpu=" + preferenceCpu + ", preferenceMemory="
				+ preferenceMemory + ", preferenceDisk=" + preferenceDisk
				+ ", authorId=" + authorId + ", active=" + active
				+ ", securityProxy=" + securityProxy + ", applianceIds="
				+ applianceIds + ", portMappingTemplateIds="
				+ portMappingTemplateIds
				+ ", applianceConfigurationTemplateIds="
				+ applianceConfigurationTemplateIds
				+ ", virtualMachineTemplateIds=" + virtualMachineTemplateIds
				+ ", securityProxyId=" + securityProxyId + "]";
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	
	public String getSecurityProxyId() {
		return securityProxyId;
	}
	public void setSecurityProxyId(String securityProxyId) {
		this.securityProxyId = securityProxyId;
	}
	public String getVisibleTo() {
		return visibleTo;
	}
	public void setVisibleTo(String visibleTo) {
		this.visibleTo = visibleTo;
	}
}