package com.kakaocorp.iamguide.model;

import lombok.Data;

@Data
public class Guide_Dir {
    private int id;
    private String parent;
    private String title;
    private boolean state;
}
