package com.example.iam_guide_project.service;

import com.example.iam_guide_project.dao.Guide_DocMapper;
import com.example.iam_guide_project.model.Guide_Doc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class Guide_DocService {
    private Logger logger = LoggerFactory.getLogger(Guide_DocService.class);

    @Autowired
    Guide_DocMapper guide_docMapper;


    public List<Guide_Doc> getGuideTreeList() {
        List<Guide_Doc> temp = guide_docMapper.selectTreeDataForGuide();
        for(int i=0;i<temp.size();i++){
            Guide_Doc doc = temp.get(i);
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
    public String setGuide_Doc(String doc_key) {
        return guide_docMapper.selectGuide(doc_key);
    }

    public void editGuide_Doc(String doc_key, String content){
        guide_docMapper.editGuide_Doc(doc_key,content);
    }

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
