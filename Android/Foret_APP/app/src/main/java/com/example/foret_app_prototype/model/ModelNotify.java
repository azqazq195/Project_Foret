package com.example.foret_app_prototype.model;

import com.google.firebase.database.PropertyName;

public class ModelNotify {

    String type, content, time,sender,receiver;
    boolean isSeen;

    public ModelNotify() {
    }

    public ModelNotify(String type, String content, String time, String sender, String receiver, boolean isSeen) {
        this.type = type;
        this.content = content;
        this.time = time;
        this.sender = sender;
        this.receiver = receiver;
        this.isSeen = isSeen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    @PropertyName("isSeen")
    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
