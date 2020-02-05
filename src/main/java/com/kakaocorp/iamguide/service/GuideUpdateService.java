package com.kakaocorp.iamguide.service;

import com.kakaocorp.iamguide.dao.GuideUpdateMapper;
import com.kakaocorp.iamguide.model.GuideUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Service
public class GuideUpdateService {

    @Autowired
    private GuideUpdateMapper guideUpdateMapper;

    public String retrieveGuideUpdate(String DOCUMENT_TITLE){
        GuideUpdate temp = guideUpdateMapper.retrieveGuideUpdate(DOCUMENT_TITLE);
        String retrieveUpdate = temp.getADMIN_ID()+" " + temp.getUPDATE_TYPE_CUD()+ " at "+ temp.getUPDATE_TIME();
        return retrieveUpdate;
    }
    public void createGuideUpdate(String ADMIN_ID, String DOCUMENT_TITLE, String UPDATE_TYPE_CUD){
        guideUpdateMapper.createGuideUpdate(ADMIN_ID,DOCUMENT_TITLE,UPDATE_TYPE_CUD);
    }
}


