package com.kakaocorp.iamguide.model;

import com.daum.mis.remote.client.model.IdentityPersonInfo;

import java.io.Serializable;

public class UserInfo extends IdentityPersonInfo implements Serializable {
    public UserInfo(IdentityPersonInfo parent){
        super.setEmployeeNo(parent.getEmployeeNo());
        super.setAccountId(parent.getAccountId());
        super.setPersonName(parent.getPersonName());
        super.setDeptCode(parent.getDeptCode());
        super.setDeptName(parent.getDeptName());
    }
}
