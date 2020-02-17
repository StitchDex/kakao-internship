package com.kakaocorp.iamguide.dao;

import com.kakaocorp.iamguide.model.GuideDoc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Mapper
public interface GuideDocMapper {
    String selectMain(@Param("state") String state);

    List<GuideDoc> retrieveGuideTreeList();

    GuideDoc retrieveGuideDoc(@Param("id") String id); // doc_key

    void updateGuideDoc(@Param("id") String id, @Param("content") String text);

    void createGuideTree(GuideDoc guideDoc);

    void deleteGuideTree(@Param("key") String key);

    void updateGuideTree(GuideDoc guideDoc);
}
