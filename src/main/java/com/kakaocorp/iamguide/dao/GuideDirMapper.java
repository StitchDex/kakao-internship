package com.kakaocorp.iamguide.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
public interface GuideDirMapper {
    void createGuideDir(@Param("parent") String parent, @Param("text") String text, @Param("state") boolean state);

    void createGuideRootDir(@Param("text") String text, @Param("state") boolean state);

    void deleteGuideDir(@Param("key") String key);

    void updateGuideDir(@Param("key") String key, @Param("parent") String parent, @Param("text") String text, @Param("state") boolean state);
}
