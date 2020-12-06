package com.example.foret_app_prototype.model;

import java.io.Serializable;
import java.util.List;

public class HomeForetDTO implements Serializable {
    private static String RT;
    private static int foretTotal;

    private int id;
    private String name;
    private String photo;
    private List<HomeForetBoardDTO> board;

    public static String getRT() {
        return RT;
    }

    public static void setRT(String RT) {
        HomeForetDTO.RT = RT;
    }

    public static int getForetTotal() {
        return foretTotal;
    }

    public static void setForetTotal(int foretTotal) {
        HomeForetDTO.foretTotal = foretTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<HomeForetBoardDTO> getBoard() {
        return board;
    }

    public void setBoard(List<HomeForetBoardDTO> board) {
        this.board = board;
    }
}