package com.kakaocorp.iamguide.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class GuideUpdate {
    private int updateKey;
    private String adminId;
    private String documentTitle;
    private Timestamp updateTime;
    private String updateTypeCUD;
}
