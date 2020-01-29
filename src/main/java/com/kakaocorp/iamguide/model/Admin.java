package com.kakaocorp.iamguide.model;

import lombok.Data;

@Data
public class Admin {
    private String adminEmpNo;
    private String adminAccountId;
    private String adminName;

    public String getAdminEmpNo() {
        return adminEmpNo;
    }

    public void setAdminEmpNo(String adminEmpNo) {
        this.adminEmpNo = adminEmpNo;
    }

    public String getAdminAccountId() {
        return adminAccountId;
    }

    public void setAdminAccountId(String adminAccountId) {
        this.adminAccountId = adminAccountId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
}
