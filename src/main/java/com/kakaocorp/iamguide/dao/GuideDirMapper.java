package com.kakaocorp.iamguide.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GuideDirMapper {
    void createGuide_Dir(@Param("parent") String parent, @Param("text") String text,@Param("state") boolean state);
    void deleteGuide_Dir(@Param("key") String key);
    void updateGuide_Dir(@Param("key") String key,@Param("parent") String parent, @Param("text") String text,@Param("state") boolean state);
}
