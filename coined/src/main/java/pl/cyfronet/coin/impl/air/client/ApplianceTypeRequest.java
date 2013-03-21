package pl.cyfronet.coin.impl.air.client;


public class ApplianceTypeRequest {

private String client;
	
	private String name;
	
	private String description;

	private Boolean in_proxy;
	
	private Boolean scalable;
	
	private Boolean published;
	
	private Boolean shared;
	
	private Boolean http;
	
	private Boolean vnc;
	
	private Boolean development;
	
	private String original_appliance;
	
	private String proxy_conf_name;

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
	 * @return the in_proxy
	 */
	public Boolean getIn_proxy() {
		return in_proxy;
	}

	/**
	 * @param in_proxy the in_proxy to set
	 */
	public void setIn_proxy(Boolean in_proxy) {
		this.in_proxy = in_proxy;
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
	 * @return the http
	 */
	public Boolean getHttp() {
		return http;
	}

	/**
	 * @param http the http to set
	 */
	public void setHttp(Boolean http) {
		this.http = http;
	}

	/**
	 * @return the vnc
	 */
	public Boolean getVnc() {
		return vnc;
	}

	/**
	 * @param vnc the vnc to set
	 */
	public void setVnc(Boolean vnc) {
		this.vnc = vnc;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AtomicServiceRequest [client=" + client + ", name=" + name
				+ ", description=" + description + ", in_proxy=" + in_proxy
				+ ", scalable=" + scalable + ", published=" + published
				+ ", shared=" + shared + ", http=" + http + ", vnc=" + vnc
				+ ", development=" + development + ", original_appliance="
				+ original_appliance + ", proxy_conf_name=" + proxy_conf_name
				+ ", author=" + author + "]";
	}
}
