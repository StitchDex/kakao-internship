package com.kakaocorp.iamguide.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class GuideDoc implements Serializable {
    private String id;
    private String parent;
    private String parentTitle;
    private String text;
    private int orders;
    private String content;
    private String type;
    private int state;
    private ArrayList<String> tags;
    private int DIR_KEY; // for RootDIR

    public void setTags(List<GuideTag> tags) {
        this.tags = new ArrayList<>();
        for(GuideTag t : tags){
            this.tags.add(t.getTag());
        }
    }
}
