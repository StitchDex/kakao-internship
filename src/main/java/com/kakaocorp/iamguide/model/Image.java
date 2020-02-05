package com.kakaocorp.iamguide.model;

import lombok.Data;

@Data
public class Image {
    private String key;
    private String path;
    private String documentKey;
    private String uuid;

    public Image(String path, String documentKey) {
        this.path = path;
        this.documentKey = documentKey;
    }
}
