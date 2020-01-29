package com.kakaocorp.iamguide.service;

import com.daum.mis.remote.client.HelloIdentityServiceClient;
import com.kakaocorp.iamguide.dao.CommonMapper;
import com.kakaocorp.iamguide.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


@Service
public class CommonService {
    @Autowired
    CommonMapper commonMapper;

    public String isAdmin(String username) throws Exception{
        return commonMapper.isAdmin(username);
    }
    public List suggest(String accountId) throws Exception{
        HelloIdentityServiceClient client = HelloIdentityServiceClient.getHelloIdentityServiceClient();

        return client.getMembersByName(accountId);
    }
    public List<Admin> getAdminAll() throws Exception{
        return commonMapper.getAdminAll();
    }
    public void insertAdmin(List admins) throws Exception {
        commonMapper.insertAdmin(admins);
    }

    public void deleteAdmin(List admins) throws Exception {
        commonMapper.deleteAdmin(admins);
    }
}
