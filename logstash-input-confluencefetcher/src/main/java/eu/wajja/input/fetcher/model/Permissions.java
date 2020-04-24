package eu.wajja.input.fetcher.model;

import java.util.HashSet;
import java.util.Set;

public class Permissions {

	private Set<String> users = new HashSet<>();
	private Set<String> groups = new HashSet<>();

	public Set<String> getUsers() {
		return users;
	}

	public void setUsers(Set<String> users) {
		this.users = users;
	}

	public Set<String> getGroups() {
		return groups;
	}

	public void setGroups(Set<String> groups) {
		this.groups = groups;
	}

}
