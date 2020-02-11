package com.kakaocorp.iamguide.dao;

import com.kakaocorp.iamguide.model.GuideUpdate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
public interface GuideUpdateMapper {

    void createGuideUpdate(@Param("ADMIN_ID") String id, @Param("DOCUMENT_KEY") String documentKey, @Param("DOCUMENT_TITLE") String title,
                           @Param("UPDATE_TYPE_CUD") String crud);

    GuideUpdate retrieveGuideUpdate(@Param("documentKey") String documentKey);


}

