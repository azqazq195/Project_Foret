package com.example.foret_app_prototype.model;

public class ModelNotify {

    String type, message, time,iamge;

    public ModelNotify() {
    }

    public ModelNotify(String type, String message, String time, String iamge) {
        this.type = type;
        this.message = message;
        this.time = time;
        this.iamge = iamge;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIamge() {
        return iamge;
    }

    public void setIamge(String iamge) {
        this.iamge = iamge;
    }
}
