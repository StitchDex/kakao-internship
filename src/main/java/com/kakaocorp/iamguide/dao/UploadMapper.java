package com.kakaocorp.iamguide.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface UploadMapper {
    public void createImage(@Param("url") String url);
    public void insertImaging(List insert ,@Param("docId") String docId);
    public void deleteImaging(List delete, @Param("docId") String docId);
    public void deleteTrash(List trash);
    public List findTrash();
}
