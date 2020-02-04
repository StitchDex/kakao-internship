package com.kakaocorp.iamguide.service;

import com.kakaocorp.iamguide.dao.GuideTagMapper;
import com.kakaocorp.iamguide.model.GuideDoc;
import com.kakaocorp.iamguide.model.GuideTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class GuideTagService {
    @Autowired
    private GuideTagMapper guideTagMapper;

    public List<GuideDoc> retrieveGuideList(String tag){return guideTagMapper.retrieveGuideList(tag);}//for search result
    public List<GuideTag> retrieveGuideTagList(String doc_key) {return guideTagMapper.retrieveGuideTagList(doc_key);}
    public List<GuideTag> suggestGuideTagList(String tag) {return guideTagMapper.suggestGuideTagList(tag);}

    public void updateGuideTag(Object tags) {
        HashMap temp = (HashMap) tags;
        String id = (String) temp.get("doc_key");
        ArrayList<String> insert = (ArrayList<String>) temp.get("insert");
        ArrayList<String> delete = (ArrayList<String>) temp.get("delete");

        if(!insert.isEmpty()) {
            guideTagMapper.createTag(insert);
            guideTagMapper.createTagging(id, insert);
        }
        if(!delete.isEmpty()) {
            guideTagMapper.deleteTagging(id, delete);
        }
        guideTagMapper.deleteTrash();
    }
}