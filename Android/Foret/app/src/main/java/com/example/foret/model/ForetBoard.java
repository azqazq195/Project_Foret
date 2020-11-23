package com.example.foret.model;

import java.io.Serializable;

public class ForetBoard implements Serializable {
    private String rt;
    private int total;

    private int group_no;
    private int board_no;
    private int board_type;
    private String board_writer;
    private String board_subject;
    private String board_content;
    private String board_photo_name;
    private int board_hit;
    private int board_like_count;
    private int board_comment_count;
    private String board_writed_date;
    private String board_edited_date;
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

    public int getBoard_no() {
        return board_no;
    }

    public void setBoard_no(int board_no) {
        this.board_no = board_no;
    }

    public int getBoard_type() {
        return board_type;
    }

    public void setBoard_type(int board_type) {
        this.board_type = board_type;
    }

    public String getBoard_writer() {
        return board_writer;
    }

    public void setBoard_writer(String board_writer) {
        this.board_writer = board_writer;
    }

    public String getBoard_subject() {
        return board_subject;
    }

    public void setBoard_subject(String board_subject) {
        this.board_subject = board_subject;
    }

    public String getBoard_content() {
        return board_content;
    }

    public void setBoard_content(String board_content) {
        this.board_content = board_content;
    }

    public String getBoard_photo_name() {
        return board_photo_name;
    }

    public void setBoard_photo_name(String board_photo_name) {
        this.board_photo_name = board_photo_name;
    }

    public int getBoard_hit() {
        return board_hit;
    }

    public void setBoard_hit(int board_hit) {
        this.board_hit = board_hit;
    }

    public int getBoard_like_count() {
        return board_like_count;
    }

    public void setBoard_like_count(int board_like_count) {
        this.board_like_count = board_like_count;
    }

    public int getBoard_comment_count() {
        return board_comment_count;
    }

    public void setBoard_comment_count(int board_comment_count) {
        this.board_comment_count = board_comment_count;
    }

    public String getBoard_writed_date() {
        return board_writed_date;
    }

    public void setBoard_writed_date(String board_writed_date) {
        this.board_writed_date = board_writed_date;
    }

    public String getBoard_edited_date() {
        return board_edited_date;
    }

    public void setBoard_edited_date(String board_edited_date) {
        this.board_edited_date = board_edited_date;
    }

    public String getPhoto_name() {
        return photo_name;
    }

    public void setPhoto_name(String photo_name) {
        this.photo_name = photo_name;
    }
}
