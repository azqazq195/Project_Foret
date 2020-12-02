package com.example.foret_app_prototype.model;

public class ModelChatList {

    String id;  //chat list  sender / reciver uid

    public ModelChatList(String id) {
        this.id = id;
    }

    public ModelChatList() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
