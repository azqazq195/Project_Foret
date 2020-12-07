package com.example.foret_app_prototype.model;

public class ModelNotice {

    String writer, time, title,content;

    public ModelNotice() {
    }

    public ModelNotice(String writer, String time, String title, String content) {
        this.writer = writer;
        this.time = time;
        this.title = title;
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
