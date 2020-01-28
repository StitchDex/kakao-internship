package com.kakaocorp.iamguide.service;

import com.kakaocorp.iamguide.dao.UploadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadService {

    @Autowired
    UploadMapper uploadMapper;

    public void setImage(String url,String key,String user){
        uploadMapper.setImage(url);
    }
}
