package com.kakaocorp.iamguide.service;

import com.kakaocorp.iamguide.dao.Guide_DirMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Guide_DirService {
    @Autowired
    Guide_DirMapper guide_dirMapper;

    public void createGuide_Dir(String parent, String text,boolean state){
        if(parent.length()>1)
        parent = parent.substring(3);
        String content = " ";
        guide_dirMapper.createGuide_Dir(parent,text,state);
    }
    public void deleteGuide_Dir(String key){
        key=key.substring(3);
        guide_dirMapper.deleteGuide_Dir(key);
    }
    public void updateGuide_Dir(String key,String parent, String text,boolean state){
        key=key.substring(3);
        if(parent.length()>1)
            parent = parent.substring(3);
        guide_dirMapper.updateGuide_Dir(key,parent,text,state);
    }
}
