package com.kakaocorp.iamguide.dao;

import com.kakaocorp.iamguide.model.GuideTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GuideTagMapper {
    List<GuideTag> getGuideList(@Param("tag") String tag); // select tag with search bar
    List<GuideTag> getTags(@Param("doc_key") String doc_key);
    List<GuideTag> suggestTags(@Param("tag") String tag);
    void insertTag(@Param("insertTag") String sql);
    void insertTagging(@Param("insertTagging") String sql);
    void deleteTag(@Param("deleteTag") String sql);
}