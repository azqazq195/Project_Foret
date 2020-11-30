package com.example.foret_app_prototype.model;

import java.io.Serializable;

public class ForetBoard implements Serializable {
    private String rt;
    private int total;

    private int id;
    private String writer;
    private int type;
    private int hit;
    private String subject;
    private String content;
    private String reg_date;
    private String edit_date;
    private String member_photo;
    private String[] board_photo;
    private int like_count;
    private int comment_count;

    // 임시
    private int boradImage;
    private int memberImage;

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

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getEdit_date() {
        return edit_date;
    }

    public void setEdit_date(String edit_date) {
        this.edit_date = edit_date;
    }

    public String getMember_photo() {
        return member_photo;
    }

    public void setMember_photo(String member_photo) {
        this.member_photo = member_photo;
    }

    public String[] getBoard_photo() {
        return board_photo;
    }

    public void setBoard_photo(String[] board_photo) {
        this.board_photo = board_photo;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getBoradImage() {
        return boradImage;
    }

    public void setBoradImage(int boradImage) {
        this.boradImage = boradImage;
    }

    public int getMemberImage() {
        return memberImage;
    }

    public void setMemberImage(int memberImage) {
        this.memberImage = memberImage;
    }
}
