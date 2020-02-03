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
        String doc_key = (String) temp.get("doc_key");
        ArrayList<String> insert = (ArrayList<String>) temp.get("insert");
        ArrayList<String> delete = (ArrayList<String>) temp.get("delete");
        String insertTagSql = "INSERT IGNORE INTO GUIDE_TAG VALUES";
        String insertTaggingSql = "INSERT INTO GUIDE_TAGGING(DOCUMENT_KEY, TAG) VALUES";
        String deleteTagSql = "DELETE FROM GUIDE_TAGGING WHERE DOCUMENT_KEY=" + doc_key + " AND TAG IN (";

        for(int i = 0; i < insert.size(); i++)
        {
            if(i != insert.size()-1)
            {
                insertTagSql += String.format("('%s'),",insert.get(i));
                insertTaggingSql += String.format("('%s','%s'),", doc_key, insert.get(i));
            }
            else
            {
                insertTagSql += String.format("('%s');",insert.get(i));
                insertTaggingSql += String.format("('%s','%s');", doc_key, insert.get(i));
            }
        }

        for(int i = 0; i < delete.size(); i++)
        {
            if(i != delete.size()-1) deleteTagSql += String.format("'%s',",delete.get(i));
            else deleteTagSql += String.format("'%s');",delete.get(i));
        }

        if(!insert.isEmpty())
        {
            guideTagMapper.createTag(insertTagSql);
            guideTagMapper.createTagging(insertTaggingSql);
        }
        if(!delete.isEmpty())
            guideTagMapper.deleteTag(deleteTagSql);
    }
}