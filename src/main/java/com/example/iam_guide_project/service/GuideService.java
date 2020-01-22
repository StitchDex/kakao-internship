package com.example.iam_guide_project.service;

import com.example.iam_guide_project.dao.CommonDAO;
import com.example.iam_guide_project.dao.GuideDAO;
import com.example.iam_guide_project.model.Guide_Doc;
import com.example.iam_guide_project.model.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class GuideService {
    private Logger logger = LoggerFactory.getLogger(GuideService.class);
    @Autowired
    CommonDAO commonDAO;
    @Autowired
    GuideDAO guideDAO;

    public List<Guide_Doc> getGuideTreeList() {
        return guideDAO.selectTreeDataForGuide();
    }

    public String readGuide_Doc(String doc_key) {
        String content = guideDAO.selectGuide(doc_key);
        return content;
    }

    public List<Tag> getTagInfo(String tag){
        return guideDAO.selectTag(tag);
    }
}
