package pl.cyfronet.coin.impl.air.client;

public class ApplianceTypeRequest {

	private String client;

	private String name;

	private String description;

	private Boolean scalable;

	private Boolean published;

	private Boolean shared;

	private Boolean development;

	private String original_appliance;

	private String proxy_conf_name;

	private Float cpu;
	private Integer memory;
	private Integer disk;
	
	/**
	 * #1021
	 * @since 1.1.0
	 */
	private String author;

	/**
	 * @return the client
	 */
	public String getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(String client) {
		this.client = client;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the scalable
	 */
	public Boolean getScalable() {
		return scalable;
	}

	/**
	 * @param scalable the scalable to set
	 */
	public void setScalable(Boolean scalable) {
		this.scalable = scalable;
	}

	/**
	 * @return the published
	 */
	public Boolean getPublished() {
		return published;
	}

	/**
	 * @param published the published to set
	 */
	public void setPublished(Boolean published) {
		this.published = published;
	}

	/**
	 * @return the shared
	 */
	public Boolean getShared() {
		return shared;
	}

	/**
	 * @param shared the shared to set
	 */
	public void setShared(Boolean shared) {
		this.shared = shared;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the development
	 */
	public Boolean getDevelopment() {
		return development;
	}

	/**
	 * @param development the development to set
	 */
	public void setDevelopment(Boolean development) {
		this.development = development;
	}

	/**
	 * @return the original_appliance
	 */
	public String getOriginal_appliance() {
		return original_appliance;
	}

	/**
	 * @param original_appliance the original_appliance to set
	 */
	public void setOriginal_appliance(String original_appliance) {
		this.original_appliance = original_appliance;
	}

	public String getProxy_conf_name() {
		return proxy_conf_name;
	}

	public void setProxy_conf_name(String proxy_conf_name) {
		this.proxy_conf_name = proxy_conf_name;
	}

	/**
	 * @return the cpu
	 */
	public Float getCpu() {
		return cpu;
	}

	/**
	 * @param cpu the cpu to set
	 */
	public void setCpu(Float cpu) {
		this.cpu = cpu;
	}

	/**
	 * @return the memory
	 */
	public Integer getMemory() {
		return memory;
	}

	/**
	 * @param memory the memory to set
	 */
	public void setMemory(Integer memory) {
		this.memory = memory;
	}

	/**
	 * @return the disk
	 */
	public Integer getDisk() {
		return disk;
	}

	/**
	 * @param disk the disk to set
	 */
	public void setDisk(Integer disk) {
		this.disk = disk;
	}

	@Override
	public String toString() {
		return "ApplianceTypeRequest [client=" + client + ", name=" + name
				+ ", description=" + description + ", scalable=" + scalable
				+ ", published=" + published + ", shared=" + shared
				+ ", development=" + development + ", original_appliance="
				+ original_appliance + ", proxy_conf_name=" + proxy_conf_name
				+ ", cpu=" + cpu + ", memory=" + memory + ", disk=" + disk
				+ ", author=" + author + "]";
	}
}
