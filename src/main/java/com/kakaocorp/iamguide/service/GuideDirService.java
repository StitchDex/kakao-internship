package com.kakaocorp.iamguide.service;

import com.kakaocorp.iamguide.dao.GuideDirMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class GuideDirService {
    @Autowired
    private GuideDirMapper guideDirMapper;

    @CacheEvict(cacheNames ="treeCache",allEntries = true)
    public void createGuideDir(String parent, String text, int state, String order) {

        if (parent.equals("#")) {
            guideDirMapper.createGuideRootDir(text, state, Integer.parseInt(order));
        } else {
            parent=parent.substring(3);
            guideDirMapper.createGuideDir(parent, text, state, Integer.parseInt(order));
        }
    }
    @CacheEvict(cacheNames ="treeCache",allEntries = true)
    public void deleteGuideDir(String key) {
        key = key.substring(3);
        guideDirMapper.deleteGuideDir(key);
    }

    @CacheEvict(cacheNames ="treeCache",allEntries = true)
    public void updateGuideDir(String key, String parent, String text, int state, String order) {
        key = key.substring(3);
        if (parent.length() > 1) {
            parent = parent.substring(3);
        } else {
            parent = key;
        }
        guideDirMapper.updateGuideDir(key, parent, text, state, Integer.parseInt(order));
    }
}
