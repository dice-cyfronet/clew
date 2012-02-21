package pl.cyfronet.coin.api.beans;


public class Endpoint {
	/**
	 * WSDL or WADL remote location.
	 */
	private String serviceDescription;
	private String invocationPath;
	private int port;
	
	public String getServiceDescription() {
		return serviceDescription;
	}
	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
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
}