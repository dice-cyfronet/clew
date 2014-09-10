package pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliance;

import java.util.List;

import org.fusesource.restygwt.client.Json;

import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance.State;

public class AggregateAppliance {
	private String id;
	
	@Json(name = "port_mapping_templates")
	private List<AggregatePortMappingTemplate> portMappingTemplates;
	
	@Json(name = "virtual_machines")
	private List<AggregateVm> virtualMachines;
	
	private State state;
	private String name;
	private String description;
	
	@Json(name = "state_explanation")
	private String stateExplanation;
	
	@Json(name = "amount_billed")
	private long amountBilled;
	
	@Json(name = "prepaid_until")
	private String prepaidUntil;
	
	@Json(name = "appliance_set_id")
	private String applianceSetId;

	public String getApplianceSetId() {
		return applianceSetId;
	}

	public void setApplianceSetId(String applianceSetId) {
		this.applianceSetId = applianceSetId;
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

	public List<AggregatePortMappingTemplate> getPortMappingTemplates() {
		return portMappingTemplates;
	}

	public void setPortMappingTemplates(
			List<AggregatePortMappingTemplate> portMappingTemplates) {
		this.portMappingTemplates = portMappingTemplates;
	}

	public List<AggregateVm> getVirtualMachines() {
		return virtualMachines;
	}

	public void setVirtualMachines(List<AggregateVm> virtualMachines) {
		this.virtualMachines = virtualMachines;
	}

	@Override
	public String toString() {
		return "AggregateAppliance [id=" + id + ", portMappingTemplates="
				+ portMappingTemplates + ", virtualMachines=" + virtualMachines
				+ ", state=" + state + ", name=" + name + ", description="
				+ description + ", stateExplanation=" + stateExplanation + "]";
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

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
}