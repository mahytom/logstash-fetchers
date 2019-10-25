package eu.wajja.input.fetcher.model;

import java.util.ArrayList;
import java.util.List;

public class Result {

    private String id;
    private String type;
    private String title;
    private String status;

    private History history;
    private Space space;
    private Version version;
    private Extensions extensions;
    private Links _links;
    private Expandable _expandable;

    private Body body;
    private List<Ancestor> ancestors;

    public Result() {}

    public Result(String id, String title, String content, Integer versionNumber) {

        this.id = id;
        this.title = title;
        this.body = new Body(content);
        this.version = new Version(versionNumber);
        this._expandable = new Expandable(id);

        this.ancestors = new ArrayList<>();
    }

    public Links get_links() {

        return _links;
    }

    public void set_links(Links _links) {

        this._links = _links;
    }

    public Extensions getExtensions() {

        return extensions;
    }

    public void setExtensions(Extensions extensions) {

        this.extensions = extensions;
    }

    public History getHistory() {

        return history;
    }

    public void setHistory(History history) {

        this.history = history;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public List<Ancestor> getAncestors() {

        return ancestors;
    }

    public void setAncestors(List<Ancestor> ancestors) {

        this.ancestors = ancestors;
    }

    public Expandable get_expandable() {

        return _expandable;
    }

    public void set_expandable(Expandable _expandable) {

        this._expandable = _expandable;
    }

    public Version getVersion() {

        return version;
    }

    public void setVersion(Version version) {

        this.version = version;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public Space getSpace() {

        return space;
    }

    public void setSpace(Space space) {

        this.space = space;
    }

    public Body getBody() {

        return body;
    }

    public void setBody(Body body) {

        this.body = body;
    }

}
