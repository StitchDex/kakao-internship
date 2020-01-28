package com.example.iam_guide_project.dao;

import com.example.iam_guide_project.model.Guide_Update;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface Guide_UpdateMapper {
    void setGuide_Update(@Param("ADMIN_ID")String id, @Param("DOCUMENT_TITLE")String title,
                          @Param("UPDATE_TYPE_CUD")String crud);
    List<Guide_Update> getGuide_Update(@Param("title") String title);

}

