package com.example.foret_app_prototype.model;

import java.io.Serializable;
import java.util.List;

public class ForetDTO implements Serializable {
    private String rt;
    private int total;

    private int foret_id;
    private String foret_name;
    private String introduce;
    private String foret_leader;
    private int max_member;
    private String foret_photo;
    private String reg_date;
    private List<String> foret_tag;
    private List<String> foret_member;
    private List<String> foret_region_si;
    private List<String> foret_region_gu;

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

    public int getForet_id() {
        return foret_id;
    }

    public void setForet_id(int foret_id) {
        this.foret_id = foret_id;
    }

    public String getForet_name() {
        return foret_name;
    }

    public void setForet_name(String foret_name) {
        this.foret_name = foret_name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getForet_leader() {
        return foret_leader;
    }

    public void setForet_leader(String foret_leader) {
        this.foret_leader = foret_leader;
    }

    public int getMax_member() {
        return max_member;
    }

    public void setMax_member(int max_member) {
        this.max_member = max_member;
    }

    public String getForet_photo() {
        if(foret_photo!=null){
            return "http://54.180.219.200:8085/storage/" + foret_photo;
        }else{
            return foret_photo;
        }
    }

    public void setForet_photo(String foret_photo) {
        this.foret_photo = foret_photo;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public List<String> getForet_tag() {
        return foret_tag;
    }

    public void setForet_tag(List<String> foret_tag) {
        this.foret_tag = foret_tag;
    }

    public List<String> getForet_member() {
        return foret_member;
    }

    public void setForet_member(List<String> foret_member) {
        this.foret_member = foret_member;
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

    @Override
    public String toString() {
        return  "id = " + foret_id +
                "\nname = " + foret_name +
                "\nintroduce = " + introduce +
                "\nleader_id = " + foret_leader +
                "\nmax_member = " + max_member +
                "\nphoto = " + foret_photo +
                "\nreg_date = " + reg_date +
                "\ntag = " + foret_tag +
                "\nregion_si = " + foret_region_si +
                "\nregion_gu = " + foret_region_gu + "\n";
    }

}