package com.example.iam_guide_project.service;

import com.example.iam_guide_project.dao.Guide_TagMapper;
import com.example.iam_guide_project.model.Guide_Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class Guide_TagService {
    @Autowired
    Guide_TagMapper guideTagMapper;

    public List<Guide_Tag> getGuide_Tag(String tag){
        return guideTagMapper.getGuide_Tag(tag);
    }
    public List<Guide_Tag> getTags(String doc_key) {return guideTagMapper.getTags(doc_key);}
    public List<Guide_Tag> suggestTags(String tag) {return guideTagMapper.suggestTags(tag);}

    public void updateTags(Object tags) {
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
            guideTagMapper.insertTag(insertTagSql);
            guideTagMapper.insertTagging(insertTaggingSql);
        }
        if(!delete.isEmpty())
            guideTagMapper.deleteTag(deleteTagSql);
    }
}