package com.project.foret.db.model;

import org.apache.ibatis.type.Alias;

@Alias("board")
public class Board {
    private int id;
    private int writer_id;
    private int foret_id;
    private int type;
    private int hit;
    private String subject;
    private String content;
    private String reg_date;
    private String edit_date;

    public Board() {
    }

    public Board(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWriter_id() {
        return this.writer_id;
    }

    public void setWriter_id(int writer_id) {
        this.writer_id = writer_id;
    }

    public int getForet_id() {
        return this.foret_id;
    }

    public void setForet_id(int foret_id) {
        this.foret_id = foret_id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHit() {
        return this.hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReg_date() {
        return this.reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getEdit_date() {
        return this.edit_date;
    }

    public void setEdit_date(String edit_date) {
        this.edit_date = edit_date;
    }

}
