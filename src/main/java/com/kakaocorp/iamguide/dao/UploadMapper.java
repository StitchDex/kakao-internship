package com.kakaocorp.iamguide.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface UploadMapper {
    void createImage(@Param("url") String url);

    void insertImaging(List insert, @Param("docId") String docId);

    void deleteImaging(List delete, @Param("docId") String docId);

    void deleteTrash(List trash);

    List findTrash();
}
