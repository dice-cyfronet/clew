package pl.cyfronet.coin.clew.client.controller.cf.applianceinstance;

import java.util.List;

import org.fusesource.restygwt.client.Json;

public class ApplianceInstance extends NewApplianceInstance {
	public enum State {
		satisfied,
		unsatisfied
	}
	
	private String id;
	@Json(name = "appliance_type_id")
	private String applianceTypeId;
	@Json(name = "appliance_configuration_instance_id")
	private String configurationInstanceId;
	private State state;
	@Json(name = "state_explanation")
	private String stateExplanation;
	@Json(name = "amount_billed")
	private long amountBilled;
	@Json(name = "prepaid_until")
	private String prepaidUntil;
	private String description;
	@Json(name = "virtual_machine_ids")
	private List<String> virtualMachineIds;
	
	public String getPrepaidUntil() {
		return prepaidUntil;
	}
	public void setPrepaidUntil(String prepaidUntil) {
		this.prepaidUntil = prepaidUntil;
	}
	public long getAmountBilled() {
		return amountBilled;
	}
	public void setAmountBilled(long amountBilled) {
		this.amountBilled = amountBilled;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public String getStateExplanation() {
		return stateExplanation;
	}
	public void setStateExplanation(String stateExplanation) {
		this.stateExplanation = stateExplanation;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getApplianceTypeId() {
		return applianceTypeId;
	}
	public void setApplianceTypeId(String applianceTypeId) {
		this.applianceTypeId = applianceTypeId;
	}
	public String getConfigurationInstanceId() {
		return configurationInstanceId;
	}
	public void setConfigurationInstanceId(String configurationInstanceId) {
		this.configurationInstanceId = configurationInstanceId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getVirtualMachineIds() {
		return virtualMachineIds;
	}
	public void setVirtualMachineIds(List<String> virtualMachineIds) {
		this.virtualMachineIds = virtualMachineIds;
	}
}
