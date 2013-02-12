package pl.cyfronet.coin.impl.air.client;

public class GrantBean {

	private String payload_get;
	private String payload_post;
	private String payload_put;
	private String payload_delete;
	
	public String getPayload_get() {
		return payload_get;
	}

	public void setPayload_get(String payload_get) {
		this.payload_get = payload_get;
	}

	public String getPayload_post() {
		return payload_post;
	}

	public void setPayload_post(String payload_post) {
		this.payload_post = payload_post;
	}

	public String getPayload_put() {
		return payload_put;
	}

	public void setPayload_put(String payload_put) {
		this.payload_put = payload_put;
	}

	public String getPayload_delete() {
		return payload_delete;
	}

	public void setPayload_delete(String payload_delete) {
		this.payload_delete = payload_delete;
	}

	@Override
	public String toString() {
		return "GrantBean [payload_get=" + payload_get + ", payload_post="
				+ payload_post + ", payload_put=" + payload_put
				+ ", payload_delete=" + payload_delete + "]";
	}
}
