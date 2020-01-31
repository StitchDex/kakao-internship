package com.kakaocorp.iamguide.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class GuideDir implements Serializable {
    private int id;
    private String parent;
    private String title;
    private boolean state;
}
