package com.example.iam_guide_project.service;

import com.example.iam_guide_project.dao.Guide_UpdateMapper;
import com.example.iam_guide_project.model.Guide_Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class Guide_UpdateService {
    @Autowired
    Guide_UpdateMapper guide_updateMapper;

    public List<Guide_Update> getGuide_Update(String DOCUMENT_TITLE){
        return guide_updateMapper.getGuide_Update(DOCUMENT_TITLE);
    }
    public void setGuide_Update(String ADMIN_ID,String DOCUMENT_TITLE,String UPDATE_TYPE_CUD){
        /*Timestamp UPDATE_TIME = new Timestamp(new Date().getTime());
        System.out.println(UPDATE_TIME);*/
        guide_updateMapper.setGuide_Update(ADMIN_ID,DOCUMENT_TITLE,UPDATE_TYPE_CUD);
    }
}


