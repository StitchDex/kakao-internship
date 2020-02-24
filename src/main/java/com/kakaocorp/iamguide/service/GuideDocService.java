package com.kakaocorp.iamguide.service;

import com.kakaocorp.iamguide.IamUtils;
import com.kakaocorp.iamguide.dao.GuideDocMapper;
import com.kakaocorp.iamguide.model.GuideDoc;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GuideDocService {

    private final GuideDocMapper guideDocMapper;

    public GuideDocService(GuideDocMapper guideDocMapper) {
        this.guideDocMapper = guideDocMapper;
    }


    public String selectMain(String state) {
        return guideDocMapper.selectMain(state);
    }

    @Cacheable(cacheNames = "treeCache")
    public List<GuideDoc> retrieveGuideTreeList() {
        List<GuideDoc> guideTreeList = guideDocMapper.retrieveGuideTreeList();
        for (int i = 0; i < guideTreeList.size(); i++) {
            GuideDoc doc = guideTreeList.get(i);
            if (doc.getId().startsWith("DIR")) {
                doc.setType("DIR");
            } else {
                doc.setType("DOC");
            }
        }
        //root -> DIR0, parent =#
        return guideTreeList;
    }

    @CacheEvict(cacheNames = "treeCache", allEntries = true)
    public String createGuideTree(GuideDoc guideDoc) {
        if (guideDoc.getParent().length() < 1)
            guideDoc.setParent("0");
        guideDoc.setParent(guideDoc.getParent().substring(3));
        guideDoc.setContent(" ");
        guideDocMapper.createGuideTree(guideDoc);
        return guideDoc.getId();
    }

    @CacheEvict(cacheNames = "treeCache", allEntries = true)
    public void deleteGuideTree(String key) {
        key = key.substring(3);
        guideDocMapper.deleteGuideTree(key);
    }

    @CacheEvict(cacheNames = "treeCache", allEntries = true)
    public void updateGuideTree(GuideDoc guideDoc) {
        guideDoc.setId(IamUtils.keyParsing(guideDoc.getId()));
        if (guideDoc.getParent().length() < 1)
            guideDoc.setParent("0");
        guideDoc.setParent(guideDoc.getParent().substring(3));
        guideDocMapper.updateGuideTree(guideDoc);
    }

    public GuideDoc retrieveGuideDoc(String doc_key) {
        return guideDocMapper.retrieveGuideDoc(doc_key);
    }

    public void updateGuideDoc(String doc_key, String content) {
        guideDocMapper.updateGuideDoc(doc_key, content);
    }

}
