package com.kakaocorp.iamguide.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Mapper
@Transactional(isolation = Isolation.REPEATABLE_READ)
public interface GuideDirMapper {
    void createGuideDir(@Param("parent") String parent, @Param("text") String text, @Param("state") int state, @Param("order") int order);

    void createGuideRootDir(@Param("text") String text, @Param("state") int state,@Param("order") int order);

    void deleteGuideDir(@Param("key") String key);

    void updateGuideDir(@Param("key") String key, @Param("parent") String parent, @Param("text") String text, @Param("state") int state, @Param("order") int order);
}
