package com.example.foret_app_prototype.model;

import java.io.Serializable;
import java.util.List;

// HomeFragment 사용
public class HomeForetDTO implements Serializable {
    private int foretTotal;

    private int id;
    private String name;
    private String photo;
    private HomeForetBoardDTO homeForetBoardDTO[];
    private List<HomeForetBoardDTO> homeForetBoardDTOList;

    public List<HomeForetBoardDTO> getHomeForetBoardDTOList() {
        return homeForetBoardDTOList;
    }

    public void setHomeForetBoardDTOList(List<HomeForetBoardDTO> homeForetBoardDTOList) {
        this.homeForetBoardDTOList = homeForetBoardDTOList;
    }

    public int getForetTotal() {
        return foretTotal;
    }

    public void setForetTotal(int foretTotal) {
        this.foretTotal = foretTotal;
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

    public HomeForetBoardDTO[] getHomeForetBoardDTO() {
        return homeForetBoardDTO;
    }

    public void setHomeForetBoardDTO(HomeForetBoardDTO[] homeForetBoardDTO) {
        this.homeForetBoardDTO = homeForetBoardDTO;
    }
}