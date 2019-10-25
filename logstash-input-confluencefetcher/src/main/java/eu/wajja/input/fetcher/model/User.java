package eu.wajja.input.fetcher.model;

public class User {

	private String type;
	private String username;
	private String userKey;
	private ProfilePicture profilePicture;
	private String displayName;
	private Links _links;
	private Expandable _expandable;

	public Expandable get_expandable() {

		return _expandable;
	}

	public void set_expandable(Expandable _expandable) {

		this._expandable = _expandable;
	}

	public String getType() {

		return type;
	}

	public void setType(String type) {

		this.type = type;
	}

	public String getUsername() {

		return username;
	}

	public void setUsername(String username) {

		this.username = username;
	}

	public String getUserKey() {

		return userKey;
	}

	public void setUserKey(String userKey) {

		this.userKey = userKey;
	}

	public ProfilePicture getProfilePicture() {

		return profilePicture;
	}

	public void setProfilePicture(ProfilePicture profilePicture) {

		this.profilePicture = profilePicture;
	}

	public String getDisplayName() {

		return displayName;
	}

	public void setDisplayName(String displayName) {

		this.displayName = displayName;
	}

	public Links get_links() {

		return _links;
	}

	public void set_links(Links _links) {

		this._links = _links;
	}
}
