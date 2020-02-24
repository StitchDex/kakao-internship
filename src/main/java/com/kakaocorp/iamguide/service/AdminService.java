package com.kakaocorp.iamguide.service;

import com.daum.mis.remote.client.HelloIdentityServiceClient;
import com.daum.mis.remote.client.model.IdentityPersonInfo;
import com.kakaocorp.iamguide.controller.CommonController;
import com.kakaocorp.iamguide.dao.AdminMapper;
import com.kakaocorp.iamguide.model.Admin;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdminService {

    private final AdminMapper adminMapper;

    public AdminService(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }


    public String isAdmin(String username) throws Exception {
        return adminMapper.isAdmin(username);
    }

    @Cacheable(cacheNames = "misCache", key = "#accountId")
    public List<IdentityPersonInfo> suggest(String accountId) throws Exception {
        HelloIdentityServiceClient client = HelloIdentityServiceClient.getHelloIdentityServiceClient();
        return client.getMembersByName(accountId);
    }

    public List<Admin> getAdminList() throws Exception {
        return adminMapper.getAdminList();
    }

    public void createAdmin(List<Admin> admins) throws Exception {
        adminMapper.createAdmin(admins);
    }

    public void deleteAdmin(List<Admin> admins) throws Exception {
        adminMapper.deleteAdmin(admins);
    }
}
