package com.kakaocorp.iamguide.dao;

import com.kakaocorp.iamguide.model.GuideUpdate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
public interface GuideUpdateMapper {

    void createGuideUpdate(GuideUpdate guideUpdate);

    GuideUpdate retrieveGuideUpdate(@Param("documentKey") String documentKey);

}

