package com.kakaocorp.iamguide.dao;

import com.kakaocorp.iamguide.model.Guide_Doc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface Guide_DocMapper {
    List<Guide_Doc> selectTreeDataForGuide();
    String selectGuide(@Param("id") String id); // doc_key
    void editGuide_Doc(@Param("id") String id, @Param("content") String text);
    void createGuide_Doc(@Param("parent") String parent,@Param("content") String content, @Param("text") String text,@Param("state") boolean state);
    void deleteGuide_Doc(@Param("key") String key);
    void updateGuide_Doc(@Param("key") String key,@Param("parent") String parent, @Param("text") String text,@Param("state") boolean state);
}
