package com.project.foret.db.service;

import com.project.foret.db.mapper.BoardMapper;
import com.project.foret.db.model.Board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    @Autowired
    BoardMapper boardMapper;

    public int boardInsert(Board board) throws Exception {
        System.out.println("--- boardInsert");
        return boardMapper.boardInsert(board);
    }

    public int boardUpdate(Board board) throws Exception {
        System.out.println("--- boardUpdate");
        return boardMapper.boardUpdate(board);
    }

    public int boardDelete(Board board) throws Exception {
        System.out.println("--- boardDelete");
        return boardMapper.boardDelete(board);
    }
}
