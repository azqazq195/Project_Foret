package com.example.foret_app_prototype.model;

import java.io.Serializable;
import java.util.List;

public class ForetBoardDTO implements Serializable {
    private String RT;

    private int id;
    private int foret_id;
    private int board_like;
    private int writer;
    private int type;
    private String subject;
    private String content;
    private int hit;
    private int board_comment;
    private String reg_date;
    private String edit_date;
    private List<String> foret_board_photo;

    public String getRT() {
        return RT;
    }

    public void setRT(String RT) {
        this.RT = RT;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getForet_id() {
        return foret_id;
    }

    public void setForet_id(int foret_id) {
        this.foret_id = foret_id;
    }

    public int getBoard_like() {
        return board_like;
    }

    public void setBoard_like(int board_like) {
        this.board_like = board_like;
    }

    public int getWriter() {
        return writer;
    }

    public void setWriter(int writer) {
        this.writer = writer;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getBoard_comment() {
        return board_comment;
    }

    public void setBoard_comment(int board_comment) {
        this.board_comment = board_comment;
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

    public List<String> getForet_board_photo() {
        return foret_board_photo;
    }

    public void setForet_board_photo(List<String> foret_board_photo) {
        this.foret_board_photo = foret_board_photo;
    }
}
