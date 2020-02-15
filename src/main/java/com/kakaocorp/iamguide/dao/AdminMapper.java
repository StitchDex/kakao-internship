package com.kakaocorp.iamguide.dao;

import com.kakaocorp.iamguide.model.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface AdminMapper {
    String isAdmin(@Param("adminId") String username) throws Exception;

    List<Admin> getAdminList() throws Exception;

    void createAdmin(List<Admin> admins) throws Exception;

    void deleteAdmin(List<Admin> admins) throws Exception;
}
