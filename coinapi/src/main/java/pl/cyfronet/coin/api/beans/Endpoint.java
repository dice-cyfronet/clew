package pl.cyfronet.coin.api.beans;


public class Endpoint {

	private String description;
	
	private String descriptor;
	
	private String invocationPath;
	
	private int port;
	
	private String serviceName;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String serviceDescription) {
		this.description = serviceDescription;
	}
	public String getInvocationPath() {
		return invocationPath;
	}
	public void setInvocationPath(String invocationPath) {
		this.invocationPath = invocationPath;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @return the descriptor
	 */
	public String getDescriptor() {
		return descriptor;
	}
	/**
	 * @param descriptor the descriptor to set
	 */
	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}
	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}