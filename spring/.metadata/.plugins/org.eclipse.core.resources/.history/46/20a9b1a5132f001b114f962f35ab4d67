package freeboard.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import freeboard.bean.FreeBoard;

@Repository
public class FreeBoardDAO {
	
	@Autowired
	SqlSessionTemplate sqlSession;
	
	public int writeFreeboard(FreeBoard freeBoard) {
		return sqlSession.insert("mybatis.freeboard.writeFreeboard", freeBoard);
	}
	
	public int modifyFreeboard(FreeBoard freeBoard) {
		return sqlSession.update("mybatis.freeboard.modifyFreeboard", freeBoard);
	}
	
	public int updateHitFreeboard(int freeboard_no) {
		return sqlSession.update("mybatis.freeboard.updateHitFreeboard", freeboard_no);
	}
	
	public int updateLikePlusFreeboard(int freeboard_no) {
		return sqlSession.update("mybatis.freeboard.updateLikePlusFreeboard", freeboard_no);
	}
	
	public int updateLikeMinusFreeboard(int freeboard_no) {
		return sqlSession.update("mybatis.freeboard.updateLikeMinusFreeboard", freeboard_no);
	}
	
	public int updateReplyPlusFreeboard(int freeboard_no) {
		return sqlSession.update("mybatis.freeboard.updateReplyPlusFreeboard", freeboard_no);
	}
	
	public int updateReplyMinusFreeboard(int freeboard_no) {
		return sqlSession.update("mybatis.freeboard.updateReplyMinusFreeboard", freeboard_no);
	}
	
	
	public int deleteFreeboard(int freeboard_no, String member_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("freeboard_no", freeboard_no);
		map.put("member_id", member_id);
		return sqlSession.delete("mybatis.freeboard.deleteFreeboard", map);
	}
	
	public FreeBoard readFreeboard(int freeboard_no) {
		return sqlSession.selectOne("mybatis.freeboard.readFreeboard", freeboard_no);
	}
	
	//정렬
	//1. 날짜순(최신순)
	public List<FreeBoard> listDateFreeboard(int startNum, int endNum) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("startNum", startNum);
		map.put("endNum", endNum);
		return sqlSession.selectList("mybatis.freeboard.listDateFreeboard", map);
	}
	
	//2. 댓글순
	public List<FreeBoard> listReplyFreeboard(int startNum, int endNum) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("startNum", startNum);
		map.put("endNum", endNum);
		return sqlSession.selectList("mybatis.freeboard.listReplyFreeboard", map);
	}
	
	//3. 좋아요순
	public List<FreeBoard> listLikeFreeboard(int startNum, int endNum) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("startNum", startNum);
		map.put("endNum", endNum);
		return sqlSession.selectList("mybatis.freeboard.listLikeFreeboard", map);
	}

}
