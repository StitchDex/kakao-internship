package com.kakaocorp.iamguide.dao;

import com.kakaocorp.iamguide.model.Guide_Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface Guide_TagMapper {
    List<Guide_Tag> getGuide_Tag(@Param("tag") String tag); // select tag with search bar
    List<Guide_Tag> getTags(@Param("doc_key") String doc_key);
    List<Guide_Tag> suggestTags(@Param("tag") String tag);
    void insertTag(@Param("insertTag") String sql);
    void insertTagging(@Param("insertTagging") String sql);
    void deleteTag(@Param("deleteTag") String sql);
}