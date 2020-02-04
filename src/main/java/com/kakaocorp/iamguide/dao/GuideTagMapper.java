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
    void createTagging(@Param("doc_key") String doc_key, List insert);
    void deleteTagging(@Param("doc_key") String doc_key, List delete);
    void createTag(List tag);
    void deleteTrash();
}