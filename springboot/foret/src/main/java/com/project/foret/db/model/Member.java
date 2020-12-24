package com.project.foret.db.model;

import org.apache.ibatis.type.Alias;

@Alias("member")
public class Member {
    private int id;
    private String name;
    private String email;
    private String password;
    private String nickname;
    private String birth;
    private String reg_date;
    private String device_token;

    private String filename;
    private String tag_name;
    private String region_si;
    private String region_gu;
    private int like_board;
    private int like_comment;
    private int foret_id;

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "\n" + ", name='" + getName() + "\n" + ", email='" + getEmail() + "\n"
                + ", password='" + getPassword() + "\n" + ", nickname='" + getNickname() + "\n" + ", birth='"
                + getBirth() + "\n" + ", reg_date='" + getReg_date() + "\n" + ", device_token='" + getDevice_token()
                + "\n" + ", filename='" + getFilename() + "\n" + ", tag_name='" + getTag_name() + "\n" + ", region_si='"
                + getRegion_si() + "\n" + ", region_gu='" + getRegion_gu() + "\n" + ", like_board='" + getLike_board()
                + "\n" + ", like_comment='" + getLike_comment() + "\n" + ", foret_id='" + getForet_id() + "'" + "}\n\n";
    }

    public Member() {

    }

    public Member(String email) {
        this.email = email;
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirth() {
        return this.birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getReg_date() {
        return this.reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getDevice_token() {
        return this.device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

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

    public int getLike_board() {
        return this.like_board;
    }

    public void setLike_board(int like_board) {
        this.like_board = like_board;
    }

    public int getLike_comment() {
        return this.like_comment;
    }

    public void setLike_comment(int like_comment) {
        this.like_comment = like_comment;
    }

    public int getForet_id() {
        return this.foret_id;
    }

    public void setForet_id(int foret_id) {
        this.foret_id = foret_id;
    }

}
