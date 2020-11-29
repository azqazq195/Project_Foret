package freeboard.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import freeboard.bean.FreeBoardComment;

@Repository
public class FreeBoardCommentDAO {
	
	@Autowired
	SqlSessionTemplate sqlSession;

	public int writeComment(FreeBoardComment freeBoardComment) {
		return sqlSession.insert("mybatis.freeboard.writeComment", freeBoardComment);
	}
	
	public int modifyComment(String freeboard_comment_content, int freeboard_comment_no, String freeboard_comment_writer) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("freeboard_comment_content", freeboard_comment_content);
		map.put("freeboard_comment_no", freeboard_comment_no);
		map.put("freeboard_comment_writer", freeboard_comment_writer);
		return sqlSession.update("mybatis.freeboard.modifyComment", map);
	}
	
	public int deleteComment(int freeboard_comment_no, String freeboard_comment_writer) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("freeboard_comment_no", freeboard_comment_no);
		map.put("freeboard_comment_writer", freeboard_comment_writer);
		return sqlSession.delete("mybatis.freeboard.deleterComment", map);
	}
	
	public int deleteCommentALL(int freeboard_no) {
		return sqlSession.delete("mybatis.freeboard.deleteCommentALL", freeboard_no);
	}
	
	public List<FreeBoardComment> commentList(int freeboard_no) {
		return sqlSession.selectList("mybatis.freeboard.commentList", freeboard_no);
	}
	
	public FreeBoardComment selectComment(int freeboard_comment_no, int freeboard_no) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("freeboard_comment_no", freeboard_comment_no);
		map.put("freeboard_no", freeboard_no);
		return sqlSession.selectOne("mybatis.freeboard.selectComment", map);
	}
	
}
