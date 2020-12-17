package com.project.foret.db.mapper;

import com.project.foret.db.model.Board;

public interface BoardMapper {
    public int boardInsert(Board board) throws Exception;

    public int boardUpdate(Board board) throws Exception;

    public int boardDelete(Board board) throws Exception;

}
