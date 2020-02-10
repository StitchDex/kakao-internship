package com.kakaocorp.iamguide.service;

import com.daum.mis.remote.client.HelloIdentityServiceClient;
import com.kakaocorp.iamguide.controller.CommonController;
import com.kakaocorp.iamguide.dao.CommonMapper;
import com.kakaocorp.iamguide.model.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdminService {
    private Logger logger = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    private CommonMapper commonMapper;

    public String isAdmin(String username) throws Exception {
        return commonMapper.isAdmin(username);
    }

    @Cacheable(cacheNames = "misCache", key = "#accountId")
    public List suggest(String accountId) throws Exception {
        HelloIdentityServiceClient client = HelloIdentityServiceClient.getHelloIdentityServiceClient();
        return client.getMembersByName(accountId);
    }

    public List<Admin> getAdminList() throws Exception {
        return commonMapper.getAdminList();
    }

    public void createAdmin(List admins) throws Exception {
        commonMapper.createAdmin(admins);
    }

    public void deleteAdmin(List admins) throws Exception {
        commonMapper.deleteAdmin(admins);
    }
}
