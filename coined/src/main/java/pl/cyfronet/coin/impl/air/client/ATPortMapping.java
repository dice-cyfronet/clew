package pl.cyfronet.coin.impl.air.client;


public class ATPortMapping {

	private String id;

	private int port;

	private String service_name;

	private boolean http;

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

	@Override
	public String toString() {
		return "ATPortMapping [id=" + id + ", port=" + port + ", service_name="
				+ service_name + ", http=" + http + "]";
	}
}
