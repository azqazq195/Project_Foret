package com.example.foret.model;

public class ModelUser {

    private String msg;         //메세지
    private String nickname;    //닉네임
    private String user_id;        //user_id
    private String date;        //날짜
    private String photoRoot;    //사진 저장 파일 위치
    private String email;         //이메일

    private String uid, onlineStatus,typingTo;

    private String listlogined_date;

    public ModelUser() {
    }

    public ModelUser(String msg, String nickname, String user_id, String date, String photoRoot, String email, String uid, String onlineStatus, String typingTo, String listlogined_date) {
        this.msg = msg;
        this.nickname = nickname;
        this.user_id = user_id;
        this.date = date;
        this.photoRoot = photoRoot;
        this.email = email;
        this.uid = uid;
        this.onlineStatus = onlineStatus;
        this.typingTo = typingTo;
        this.listlogined_date = listlogined_date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhotoRoot() {
        return photoRoot;
    }

    public void setPhotoRoot(String photoRoot) {
        this.photoRoot = photoRoot;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getTypingTo() {
        return typingTo;
    }

    public void setTypingTo(String typingTo) {
        this.typingTo = typingTo;
    }

    public String getListlogined_date() {
        return listlogined_date;
    }

    public void setListlogined_date(String listlogined_date) {
        this.listlogined_date = listlogined_date;
    }
}


