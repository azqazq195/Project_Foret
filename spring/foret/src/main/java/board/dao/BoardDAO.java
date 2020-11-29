package board.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import board.bean.BoardDTO;

@Repository
public class BoardDAO {
	@Autowired
	SqlSessionTemplate sqlSession;
	public int boardWrite(BoardDTO boardDTO) {
		return sqlSession.insert("mybatis.boardMapper.boardWrite", boardDTO);
	}
	public int boardModify(BoardDTO boardDTO) {
		return sqlSession.update("mybatis.boardMapper.boardModify", boardDTO);
	}
	public int boardDelete(BoardDTO boardDTO) {
		return sqlSession.delete("mybatis.boardMapper.boardDelete", boardDTO);
	}
}
