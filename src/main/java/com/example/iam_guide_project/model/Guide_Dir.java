package com.example.iam_guide_project.model;

import lombok.Data;

@Data
public class Guide_Dir {
    private int id;
    private String parent;
    private String title;
    private boolean state;
}
