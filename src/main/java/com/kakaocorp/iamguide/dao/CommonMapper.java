package com.kakaocorp.iamguide.dao;

import com.kakaocorp.iamguide.model.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface CommonMapper {
    public String isAdmin(@Param("admin_id") String username) throws Exception;
    public List<Admin> getAdminList() throws Exception;
    public void createAdmin(List admins) throws Exception;
    public void deleteAdmin(List admins) throws Exception;
}
