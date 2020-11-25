package com.example.foret.model;

import com.google.firebase.database.PropertyName;

public class ModelChat {
    String sender; // 보내는 사람
    String receiver; //받는사람
    String message; //메세지
    String timestamp; //시간

    public boolean isSeen; //읽기 여부

    public ModelChat() {
    }

    public ModelChat(String sender, String reciver, String message, String timestamp, boolean isSeen) {
        this.sender = sender;
        this.receiver = reciver;
        this.message = message;
        this.timestamp = timestamp;
        this.isSeen = isSeen;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @PropertyName("isSeen")
    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

}

