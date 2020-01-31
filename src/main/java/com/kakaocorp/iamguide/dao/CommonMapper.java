package com.kakaocorp.iamguide.dao;

import com.kakaocorp.iamguide.model.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface CommonMapper {
    public String isAdmin(@Param("admin_id") String username) throws Exception;
    public List<Admin> getAdminAll() throws Exception;
    public void insertAdmin(List admins) throws Exception;
    public void deleteAdmin(List admins) throws Exception;
}
