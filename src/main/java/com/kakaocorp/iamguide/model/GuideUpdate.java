package com.kakaocorp.iamguide.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class GuideUpdate {
    private int UPDATE_KEY;
    private String ADMIN_ID;
    private String DOCUMENT_TITLE;
    private Timestamp UPDATE_TIME;
    private String UPDATE_TYPE_CUD;
}
