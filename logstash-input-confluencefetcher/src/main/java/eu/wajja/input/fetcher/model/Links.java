package eu.wajja.input.fetcher.model;

public class Links {

	private String self;
	private String webui;
	private String tinyui;
	private String collection;
	private String base;
	private String context;
	private String edit;
	private String next;
	private String prev;

	public String getPrev() {
		return prev;
	}

	public void setPrev(String prev) {
		this.prev = prev;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getEdit() {

		return edit;
	}

	public void setEdit(String edit) {

		this.edit = edit;
	}

	public String getSelf() {

		return self;
	}

	public String getTinyui() {

		return tinyui;
	}

	public void setTinyui(String tinyui) {

		this.tinyui = tinyui;
	}

	public String getCollection() {

		return collection;
	}

	public void setCollection(String collection) {

		this.collection = collection;
	}

	public String getBase() {

		return base;
	}

	public void setBase(String base) {

		this.base = base;
	}

	public String getContext() {

		return context;
	}

	public void setContext(String context) {

		this.context = context;
	}

	public void setSelf(String self) {

		this.self = self;
	}

	public String getWebui() {

		return webui;
	}

	public void setWebui(String webui) {

		this.webui = webui;
	}
}
