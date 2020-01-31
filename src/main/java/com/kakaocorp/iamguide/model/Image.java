package com.kakaocorp.iamguide.model;

import lombok.Data;

@Data
public class Image {
    private String id;
    private String path;
    private String dockey;

    public Image(String path, String dockey) {
        this.path = path;
        this.dockey = dockey;
    }
}
