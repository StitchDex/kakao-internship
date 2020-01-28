package com.kakaocorp.iamguide.model;

import lombok.Data;

@Data
public class GuideDoc {
    private String id;
    private String parent;
    private String text;
    private String type;
    private boolean state;
}
