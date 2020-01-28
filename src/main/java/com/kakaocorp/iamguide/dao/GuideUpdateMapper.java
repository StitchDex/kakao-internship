package com.kakaocorp.iamguide.dao;

import com.kakaocorp.iamguide.model.GuideUpdate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface GuideUpdateMapper {
    void setGuide_Update(@Param("ADMIN_ID")String id, @Param("DOCUMENT_TITLE")String title,
                          @Param("UPDATE_TYPE_CUD")String crud);
    List<GuideUpdate> getGuide_Update(@Param("title") String title);

}

