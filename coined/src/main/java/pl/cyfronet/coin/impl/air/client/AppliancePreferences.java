package pl.cyfronet.coin.impl.air.client;

public class AppliancePreferences {
	private Float cpu;
	private Integer memory;
	private Integer disk;

	public Float getCpu() {
		return cpu;
	}

	public void setCpu(Float cpu) {
		this.cpu = cpu;
	}

	public Integer getMemory() {
		return memory;
	}

	public void setMemory(Integer memory) {
		this.memory = memory;
	}

	public Integer getDisk() {
		return disk;
	}

	public void setDisk(Integer disk) {
		this.disk = disk;
	}

	@Override
	public String toString() {
		return "AppliancePreferences [cpu=" + cpu + ", memory=" + memory
				+ ", disk=" + disk + "]";
	}
}
