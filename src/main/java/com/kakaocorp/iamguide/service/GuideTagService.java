package com.kakaocorp.iamguide.service;

import com.kakaocorp.iamguide.dao.GuideTagMapper;
import com.kakaocorp.iamguide.model.GuideDoc;
import com.kakaocorp.iamguide.model.GuideTag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class GuideTagService {

    private final GuideTagMapper guideTagMapper;

    public GuideTagService(GuideTagMapper guideTagMapper) {
        this.guideTagMapper = guideTagMapper;
    }


    public List<GuideDoc> retrieveGuideList(String tag, String pathName) {
        return guideTagMapper.retrieveGuideList(tag, pathName);
    }//for search result

    public List<GuideTag> retrieveGuideTagList(String doc_key) {
        return guideTagMapper.retrieveGuideTagList(doc_key);
    }

    @Cacheable(value = "tagCache", key = "#pathName")
    public List<GuideTag> suggestGuideTagList(String pathName) {
        return guideTagMapper.suggestGuideTagList(pathName);
    }

    @CacheEvict(value = "tagCache", allEntries = true)
    public void updateGuideTag(Object tags) {
        HashMap<String, Object> temp = (HashMap<String, Object>) tags;
        String id = (String) temp.get("doc_key");
        ArrayList<String> insert = (ArrayList<String>) temp.get("insert");
        ArrayList<String> delete = (ArrayList<String>) temp.get("delete");

        if (!insert.isEmpty()) {
            guideTagMapper.createTag(insert);
            guideTagMapper.createTagging(id, insert);
        }
        if (!delete.isEmpty()) {
            guideTagMapper.deleteTagging(id, delete);
        }

        List<String> trashTags = guideTagMapper.findTrash();
        if (trashTags.size() > 0) {
            for (String trashTag : trashTags) {
                deleteGuideTrash(trashTag);
            }
        }
    }

    public void deleteGuideTrash(String trashTag) {
        guideTagMapper.deleteTrash(trashTag);
    }
}