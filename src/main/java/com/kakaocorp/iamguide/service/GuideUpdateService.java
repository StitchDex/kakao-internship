package com.kakaocorp.iamguide.service;

import com.kakaocorp.iamguide.dao.GuideUpdateMapper;
import com.kakaocorp.iamguide.model.GuideUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GuideUpdateService {

    @Autowired
    private GuideUpdateMapper guideUpdateMapper;

    public List<GuideUpdate> retrieveGuideUpdate(String DOCUMENT_TITLE){
        return guideUpdateMapper.retrieveGuideUpdate(DOCUMENT_TITLE);
    }
    public void createGuideUpdate(String ADMIN_ID, String DOCUMENT_TITLE, String UPDATE_TYPE_CUD){
        guideUpdateMapper.createGuideUpdate(ADMIN_ID,DOCUMENT_TITLE,UPDATE_TYPE_CUD);
    }
}


