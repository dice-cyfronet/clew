package pl.cyfronet.coin.clew.client.controller.cf.user;

import java.util.List;

public class UsersResponse {
	private List<User> users;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}