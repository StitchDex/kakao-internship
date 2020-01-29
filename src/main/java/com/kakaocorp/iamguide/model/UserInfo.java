package com.kakaocorp.iamguide.model;

import com.daum.mis.remote.client.model.IdentityPersonInfo;
import lombok.Data;

@Data
public class UserInfo extends IdentityPersonInfo {
    public UserInfo(IdentityPersonInfo parent){
        super.setEmployeeNo(parent.getEmployeeNo());
        super.setAccountId(parent.getAccountId());
        super.setPersonName(parent.getPersonName());
        super.setDeptCode(parent.getDeptCode());
        super.setDeptName(parent.getDeptName());
    }
}
