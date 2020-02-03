package com.kakaocorp.iamguide.dao;

import com.kakaocorp.iamguide.model.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface CommonMapper {
    String isAdmin(@Param("admin_id") String username) throws Exception;

    List<Admin> getAdminList() throws Exception;

    void createAdmin(List admins) throws Exception;

    void deleteAdmin(List admins) throws Exception;
}
