package com.kakaocorp.iamguide.model;

import lombok.Data;

@Data
public class User{
    private String user_id;
    private Integer user_key;
    private boolean isadmin = false;
}
