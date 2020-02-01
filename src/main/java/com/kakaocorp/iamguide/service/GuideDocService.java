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
    private GuideDocMapper guideDocMapper;

    @Cacheable(cacheNames = "TREE_LOAD")
    public List<GuideDoc> retrieveGuideTreeList() {
        List<GuideDoc> guideTreeList = guideDocMapper.retrieveGuideTreeList();

        for(int i=0;i<guideTreeList.size();i++){
            GuideDoc doc = guideTreeList.get(i);
            if(doc.getId().startsWith("DIR")){
                doc.setType("DIR");
            }
            else{
                doc.setType("DOC");
            }
        }
        //root -> DIR0, parent =#
        return guideTreeList;
    }

    @CacheEvict(cacheNames ="TREE_LOAD",allEntries = true)
    public void createGuideTree(String parent, String text, boolean state){
        if(parent.length()<1)
            parent = "0";
        parent = parent.substring(3);
        String content = " ";
        guideDocMapper.createGuideTree(parent,content,text,state);
    }

    @CacheEvict(cacheNames ="TREE_LOAD",allEntries = true)
    public void deleteGuideTree(String key){
        key=key.substring(3);
        guideDocMapper.deleteGuideTree(key);
    }

    @CacheEvict(cacheNames ="TreeLoad",allEntries = true)
    public void updateGuideTree(String key, String parent, String text, boolean state){
        key=key.substring(3);
        if(parent.length()<1)
            parent = "0";
        parent = parent.substring(3);
        guideDocMapper.updateGuideDoc(key,parent,text,state);
    }

    public GuideDoc retrieveGuideDoc(String doc_key) {
        return guideDocMapper.retrieveGuideDoc(doc_key);
    }

    public void updateGuideDoc(String doc_key, String content){ guideDocMapper.updateGuideDoc(doc_key,content);}


}
