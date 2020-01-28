package com.kakaocorp.iamguide.model;

import lombok.Data;

@Data
public class GuideDir {
    private int id;
    private String parent;
    private String title;
    private boolean state;
}
