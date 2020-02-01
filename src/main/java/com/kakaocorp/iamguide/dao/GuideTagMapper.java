package com.kakaocorp.iamguide.dao;

import com.kakaocorp.iamguide.model.GuideDoc;
import com.kakaocorp.iamguide.model.GuideTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface GuideTagMapper {
    List<GuideDoc> retrieveGuideList(@Param("tag") String tag); // select tag with search bar
    List<GuideTag> retrieveGuideTagList(@Param("doc_key") String doc_key);
    List<GuideTag> suggestGuideTagList(@Param("tag") String tag);
    void createTag(@Param("insertTag") String sql);
    void createTagging(@Param("insertTagging") String sql);
    void deleteTag(@Param("deleteTag") String sql);
}