package pl.cyfronet.coin.portlet.metadata;

import pl.cyfronet.coin.portlet.lobcder.LobcderWebDavMetadata;

public class Metadata {
	private LobcderWebDavMetadata lobcderWebDavMetadata;
	private String owner;
	private String readPermissions;
	private String writePermissions;
	private String uid;

	public LobcderWebDavMetadata getLobcderWebDavMetadata() {
		return lobcderWebDavMetadata;
	}
	public void setLobcderWebDavMetadata(LobcderWebDavMetadata lobcderWebDavMetadata) {
		this.lobcderWebDavMetadata = lobcderWebDavMetadata;
	}
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getReadPermissions() {
		return readPermissions;
	}
	public void setReadPermissions(String readPermissions) {
		this.readPermissions = readPermissions;
	}
	@Override
	public String toString() {
		return "Metadata [lobcderWebDavMetadata=" + lobcderWebDavMetadata
				+ ", owner=" + owner + ", readPermissions=" + readPermissions
				+ ", writePermissions=" + writePermissions + ", uid=" + uid
				+ "]";
	}
	public String getWritePermissions() {
		return writePermissions;
	}
	public void setWritePermissions(String writePermissions) {
		this.writePermissions = writePermissions;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
}