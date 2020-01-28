package com.kakaocorp.iamguide.model;

import lombok.Data;

@Data
public class Guide_Tag {
    private String tag;
    private int callCount = 0;
    private String doc_key;
    private String parent;

}
