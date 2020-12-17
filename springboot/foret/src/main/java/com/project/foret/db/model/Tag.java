package com.project.foret.db.model;

import org.apache.ibatis.type.Alias;

@Alias("tag")
public class Tag {
    private int id;
    private String tag_name;

    public Tag(int id, String tag_name) {
        this.id = id;
        this.tag_name = tag_name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag_name() {
        return this.tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

}
