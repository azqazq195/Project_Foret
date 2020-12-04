package com.example.foret_app_prototype.model;

import java.io.Serializable;

public class Foret implements Serializable {
    private String rt;
    private int total;

    private int id;
    private String name;
    private String introduce;
    private int max_member;
    private String reg_date;
    private String foret_photo;
    private String[] foret_tag;
    private String[] member;
    private String leader;
    private String[] foret_region;

    // 임시
    private int foretImage;

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getForet_photo() {
        return foret_photo;
    }

    public void setForet_photo(String foret_photo) {
        this.foret_photo = foret_photo;
    }

    public String[] getForet_tag() {
        return foret_tag;
    }

    public void setForet_tag(String[] foret_tag) {
        this.foret_tag = foret_tag;
    }

    public String[] getMember() {
        return member;
    }

    public void setMember(String[] member) {
        this.member = member;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String[] getForet_region() {
        return foret_region;
    }

    public void setForet_region(String[] foret_region) {
        this.foret_region = foret_region;
    }

    public int getForetImage() {
        return foretImage;
    }

    public void setForetImage(int foretImage) {
        this.foretImage = foretImage;
    }
}
