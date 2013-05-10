package pl.cyfronet.coin.impl.air.client;

public class AppliancePreferences {
	private Float cpu;
	private Long memory;
	private Long disk;

	public Float getCpu() {
		return cpu;
	}

	public void setCpu(Float cpu) {
		this.cpu = cpu;
	}

	public Long getMemory() {
		return memory;
	}

	public void setMemory(Long memory) {
		this.memory = memory;
	}

	public Long getDisk() {
		return disk;
	}

	public void setDisk(Long disk) {
		this.disk = disk;
	}

	@Override
	public String toString() {
		return "AppliancePreferences [cpu=" + cpu + ", memory=" + memory
				+ ", disk=" + disk + "]";
	}
}
