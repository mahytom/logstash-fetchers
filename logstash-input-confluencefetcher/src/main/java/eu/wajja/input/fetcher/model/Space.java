package eu.wajja.input.fetcher.model;

public class Space {

	private Expandable _expandable;
	private Links _links;
	private Integer id;
	private String key;
	private String name;
	private String type;

	public Expandable get_expandable() {

		return _expandable;
	}

	public void set_expandable(Expandable _expandable) {

		this._expandable = _expandable;
	}

	public Links get_links() {

		return _links;
	}

	public void set_links(Links _links) {

		this._links = _links;
	}

	public Integer getId() {

		return id;
	}

	public void setId(Integer id) {

		this.id = id;
	}

	public String getKey() {

		return key;
	}

	public void setKey(String key) {

		this.key = key;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getType() {

		return type;
	}

	public void setType(String type) {

		this.type = type;
	}
}
