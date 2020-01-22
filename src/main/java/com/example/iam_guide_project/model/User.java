package com.example.iam_guide_project.model;

import lombok.Data;

@Data
public class User{
    private String user_id;
    private Integer user_key;
    private boolean isadmin = false;

    /*public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean isIsadmin() {
        return isadmin;
    }

    public void setIsadmin(boolean isadmin) {
        this.isadmin = isadmin;
    }

    public Integer getUser_key() {
        return user_key;
    }

    public void setUser_key(Integer user_key) {
        this.user_key = user_key;
    }*/
}
