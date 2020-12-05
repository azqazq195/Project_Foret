package com.example.foret_app_prototype.model;

import java.io.Serializable;

public class HomeForetBoardDTO  implements Serializable {
    private int id;
    private String reg_date;
    private String edit_date;
    private String subject;
    private String content;
    private int hit;
    private int writer;
    private int board_like;
    private int board_comment;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public int getWriter() {
        return writer;
    }

    public void setWriter(int writer) {
        this.writer = writer;
    }

    public int getBoard_like() {
        return board_like;
    }

    public void setBoard_like(int board_like) {
        this.board_like = board_like;
    }

    public int getBoard_comment() {
        return board_comment;
    }

    public void setBoard_comment(int board_comment) {
        this.board_comment = board_comment;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}