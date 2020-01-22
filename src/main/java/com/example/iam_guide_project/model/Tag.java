package com.example.iam_guide_project.model;

import lombok.Data;

@Data
public class Tag {
    private String tag;
    private int callCount = 0;
    private String doc_key;
    private String parent;

}
