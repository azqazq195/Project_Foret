package board.controller;

import board.bean.BoardDTO;

public interface BoardService {
	// 보드 데이터
	public int boardWrite(BoardDTO boardDTO);
	public int boardModify(BoardDTO boardDTO);
	public int boardDelete(BoardDTO boardDTO);
}
