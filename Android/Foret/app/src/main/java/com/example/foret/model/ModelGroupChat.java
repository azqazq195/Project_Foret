package com.example.foret.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ModelGroupChat {
    String message,sender,senderPhoto,timestamp,type;
    int countSeen;

    public ModelGroupChat() {
    }

    public ModelGroupChat(String message, String sender, String senderPhoto, String timestamp, String type, int countSeen) {
        this.message = message;
        this.sender = sender;
        this.senderPhoto = senderPhoto;
        this.timestamp = timestamp;
        this.type = type;
        this.countSeen = countSeen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderPhoto() {
        return senderPhoto;
    }

    public void setSenderPhoto(String senderPhoto) {
        this.senderPhoto = senderPhoto;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCountSeen() {
        return countSeen;
    }

    public void setCountSeen(int countSeen) {
        this.countSeen = countSeen;
    }
}
