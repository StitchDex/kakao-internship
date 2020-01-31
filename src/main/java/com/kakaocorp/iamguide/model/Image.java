package com.kakaocorp.iamguide.model;

public class Image {
    private String id;
    private String path;
    private String dockey;

    public Image(String path, String dockey) {
        this.path = path;
        this.dockey = dockey;
    }

    public String getDockey() {
        return dockey;
    }

    public void setDockey(String dockey) {
        this.dockey = dockey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
