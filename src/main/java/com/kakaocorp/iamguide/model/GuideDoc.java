package com.kakaocorp.iamguide.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class GuideDoc implements Serializable {
    private String id;
    private String parent;
    private String title;
    private String text;
    private String type;
    private boolean state;
}
