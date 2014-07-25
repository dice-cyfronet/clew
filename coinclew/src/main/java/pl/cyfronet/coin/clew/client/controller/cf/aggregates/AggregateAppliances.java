package pl.cyfronet.coin.clew.client.controller.cf.aggregates;

import java.util.List;

public class AggregateAppliances {
	private List<AggregateAppliance> appliances;

	public List<AggregateAppliance> getAppliances() {
		return appliances;
	}

	public void setAppliances(List<AggregateAppliance> appliances) {
		this.appliances = appliances;
	}
}