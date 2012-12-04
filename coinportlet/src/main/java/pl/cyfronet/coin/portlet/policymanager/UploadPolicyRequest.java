package pl.cyfronet.coin.portlet.policymanager;

import org.hibernate.validator.constraints.NotEmpty;

public class UploadPolicyRequest {
	private String policyName;
	private String policyBody;
	
	@NotEmpty(message = "Policy name cannot be empty")
	public String getPolicyName() {
		return policyName;
	}
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
	
	@NotEmpty(message = "Policy contents cannot be empty")
	public String getPolicyBody() {
		return policyBody;
	}
	public void setPolicyBody(String policyBody) {
		this.policyBody = policyBody;
	}
	
	@Override
	public String toString() {
		return "UploadPolicyRequest [policyName=" + policyName
				+ ", policyBody=" + policyBody + "]";
	}
}