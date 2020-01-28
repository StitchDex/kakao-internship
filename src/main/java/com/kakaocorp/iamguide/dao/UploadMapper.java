package com.kakaocorp.iamguide.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UploadMapper {
    public void setImage(@Param("url") String url);
}
