package pl.cyfronet.coin.clew.client.controller.cf.applianceinstance;

import java.util.List;
import java.util.Map;

import org.fusesource.restygwt.client.Json;

public class NewApplianceInstance {
	@Json(name = "configuration_template_id")
	private String configurationTemplateId;
	@Json(name = "appliance_set_id")
	private String applianceSetId;
	private Map<String, String> params;
	private String name;
	@Json(name = "user_key_id")
	private String userKeyId;
	@Json(name = "dev_mode_property_set")
	private Map<String, String> developmentProperties;
	@Json(name = "compute_site_ids")
	private List<String> computeSiteIds;
	@Json(name = "team_id")
	private String teamId;
	
	public String getConfigurationTemplateId() {
		return configurationTemplateId;
	}
	public void setConfigurationTemplateId(String configurationTemplateId) {
		this.configurationTemplateId = configurationTemplateId;
	}
	public String getApplianceSetId() {
		return applianceSetId;
	}
	public void setApplianceSetId(String applianceSetId) {
		this.applianceSetId = applianceSetId;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public String getUserKeyId() {
		return userKeyId;
	}
	public void setUserKeyId(String userKeyId) {
		this.userKeyId = userKeyId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, String> getDevelopmentProperties() {
		return developmentProperties;
	}
	public void setDevelopmentProperties(Map<String, String> developmentProperties) {
		this.developmentProperties = developmentProperties;
	}
	
	public List<String> getComputeSiteIds() {
		return computeSiteIds;
	}
	public void setComputeSiteIds(List<String> computeSiteIds) {
		this.computeSiteIds = computeSiteIds;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
}