package com.kakaocorp.iamguide.service;

import com.kakaocorp.iamguide.dao.GuideDocMapper;
import com.kakaocorp.iamguide.model.GuideDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GuideDocService {
    private Logger logger = LoggerFactory.getLogger(GuideDocService.class);

    @Autowired
    GuideDocMapper guide_docMapper;


    public List<GuideDoc> getGuideTreeList() {
        List<GuideDoc> temp = guide_docMapper.selectTreeDataForGuide();
        for(int i=0;i<temp.size();i++){
            GuideDoc doc = temp.get(i);
            if(doc.getId().startsWith("DIR")){
                doc.setType("DIR");
            }
            else{
                doc.setType("DOC");
            }
        }
        //root -> DIR0, parent =#
        return temp;
    }
    public GuideDoc getGuideDoc(String doc_key) {
        return guide_docMapper.getGuideDoc(doc_key);
    }

    public void editGuide_Doc(String doc_key, String content){ guide_docMapper.editGuide_Doc(doc_key,content);}

    public void createGuide_Doc(String parent, String text,boolean state){
        if(parent.length()<1)
            parent = "0";
            parent = parent.substring(3);
        String content = " ";
        guide_docMapper.createGuide_Doc(parent,content,text,state);
    }
    public void deleteGuide_Doc(String key){
        key=key.substring(3);
        guide_docMapper.deleteGuide_Doc(key);
    }
    public void updateGuide_Doc(String key,String parent, String text,boolean state){
        key=key.substring(3);
        if(parent.length()<1)
            parent = "0";
        parent = parent.substring(3);
        guide_docMapper.updateGuide_Doc(key,parent,text,state);
    }

}
