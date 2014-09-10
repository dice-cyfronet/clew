package pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliancetype;

import java.util.List;
import java.util.Map;

import org.fusesource.restygwt.client.Json;

import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.controller.cf.computesite.ComputeSite;
import pl.cyfronet.coin.clew.client.controller.cf.flavor.Flavor;

public class AggregateApplianceType {
	private String id;
	private String name;
	private String description;
	@Json(name = "preference_cpu")
	private String preferenceCpu;
	@Json(name = "preference_memory")
	private String preferenceMemory;
	@Json(name = "preference_disk")
	private String preferenceDisk;
	@Json(name = "matched_flavor")
	private Flavor flavor;
	@Json(name = "compute_site_ids")
	private List<String> computeSiteIds;
	@Json(name = "appliance_configuration_templates")
	private List<ApplianceConfiguration> initialConfigurations;
	private Map<String, ComputeSite> computeSites;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public Flavor getFlavor() {
		return flavor;
	}
	public void setFlavor(Flavor flavor) {
		this.flavor = flavor;
	}
	public List<String> getComputeSiteIds() {
		return computeSiteIds;
	}
	public void setComputeSiteIds(List<String> computeSiteIds) {
		this.computeSiteIds = computeSiteIds;
	}
	public List<ApplianceConfiguration> getInitialConfigurations() {
		return initialConfigurations;
	}
	public void setInitialConfigurations(List<ApplianceConfiguration> initialConfigurations) {
		this.initialConfigurations = initialConfigurations;
	}
	@Override
	public String toString() {
		return "AggregateApplianceType [id=" + id + ", name=" + name + ", description=" + description + ", preferenceCpu=" + preferenceCpu
				+ ", preferenceMemory=" + preferenceMemory + ", preferenceDisk=" + preferenceDisk + ", flavor=" + flavor + ", computeSiteIds=" + computeSiteIds
				+ ", initialConfigurations=" + initialConfigurations + "]";
	}
	public Map<String, ComputeSite> getComputeSites() {
		return computeSites;
	}
	public void setComputeSites(Map<String, ComputeSite> computeSites) {
		this.computeSites = computeSites;
	}
}