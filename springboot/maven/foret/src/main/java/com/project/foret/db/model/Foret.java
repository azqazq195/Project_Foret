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

    private String filename;
    private String tag_name;
    private String region_si;
    private String region_gu;
    private int member_id;
    private int cnt;

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTag_name() {
        return this.tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getRegion_si() {
        return this.region_si;
    }

    public void setRegion_si(String region_si) {
        this.region_si = region_si;
    }

    public String getRegion_gu() {
        return this.region_gu;
    }

    public void setRegion_gu(String region_gu) {
        this.region_gu = region_gu;
    }

    public int getMember_id() {
        return this.member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public int getCnt() {
        return this.cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

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
