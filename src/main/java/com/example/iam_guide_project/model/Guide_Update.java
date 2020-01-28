package com.example.iam_guide_project.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Guide_Update {
    private int UPDATE_KEY;
    private String ADMIN_ID;
    private String DOCUMENT_TITLE;
    private Timestamp UPDATE_TIME;
    private String UPDATE_TYPE_CUD;
}
