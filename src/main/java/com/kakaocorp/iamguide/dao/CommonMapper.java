package com.kakaocorp.iamguide.dao;

import com.kakaocorp.iamguide.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommonMapper {
    public String isAdmin(@Param("admin_id") String username) throws Exception;
    public List<User> suggest(@Param("admin_id") String username) throws Exception;
    public List<User> getAdminAll() throws Exception;
    public void insertAdmin(@Param("insertQuery") String q) throws Exception;
    public void deleteAdmin(@Param("deleteQuery") String q) throws Exception;
}
