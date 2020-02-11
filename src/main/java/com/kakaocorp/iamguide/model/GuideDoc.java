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
    private String title;
    private String text;
    private String type;
    private boolean state;
    private ArrayList<String> tags;

    public void setTags(List<GuideTag> tags) {
        this.tags = new ArrayList<>();
        for(GuideTag t : tags){
            this.tags.add(t.getTag());
        }
    }
}
