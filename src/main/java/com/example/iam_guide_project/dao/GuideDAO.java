package com.example.iam_guide_project.dao;

import com.example.iam_guide_project.model.Guide_Doc;
import com.example.iam_guide_project.model.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.codehaus.groovy.util.ListHashMap;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface GuideDAO {
    List<Guide_Doc> selectTreeDataForGuide();
    String selectGuide(@Param("id") String id); // doc_key
    List<Tag> selectTag(@Param("tag") String tag); // select tag with search bar
}
