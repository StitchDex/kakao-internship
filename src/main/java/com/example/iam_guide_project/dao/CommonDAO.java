package com.example.iam_guide_project.dao;

import com.example.iam_guide_project.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Mapper
@Repository
public interface CommonDAO  {
    public String isAdmin(@Param("admin_id") String username) throws Exception;
    public List<User> suggest(@Param("admin_id") String username) throws Exception;
}
