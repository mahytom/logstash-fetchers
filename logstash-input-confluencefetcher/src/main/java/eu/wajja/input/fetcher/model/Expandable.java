package eu.wajja.input.fetcher.model;

public class Expandable {

	private String metadata;
	private String icon;
	private String description;
	private String homepage;
	private String status;

	private String lastUpdated;
	private String previousVersion;
	private String contributors;
	private String nextVersion;
	private String content;

	private String container;
	private String operations;
	private String body;

	private String history;
	private String ancestors;
	private String children;
	private String descendants;
	private String space;
	private String version;
	private String restrictions;

	private String webresource;
	private String editor;
	private String export_view;
	private String styled_view;
	private String anonymous_export_view;
	private String storage;

	public Expandable() {
	}

	public Expandable(String pageId) {

		this.history = "/rest/api/content/" + pageId + "/history";
		this.children = "/rest/api/content/" + pageId + "/child";
		this.descendants = "/rest/api/content/" + pageId + "/descendant";
	}

	public String getExport_view() {
		return export_view;
	}

	public void setExport_view(String export_view) {
		this.export_view = export_view;
	}

	public String getStyled_view() {
		return styled_view;
	}

	public void setStyled_view(String styled_view) {
		this.styled_view = styled_view;
	}

	public String getAnonymous_export_view() {
		return anonymous_export_view;
	}

	public void setAnonymous_export_view(String anonymous_export_view) {
		this.anonymous_export_view = anonymous_export_view;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getWebresource() {
		return webresource;
	}

	public void setWebresource(String webresource) {
		this.webresource = webresource;
	}

	public String getRestrictions() {

		return restrictions;
	}

	public void setRestrictions(String restrictions) {

		this.restrictions = restrictions;
	}

	public String getStatus() {

		return status;
	}

	public void setStatus(String status) {

		this.status = status;
	}

	public String getOperations() {

		return operations;
	}

	public void setOperations(String operations) {

		this.operations = operations;
	}

	public String getBody() {

		return body;
	}

	public void setBody(String body) {

		this.body = body;
	}

	public String getContent() {

		return content;
	}

	public void setContent(String content) {

		this.content = content;
	}

	public String getLastUpdated() {

		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {

		this.lastUpdated = lastUpdated;
	}

	public String getPreviousVersion() {

		return previousVersion;
	}

	public void setPreviousVersion(String previousVersion) {

		this.previousVersion = previousVersion;
	}

	public String getContributors() {

		return contributors;
	}

	public void setContributors(String contributors) {

		this.contributors = contributors;
	}

	public String getNextVersion() {

		return nextVersion;
	}

	public void setNextVersion(String nextVersion) {

		this.nextVersion = nextVersion;
	}

	public String getIcon() {

		return icon;
	}

	public void setIcon(String icon) {

		this.icon = icon;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public String getHomepage() {

		return homepage;
	}

	public void setHomepage(String homepage) {

		this.homepage = homepage;
	}

	public String getHistory() {

		return history;
	}

	public void setHistory(String history) {

		this.history = history;
	}

	public String getContainer() {

		return container;
	}

	public void setContainer(String container) {

		this.container = container;
	}

	public String getAncestors() {

		return ancestors;
	}

	public void setAncestors(String ancestors) {

		this.ancestors = ancestors;
	}

	public String getChildren() {

		return children;
	}

	public void setChildren(String children) {

		this.children = children;
	}

	public String getDescendants() {

		return descendants;
	}

	public void setDescendants(String descendants) {

		this.descendants = descendants;
	}

	public String getSpace() {

		return space;
	}

	public void setSpace(String space) {

		this.space = space;
	}

	public String getVersion() {

		return version;
	}

	public void setVersion(String version) {

		this.version = version;
	}

	public String getMetadata() {

		return metadata;
	}

	public void setMetadata(String metadata) {

		this.metadata = metadata;
	}
}
