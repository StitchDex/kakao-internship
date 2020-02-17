package com.kakaocorp.iamguide.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class GuideUpdate {
    private String admin;
    private String title;
    private String documentKey;
    private String updateType;
    private Timestamp UPDATE_TIME;

}
