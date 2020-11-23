package com.example.foret.model;

import java.io.Serializable;

public class Foret implements Serializable {
    private String rt;
    private int total;

    private int group_no;
    private String group_name;
    private int group_currunt_member_count;
    private String group_leader;
    private String group_profile;
    private String group_photo;
    private String group_tag;
    private String group_region;
    private int group_max_member;
    private String group_date_issued;
    private String photo_name;

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

    public int getGroup_no() {
        return group_no;
    }

    public void setGroup_no(int group_no) {
        this.group_no = group_no;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public int getGroup_currunt_member_count() {
        return group_currunt_member_count;
    }

    public void setGroup_currunt_member_count(int group_currunt_member_count) {
        this.group_currunt_member_count = group_currunt_member_count;
    }

    public String getGroup_leader() {
        return group_leader;
    }

    public void setGroup_leader(String group_leader) {
        this.group_leader = group_leader;
    }

    public String getGroup_profile() {
        return group_profile;
    }

    public void setGroup_profile(String group_profile) {
        this.group_profile = group_profile;
    }

    public String getGroup_photo() {
        return group_photo;
    }

    public void setGroup_photo(String group_photo) {
        this.group_photo = group_photo;
    }

    public String getGroup_tag() {
        return group_tag;
    }

    public void setGroup_tag(String group_tag) {
        this.group_tag = group_tag;
    }

    public String getGroup_region() {
        return group_region;
    }

    public void setGroup_region(String group_region) {
        this.group_region = group_region;
    }

    public int getGroup_max_member() {
        return group_max_member;
    }

    public void setGroup_max_member(int group_max_member) {
        this.group_max_member = group_max_member;
    }

    public String getGroup_date_issued() {
        return group_date_issued;
    }

    public void setGroup_date_issued(String group_date_issued) {
        this.group_date_issued = group_date_issued;
    }

    public String getPhoto_name() {
        return photo_name;
    }

    public void setPhoto_name(String photo_name) {
        this.photo_name = photo_name;
    }
}
