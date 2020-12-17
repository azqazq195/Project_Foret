package com.project.foret.db.model;

import org.apache.ibatis.type.Alias;

@Alias("comment")
public class Comment {
    private int id;
    private int board_id;
    private int writer_id;
    private int group_id;
    private String content;
    private String reg_date;

    public Comment() {

    }

    public Comment(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBoard_id() {
        return this.board_id;
    }

    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }

    public int getWriter_id() {
        return this.writer_id;
    }

    public void setWriter_id(int writer_id) {
        this.writer_id = writer_id;
    }

    public int getGroup_id() {
        return this.group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
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

}
