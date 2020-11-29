package board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import board.bean.BoardDTO;
import board.dao.BoardDAO;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	BoardDAO boardDAO;
	
	@Override
	public int boardWrite(BoardDTO boardDTO) {
		return boardDAO.boardWrite(boardDTO);
	}

	@Override
	public int boardModify(BoardDTO boardDTO) {
		return boardDAO.boardModify(boardDTO);
	}

	@Override
	public int boardDelete(BoardDTO boardDTO) {
		return boardDAO.boardDelete(boardDTO);
	}

}
