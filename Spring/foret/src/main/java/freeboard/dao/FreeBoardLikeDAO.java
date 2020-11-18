package freeboard.dao;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FreeBoardLikeDAO {
	
	@Autowired
	SqlSessionTemplate sqlSession;
	
	public int clickLike(String member_id, int text_no) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("member_id", member_id);
		map.put("text_no", text_no);
		return sqlSession.insert("mybatis.freeboard.clickLike", map);
	}
	
	public int unClickLike(String member_id, int text_no) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("member_id", member_id);
		map.put("text_no", text_no);
		return sqlSession.delete("mybatis.freeboard.unClickLike", map);
	}
	
	public int likeSeq(String member_id, int text_no) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("member_id", member_id);
		map.put("text_no", text_no);
		return sqlSession.selectOne("mybatis.freeboard.likeSeq", map);
	}
	
	public boolean isLiked(String member_id, int text_no) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("member_id", member_id);
		map.put("text_no", text_no);
		return sqlSession.selectOne("mybatis.freeboard.isLiked", map);
	}

}
