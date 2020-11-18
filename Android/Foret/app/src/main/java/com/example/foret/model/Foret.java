package com.example.foret.model;

public class Foret {
    private String subject;
    private String content;
    private int image;

    public Foret(String subject, String content, int image) {
        this.subject = subject;
        this.content = content;
        this.image = image;
    }

    public Foret() {
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
