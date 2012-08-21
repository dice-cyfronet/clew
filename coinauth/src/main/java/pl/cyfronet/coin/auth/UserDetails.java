package pl.cyfronet.coin.auth;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class UserDetails {

	private String username;
	private String language;
	private String country;
	private List<String> role;
	private String postcode;
	private String fullname;
	private String email;

	@XmlTransient
	private long creationTime;

	public UserDetails() {
		creationTime = System.currentTimeMillis();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<String> getRole() {
		return role;
	}

	public void setRole(List<String> role) {
		this.role = role;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	long getCreationTime() {
		return creationTime;
	}
	
	@Override
	public String toString() {
		return "UserDetails [username=" + username + ", language=" + language
				+ ", country=" + country + ", role=" + role + ", postcode="
				+ postcode + ", fullname=" + fullname + ", email=" + email
				+ "]";
	}
}
