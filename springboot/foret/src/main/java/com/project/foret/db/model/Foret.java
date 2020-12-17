package com.project.foret.db.model;

import org.apache.ibatis.type.Alias;

@Alias("foret")
public class Foret {
    private int id;
    private int leader_id;
    private String name;
    private String introduce;
    private int max_member;
    private String reg_date;

    public Foret() {

    }

    public Foret(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeader_id() {
        return this.leader_id;
    }

    public void setLeader_id(int leader_id) {
        this.leader_id = leader_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return this.introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getMax_member() {
        return this.max_member;
    }

    public void setMax_member(int max_member) {
        this.max_member = max_member;
    }

    public String getReg_date() {
        return this.reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

}
