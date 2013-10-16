package pl.cyfronet.coin.clew.client.controller.cf.appliancetype;

import java.util.List;

import org.fusesource.restygwt.client.Json;

public class NewApplianceType {
	public enum Visibility {
		unpublished,
		published
	}
	
	private String name;
	private String description;
	private boolean shared;
	private boolean scalable;
	private Visibility visibility;
	@Json(name = "preference_cpu")
	private String preferenceCpu;
	@Json(name = "preference_memory")
	private String preferenceMemory;
	@Json(name = "preference_disk")
	private String preferenceDisk;
	private String author;
	@Json(name = "security_proxy")
	private String securityProxy;
	@Json(name = "appliance_ids")
	private List<Long> applianceIds;
	@Json(name = "port_mapping_template_ids")
	private List<Long> portMappingTemplateIds;
	@Json(name = "appliance_configuration_template_ids")
	private List<Long> applianceConfigurationTemplateIds;
	@Json(name = "virtual_machine_template_ids")
	private List<Long> virtualMachineTemplateIds;
	
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
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getSecurityProxy() {
		return securityProxy;
	}
	public void setSecurityProxy(String securityProxy) {
		this.securityProxy = securityProxy;
	}
	
	public List<Long> getApplianceIds() {
		return applianceIds;
	}
	public void setApplianceIds(List<Long> applianceIds) {
		this.applianceIds = applianceIds;
	}
	
	public List<Long> getPortMappingTemplateIds() {
		return portMappingTemplateIds;
	}
	public void setPortMappingTemplateIds(List<Long> portMappingTemplateIds) {
		this.portMappingTemplateIds = portMappingTemplateIds;
	}
	
	public List<Long> getApplianceConfigurationTemplateIds() {
		return applianceConfigurationTemplateIds;
	}
	public void setApplianceConfigurationTemplateIds(
			List<Long> applianceConfigurationTemplateIds) {
		this.applianceConfigurationTemplateIds = applianceConfigurationTemplateIds;
	}
	
	public List<Long> getVirtualMachineTemplateIds() {
		return virtualMachineTemplateIds;
	}
	public void setVirtualMachineTemplateIds(List<Long> virtualMachineTemplateIds) {
		this.virtualMachineTemplateIds = virtualMachineTemplateIds;
	}
	
	@Override
	public String toString() {
		return "NewApplianceType [name=" + name + ", description="
				+ description + ", shared=" + shared + ", scalable=" + scalable
				+ ", visibility=" + getVisibility() + ", preferenceCpu="
				+ preferenceCpu + ", preferenceMemory=" + preferenceMemory
				+ ", preferenceDisk=" + preferenceDisk + ", author=" + author
				+ ", securityProxy=" + securityProxy + ", applianceIds="
				+ applianceIds + ", portMappingTemplateIds="
				+ portMappingTemplateIds
				+ ", applianceConfigurationTemplateIds="
				+ applianceConfigurationTemplateIds
				+ ", virtualMachineTemplateIds=" + virtualMachineTemplateIds
				+ "]";
	}
	
	public Visibility getVisibility() {
		return visibility;
	}
	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}
}