package eu.wajja.input.fetcher.model;

public class View {

	private String value;
	private String representation;
	private Expandable _expandable;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRepresentation() {
		return representation;
	}

	public void setRepresentation(String representation) {
		this.representation = representation;
	}

	public Expandable get_expandable() {
		return _expandable;
	}

	public void set_expandable(Expandable _expandable) {
		this._expandable = _expandable;
	}

}
