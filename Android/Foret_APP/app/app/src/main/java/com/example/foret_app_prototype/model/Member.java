package com.example.foret_app_prototype.model;

import java.io.Serializable;
import java.util.List;

public class Member implements Serializable {
    private String id;
    private String pw;
    private String name;
    private String birth;
    private String email;
    private int image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

//    private int id;
//    private String name;
//    private String email;
    private String password;
    private String nickname;
//    private String birth;
    private String reg_date;
    private String photo;
    private List<String> tag;
    private List<String> region_si;
    private List<String> region_gu;
    private List<String> member_foret;
    private List<String> like_board;
    private List<String> like_comment;

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

//    public String getBirth() {
//        return birth;
//    }
//
//    public void setBirth(String birth) {
//        this.birth = birth;
//    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public List<String> getRegion_si() {
        return region_si;
    }

    public void setRegion_si(List<String> region_si) {
        this.region_si = region_si;
    }

    public List<String> getRegion_gu() {
        return region_gu;
    }

    public void setRegion_gu(List<String> region_gu) {
        this.region_gu = region_gu;
    }

    public List<String> getMember_foret() {
        return member_foret;
    }

    public void setMember_foret(List<String> member_foret) {
        this.member_foret = member_foret;
    }

    public List<String> getLike_board() {
        return like_board;
    }

    public void setLike_board(List<String> like_board) {
        this.like_board = like_board;
    }

    public List<String> getLike_comment() {
        return like_comment;
    }

    public void setLike_comment(List<String> like_comment) {
        this.like_comment = like_comment;
    }
}
