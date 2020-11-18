package com.example.foret.model;

public class Board {
    private int imageBoard;
    private String subject;
    private String content;

    public Board(int imageBoard, String subject, String content) {
        this.imageBoard = imageBoard;
        this.subject = subject;
        this.content = content;
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

    public int getImageBoard() {
        return imageBoard;
    }

    public void setImageBoard(int imageBoard) {
        this.imageBoard = imageBoard;
    }
}
