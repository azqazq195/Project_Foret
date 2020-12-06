package com.example.foret_app_prototype.model;

import java.io.Serializable;
import java.util.List;

// ViewForetActivity 사용
public class ForetViewDTO implements Serializable {
    private int id;
    private int leader_id;
    private String name;
    private String introduce;
    private int max_member;
    private String photo;
    private String reg_date;
    private String rank;
    private List<String> foret_tag;
    private List<String> foret_region_si;
    private List<String> foret_region_gu;
    private List<Integer> member;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeader_id() {
        return leader_id;
    }

    public void setLeader_id(int leader_id) {
        this.leader_id = leader_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getMax_member() {
        return max_member;
    }

    public void setMax_member(int max_member) {
        this.max_member = max_member;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public List<String> getForet_tag() {
        return foret_tag;
    }

    public void setForet_tag(List<String> foret_tag) {
        this.foret_tag = foret_tag;
    }

    public List<String> getForet_region_si() {
        return foret_region_si;
    }

    public void setForet_region_si(List<String> foret_region_si) {
        this.foret_region_si = foret_region_si;
    }

    public List<String> getForet_region_gu() {
        return foret_region_gu;
    }

    public void setForet_region_gu(List<String> foret_region_gu) {
        this.foret_region_gu = foret_region_gu;
    }

    public List<Integer> getMember() {
        return member;
    }

    public void setMember(List<Integer> member) {
        this.member = member;
    }
}
