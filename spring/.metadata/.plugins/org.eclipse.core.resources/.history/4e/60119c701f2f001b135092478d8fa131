package foret.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import foret.bean.ForetBoardDTO;

@Repository
public class ForetBoardDAO {
	@Autowired
	SqlSessionTemplate sqlSession;
	
	// 추가
	public int foretBoardWrite(ForetBoardDTO foretBoardDTO) {
		return sqlSession.insert("mybatis.foretBoardMapper.foretBoardWrite", foretBoardDTO);
	}
	
	// 삭제
	public int foretBoardDelete(int board_no, String board_writer) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("board_no", board_no);
		map.put("board_writer", board_writer);
		return sqlSession.delete("mybatis.foretBoardMapper.foretBoardDelete", map);
	}
	
	// 수정
	public int foretBoardModify(ForetBoardDTO foretBoardDTO) {
		return sqlSession.update("mybatis.foretBoardMapper.foretBoardModify", foretBoardDTO);
	}
		
	// 전체 검색
	public List<ForetBoardDTO> foretBoardList(int group_no) {
		return sqlSession.selectList("mybatis.foretBoardMapper.foretBoardList", group_no);
	}
	
	// 단일 검색
	public ForetBoardDTO foretBoardSelect(int board_no) {
		return sqlSession.selectOne("mybatis.foretBoardMapper.foretBoardSelect", board_no);
	}
}
