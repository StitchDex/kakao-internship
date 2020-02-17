package com.kakaocorp.iamguide.service;

import com.kakaocorp.iamguide.dao.GuideDirMapper;
import com.kakaocorp.iamguide.model.GuideDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class GuideDirService {
    @Autowired
    private GuideDirMapper guideDirMapper;

    @CacheEvict(cacheNames = "treeCache", allEntries = true)
    public String createGuideDir(GuideDoc guideDoc) {

        if (guideDoc.getParent().equals("#")) {
            guideDirMapper.createGuideRootDir(guideDoc);
            return Integer.toString(guideDoc.getDIR_KEY());
        } else {
            guideDoc.setParent(guideDoc.getParent().substring(3));
            guideDirMapper.createGuideDir(guideDoc);
            return guideDoc.getId();
        }
    }

    @CacheEvict(cacheNames = "treeCache", allEntries = true)
    public void deleteGuideDir(String key) {
        key = key.substring(3);
        guideDirMapper.deleteGuideDir(key);
    }

    @CacheEvict(cacheNames = "treeCache", allEntries = true)
    public void updateGuideDir(GuideDoc guideDoc) {
        guideDoc.setId(guideDoc.getId().substring(3));
        if (guideDoc.getParent().length() > 1) {
            guideDoc.setParent(guideDoc.getParent().substring(3));
        } else {
            guideDoc.setParent(guideDoc.getId());
        }
        guideDirMapper.updateGuideDir(guideDoc);
    }
}
