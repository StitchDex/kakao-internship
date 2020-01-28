package com.example.iam_guide_project.service;

import com.example.iam_guide_project.dao.CommonMapper;
import com.example.iam_guide_project.model.User;
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
    public List<User> suggest(String username) throws Exception{
        return commonMapper.suggest(username);
    }
    public List<User> getAdminAll() throws Exception{
        return commonMapper.getAdminAll();
    }
    public void insertAdmin(List<Object> admins) throws Exception {
        String sql = "INSERT INTO ADMIN(ADMIN_ID) VALUES ";
        for(int i = 0; i < admins.size(); i++)
        {
            HashMap map = (HashMap) admins.get(i);
            if(i != admins.size()-1) sql += "('" + map.get("admin") + "'),";
            else sql += "('" + map.get("admin") + "')";
        }
        commonMapper.insertAdmin(sql);
    }

    public void deleteAdmin(List<Object> admins) throws Exception {
        String sql = "DELETE FROM ADMIN WHERE ADMIN_ID IN (";
        for(int i = 0; i < admins.size(); i++)
        {
            HashMap map = (HashMap) admins.get(i);
            if(i != admins.size()-1) sql += "'" + map.get("admin") + "',";
            else sql += "'" + map.get("admin") + "')";
        }
        commonMapper.deleteAdmin(sql);
    }
}
