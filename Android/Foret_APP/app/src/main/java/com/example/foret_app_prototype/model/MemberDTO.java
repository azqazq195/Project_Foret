package com.example.foret_app_prototype.model;

import java.io.Serializable;
import java.util.List;

public class MemberDTO implements Serializable {
    private int id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String birth;
    private String photo;
    private String reg_date;
    private String deviceToken;
    private List<String> tag;
    private List<String> region_si;
    private List<String> region_gu;
    private List<String> like_board;
    private List<String> like_comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhoto() {
        return "http://34.72.240.24:8085/foret/storage/"+photo;
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

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
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

    @Override
    public String toString() {
        return "MemberDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", birth='" + birth + '\'' +
                ", photo='" + photo + '\'' +
                ", reg_date='" + reg_date + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                ", tag=" + tag +
                ", region_si=" + region_si +
                ", region_gu=" + region_gu +
                ", like_board=" + like_board +
                ", like_comment=" + like_comment +
                '}';
    }
}
