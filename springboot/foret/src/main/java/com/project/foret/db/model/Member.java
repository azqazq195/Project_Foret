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

    public Member() {

    }

    public Member(String email) {
        this.email = email;
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
