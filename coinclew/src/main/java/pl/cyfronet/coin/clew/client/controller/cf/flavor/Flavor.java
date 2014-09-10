package pl.cyfronet.coin.clew.client.controller.cf.flavor;

import org.fusesource.restygwt.client.Json;

public class Flavor {
	private String id;
	@Json(name = "flavor_name")
	private String name;
	private Float cpu;
	private Float memory;
	private Float hdd;
	@Json(name = "hourly_cost")
	private Integer hourlyCost;
	
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
	public Float getCpu() {
		return cpu;
	}
	public void setCpu(Float cpu) {
		this.cpu = cpu;
	}
	public Float getMemory() {
		return memory;
	}
	public void setMemory(Float memory) {
		this.memory = memory;
	}
	public Float getHdd() {
		return hdd;
	}
	public void setHdd(Float hdd) {
		this.hdd = hdd;
	}
	public Integer getHourlyCost() {
		return hourlyCost;
	}
	public void setHourlyCost(Integer hourlyCost) {
		this.hourlyCost = hourlyCost;
	}
	@Override
	public String toString() {
		return "Flavor [id=" + id + ", name=" + name + ", cpu=" + cpu
				+ ", memory=" + memory + ", hdd=" + hdd + ", hourlyCost="
				+ hourlyCost + "]";
	}
}