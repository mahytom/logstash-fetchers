package eu.wajja.input.fetcher.model;

import java.util.ArrayList;
import java.util.List;

public class Permissions {

	private List<String> users = new ArrayList<>();
	private List<String> groups = new ArrayList<>();

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

}
