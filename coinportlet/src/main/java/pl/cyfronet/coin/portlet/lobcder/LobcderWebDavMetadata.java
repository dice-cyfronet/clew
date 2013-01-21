package pl.cyfronet.coin.portlet.lobcder;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

public class LobcderWebDavMetadata {
	private boolean driSupervised;
	@DecimalMin("0") @DecimalMax("9223372036854775807") private long driChecksum;
	private long driLastValidationDateMs;
	private String creationDate;
	private String modificationDate;
	private String format;
	
	@Override
	public String toString() {
		return "LobcderWebDavMetadata [driSupervised=" + driSupervised
				+ ", driChecksum=" + driChecksum + ", driLastValidationDateMs="
				+ driLastValidationDateMs + ", creationDate=" + creationDate
				+ ", modificationDate=" + modificationDate + ", format="
				+ format + "]";
	}
	
	public boolean isDriSupervised() {
		return driSupervised;
	}
	public void setDriSupervised(boolean driSupervised) {
		this.driSupervised = driSupervised;
	}
	public long getDriChecksum() {
		return driChecksum;
	}
	public void setDriChecksum(long driChecksum) {
		this.driChecksum = driChecksum;
	}
	public long getDriLastValidationDateMs() {
		return driLastValidationDateMs;
	}
	public void setDriLastValidationDateMs(long driLastValidationDateMs) {
		this.driLastValidationDateMs = driLastValidationDateMs;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getModificationDate() {
		return modificationDate;
	}
	public void setModificationDate(String modificationDate) {
		this.modificationDate = modificationDate;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
}