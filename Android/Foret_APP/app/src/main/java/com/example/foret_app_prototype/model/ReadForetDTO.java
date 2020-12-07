package com.example.foret_app_prototype.model;

import java.io.Serializable;
import java.util.List;

// ReadForetBoardActivity 에서 사용
public class ReadForetDTO implements Serializable {
    private int id;
    private int writer;
    private int foret_id;
    private String subject;
    private String content;
    private String writer_photo;
    private String reg_date;
    private String edit_date;
    private String writer_nickname;
    private List<String> photo;
    private int board_like;
    private int type;
    private int hit;
    private int board_comment;
    private boolean like;

    private List<FBCommentDTO> fbCommentDTOList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWriter() {
        return writer;
    }

    public void setWriter(int writer) {
        this.writer = writer;
    }

    public int getForet_id() {
        return foret_id;
    }

    public void setForet_id(int foret_id) {
        this.foret_id = foret_id;
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

    public String getWriter_photo() {
        return writer_photo;
    }

    public void setWriter_photo(String writer_photo) {
        this.writer_photo = writer_photo;
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

    public String getWriter_nickname() {
        return writer_nickname;
    }

    public void setWriter_nickname(String writer_nickname) {
        this.writer_nickname = writer_nickname;
    }

    public List<String> getPhoto() {
        return photo;
    }

    public void setPhoto(List<String> photo) {
        this.photo = photo;
    }

    public int getBoard_like() {
        return board_like;
    }

    public void setBoard_like(int board_like) {
        this.board_like = board_like;
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

    public int getBoard_comment() {
        return board_comment;
    }

    public void setBoard_comment(int board_comment) {
        this.board_comment = board_comment;
    }

    public List<FBCommentDTO> getFbCommentDTOList() {
        return fbCommentDTOList;
    }

    public void setFbCommentDTOList(List<FBCommentDTO> fbCommentDTOList) {
        this.fbCommentDTOList = fbCommentDTOList;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
