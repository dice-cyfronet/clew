package pl.cyfronet.coin.clew.client.controller.cf.user;

import java.util.List;

import org.fusesource.restygwt.client.Json;

public class User {
	private String id;
	private String login;
	@Json(name = "full_name")
	private String fullName;
	private String email;
	private List<String> roles;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}