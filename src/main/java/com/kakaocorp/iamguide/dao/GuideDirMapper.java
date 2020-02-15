package com.kakaocorp.iamguide.dao;

import com.kakaocorp.iamguide.model.GuideDoc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Mapper
@Transactional(isolation = Isolation.REPEATABLE_READ)
public interface GuideDirMapper {
    void createGuideDir(GuideDoc guideDoc);

    void createGuideRootDir(GuideDoc guideDoc);

    void deleteGuideDir(@Param("key") String key);

    void updateGuideDir(GuideDoc guideDoc);
}
