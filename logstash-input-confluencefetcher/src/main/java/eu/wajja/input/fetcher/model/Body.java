package eu.wajja.input.fetcher.model;

public class Body {

	private Storage storage;
	private View view;
	private Expandable _expandable;

	public Body() {
	}

	public Body(String content) {
		this.storage = new Storage(content);
	}

	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public Expandable get_expandable() {
		return _expandable;
	}

	public void set_expandable(Expandable _expandable) {
		this._expandable = _expandable;
	}
}
