package com.kakaocorp.iamguide.service;

import com.kakaocorp.iamguide.dao.GuideDocMapper;
import com.kakaocorp.iamguide.model.GuideDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GuideDocService {
    private Logger logger = LoggerFactory.getLogger(GuideDocService.class);

    @Autowired
    GuideDocMapper guideDocMapper;

    public List<GuideDoc> retrieveGuideTreeList() {
        List<GuideDoc> guideTreeList = guideDocMapper.retrieveGuideTreeList();
        for (int i = 0; i < guideTreeList.size(); i++) {
            GuideDoc doc = guideTreeList.get(i);
            if(doc.getId().startsWith("DIR")){
                doc.setType("DIR");
            } else {
                doc.setType("DOC");
            }
        }
        //root -> DIR0, parent =#
        return guideTreeList;
    }

    public String createGuideTree(String parent, String text, boolean state,String order) {
        final String EMPTY_STRING = " ";
        if (parent.length() < 1)
            parent = "0";
        parent = parent.substring(3);
        GuideDoc guideDoc = new GuideDoc();
        guideDoc.setParent(parent);
        guideDoc.setText(EMPTY_STRING);
        guideDoc.setTitle(text);
        guideDoc.setState(true);
        guideDoc.setOrders(Integer.parseInt(order));
        guideDocMapper.createGuideTree(guideDoc);
        return guideDoc.getId();
    }

    public void deleteGuideTree(String key) {
        key = key.substring(3);
        guideDocMapper.deleteGuideTree(key);
    }

    public void updateGuideTree(String key, String parent, String text, boolean state,String order) {
        key = key.substring(3);
        if (parent.length() < 1)
            parent = "0";
        parent = parent.substring(3);
        logger.info("{},{},{},{},{}", key, parent, text, state,order);
        guideDocMapper.updateGuideTree(key, parent, text, state,Integer.parseInt(order));
    }

    public GuideDoc retrieveGuideDoc(String doc_key) {
        return guideDocMapper.retrieveGuideDoc(doc_key);
    }

    public void updateGuideDoc(String doc_key, String content) {
        guideDocMapper.updateGuideDoc(doc_key, content);
        //업데이트 내역, 태그 , 이미지
    }

}
