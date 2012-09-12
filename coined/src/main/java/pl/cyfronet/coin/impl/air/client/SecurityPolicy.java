package pl.cyfronet.coin.impl.air.client;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "securityPolicy")
public class SecurityPolicy {
	
	private String policy_name;
	
	private String id;

	
	public String getPolicy_name() {
		return policy_name;
	}

	public void setPolicy_name(String policy_name) {
		this.policy_name = policy_name;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
