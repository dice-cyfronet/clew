package pl.cyfronet.coin.portlet.lobcder;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

public class LobcderWebDavMetadata {
	private boolean driSupervised;
	@DecimalMin("0") @DecimalMax("9223372036854775807") private String driChecksum;
	private String driLastValidationDateMs;
	
	public boolean isDriSupervised() {
		return driSupervised;
	}
	public void setDriSupervised(boolean driSupervised) {
		this.driSupervised = driSupervised;
	}
	public String getDriChecksum() {
		return driChecksum;
	}
	public void setDriChecksum(String driChecksum) {
		this.driChecksum = driChecksum;
	}
	public String getDriLastValidationDateMs() {
		return driLastValidationDateMs;
	}
	public void setDriLastValidationDateMs(String driLastValidationDateMs) {
		this.driLastValidationDateMs = driLastValidationDateMs;
	}
	
	@Override
	public String toString() {
		return "LobcderWebDavMetadata [driSupervised=" + driSupervised
				+ ", driChecksum=" + driChecksum + ", driLastValidationDateMs="
				+ driLastValidationDateMs + "]";
	}
}