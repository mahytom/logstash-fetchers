package eu.wajja.input.fetcher.model;

public class History {

	private boolean latest;
	private User createdBy;
	private String createdDate;
	private Links _links;
	private Expandable _expandable;

	public boolean isLatest() {

		return latest;
	}

	public void setLatest(boolean latest) {

		this.latest = latest;
	}

	public User getCreatedBy() {

		return createdBy;
	}

	public void setCreatedBy(User createdBy) {

		this.createdBy = createdBy;
	}

	public String getCreatedDate() {

		return createdDate;
	}

	public void setCreatedDate(String createdDate) {

		this.createdDate = createdDate;
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
