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
    
    public String retrieveGuideUpdate(String DOCUMENT_KEY) {
        GuideUpdate temp = guideUpdateMapper.retrieveGuideUpdate(DOCUMENT_KEY);
        String retrieveUpdate = temp.getADMIN_ID() + " " + temp.getUPDATE_TYPE_CUD() + " at " + temp.getUPDATE_TIME();
        return retrieveUpdate;
    }

    public void createGuideUpdate(String ADMIN_ID, String DOCUMENT_KEY, String DOCUMENT_TITLE, String UPDATE_TYPE_CUD) {

        if (DOCUMENT_KEY == null || DOCUMENT_KEY.startsWith("DIR")) {
            DOCUMENT_KEY = null;
            guideUpdateMapper.createGuideUpdate(ADMIN_ID, DOCUMENT_KEY, DOCUMENT_TITLE, UPDATE_TYPE_CUD);
        } else if (DOCUMENT_KEY.startsWith("DOC")) {
            DOCUMENT_KEY = DOCUMENT_KEY.substring(3);
            guideUpdateMapper.createGuideUpdate(ADMIN_ID, DOCUMENT_KEY, DOCUMENT_TITLE, UPDATE_TYPE_CUD);
        } else {
            guideUpdateMapper.createGuideUpdate(ADMIN_ID, DOCUMENT_KEY, DOCUMENT_TITLE, UPDATE_TYPE_CUD);
        }
    }
}


