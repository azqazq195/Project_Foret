package com.project.foret.db.model;

import org.apache.ibatis.type.Alias;

@Alias("link")
public class Link {
    private int member_id;
    private int reference_id;

    public Link() {
    }

    public Link(int member_id, int reference_id) {
        this.member_id = member_id;
        this.reference_id = reference_id;
    }

    public int getMember_id() {
        return this.member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public int getReference_id() {
        return this.reference_id;
    }

    public void setReference_id(int reference_id) {
        this.reference_id = reference_id;
    }

}
