package eu.wajja.input.fetcher.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpacePermission {

    private String userName;
    private String groupName;
    private String type;
    
    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public String getGroupName() {

        return groupName;
    }

    public void setGroupName(String groupName) {

        this.groupName = groupName;
    }

    
    public String getType() {
    
        return type;
    }

    
    public void setType(String type) {
    
        this.type = type;
    }
    
   
}
