package pl.cyfronet.coin.impl.air.client;


public class ATPortMapping {

	private String id;

	private int port;

	private String service_name;

	private boolean http;

	private boolean https;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getService_name() {
		return service_name;
	}

	public void setService_name(String service_name) {
		this.service_name = service_name;
	}

	public boolean isHttp() {
		return http;
	}

	public void setHttp(boolean http) {
		this.http = http;
	}

	public boolean isHttps() {
		return https;
	}

	public void setHttps(boolean https) {
		this.https = https;
	}

	@Override
	public String toString() {
		return "ATPortMapping [id=" + id + ", port=" + port + ", service_name="
				+ service_name + ", http=" + http + ", https=" + https + "]";
	}
}
