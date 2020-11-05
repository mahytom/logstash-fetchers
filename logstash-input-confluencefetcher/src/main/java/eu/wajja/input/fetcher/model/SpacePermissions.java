package eu.wajja.input.fetcher.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpacePermissions {

    private String type;
    private List<SpacePermission> spacePermissions = new ArrayList<>();

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public List<SpacePermission> getSpacePermissions() {

        return spacePermissions;
    }

    public void setSpacePermissions(List<SpacePermission> spacePermissions) {

        this.spacePermissions = spacePermissions;
    }

}
