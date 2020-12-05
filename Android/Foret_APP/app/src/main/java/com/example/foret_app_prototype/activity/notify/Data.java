package com.example.foret_app_prototype.activity.notify;

public class Data {

    private String user,message,title,sent;
    private Integer icon;
    public Data() {
    }

    public Data(String user, String message, String title, String sent, Integer icon) {
        this.user = user;
        this.message = message;
        this.title = title;
        this.sent = sent;
        this.icon = icon;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }
}
