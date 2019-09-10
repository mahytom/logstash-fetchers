package eu.wajja.input.fetcher.model;

public class Version {

	private User by;
	private String when;
	private String message;
	private Integer number;
	private Boolean minorEdit;
	private Boolean hidden;

	private Links _links;
	private Expandable _expandable;

	public Version() {
	}

	public Boolean getHidden() {

		return hidden;
	}

	public void setHidden(Boolean hidden) {

		this.hidden = hidden;
	}

	public Version(Integer number) {

		this.number = number;
	}

	public User getBy() {

		return by;
	}

	public void setBy(User by) {

		this.by = by;
	}

	public String getWhen() {

		return when;
	}

	public void setWhen(String when) {

		this.when = when;
	}

	public String getMessage() {

		return message;
	}

	public void setMessage(String message) {

		this.message = message;
	}

	public Integer getNumber() {

		return number;
	}

	public void setNumber(Integer number) {

		this.number = number;
	}

	public Boolean getMinorEdit() {

		return minorEdit;
	}

	public void setMinorEdit(Boolean minorEdit) {

		this.minorEdit = minorEdit;
	}

	public Links get_links() {

		return _links;
	}

	public void set_links(Links _links) {

		this._links = _links;
	}

	public Expandable get_expandable() {

		return _expandable;
	}

	public void set_expandable(Expandable _expandable) {

		this._expandable = _expandable;
	}
}
