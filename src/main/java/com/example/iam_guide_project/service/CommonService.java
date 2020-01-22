package com.example.iam_guide_project.service;

import com.example.iam_guide_project.dao.CommonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {
    @Autowired
    CommonDAO commonDAO;

    public String isAdmin(String username) throws Exception{
        return commonDAO.isAdmin(username);
    }
}
