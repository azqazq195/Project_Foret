package com.example.foret_app_prototype.model;

import java.io.Serializable;

public class ForetBoardComment implements Serializable {
    private int id;
    private int board_id;
    private int writer_image;
    private String writer;
    private String content;
    private String reg_date;
    private int group_no;
    private int group_seq;

    public ForetBoardComment() {
    }

    public ForetBoardComment(String content, String writer, String reg_date, int writer_image) {
        this.writer_image = writer_image;
        this.content = content;
        this.writer = writer;
        this.reg_date = reg_date;
    }

    public int getWriter_image() {
        return writer_image;
    }

    public void setWriter_image(int writer_image) {
        this.writer_image = writer_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBoard_id() {
        return board_id;
    }

    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
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

    public int getGroup_no() {
        return group_no;
    }

    public void setGroup_no(int group_no) {
        this.group_no = group_no;
    }

    public int getGroup_seq() {
        return group_seq;
    }

    public void setGroup_seq(int group_seq) {
        this.group_seq = group_seq;
    }
}
