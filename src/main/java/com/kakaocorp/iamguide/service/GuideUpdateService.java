package com.kakaocorp.iamguide.service;

import com.kakaocorp.iamguide.dao.GuideUpdateMapper;
import com.kakaocorp.iamguide.model.GuideUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GuideUpdateService {
    @Autowired
    GuideUpdateMapper guide_updateMapper;

    public List<GuideUpdate> getGuide_Update(String DOCUMENT_TITLE){
        return guide_updateMapper.getGuide_Update(DOCUMENT_TITLE);
    }
    public void setGuide_Update(String ADMIN_ID,String DOCUMENT_TITLE,String UPDATE_TYPE_CUD){
        /*Timestamp UPDATE_TIME = new Timestamp(new Date().getTime());
        System.out.println(UPDATE_TIME);*/
        guide_updateMapper.setGuide_Update(ADMIN_ID,DOCUMENT_TITLE,UPDATE_TYPE_CUD);
    }
}


