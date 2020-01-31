package com.kakaocorp.iamguide.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface UploadMapper {
    public void setImage(@Param("url") String url);
    public void insertImageUrl(List insert);
    public void insertImaging(List insert);
}
