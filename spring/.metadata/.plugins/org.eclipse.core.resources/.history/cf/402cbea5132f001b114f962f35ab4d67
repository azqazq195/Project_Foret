package foret.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import foret.bean.ForetDTO;

@Repository
public class ForetDAO {
	@Autowired
	SqlSessionTemplate sqlSession;
	
	// 추가
	public int foretWrite(ForetDTO foretDTO) {
		return sqlSession.insert("mybatis.foretMapper.foretWrite", foretDTO);
	}
	
	// 삭제
	public int foretDelete(int group_no, String group_leader) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("group_no", group_no);
		map.put("group_leader", group_leader);
		return sqlSession.delete("mybatis.foretMapper.foretDelete", map);
	}
	
	// 수정
	public int foretModify(ForetDTO foretDTO) {
		return sqlSession.update("mybatis.foretMapper.foretModify", foretDTO);
	}
	
	// 리더 변경
	public int foretChangeLeader(String new_leader, int group_no, String group_leader) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("new_leader", new_leader);
		map.put("group_no", group_no);
		map.put("group_leader", group_leader);
		return sqlSession.update("mybatis.foretMapper.foretChangeLeader", map);
	}
		
	// 전체 검색
	public List<ForetDTO> foretList() {
		return sqlSession.selectList("mybatis.foretMapper.foretList");
	}
	
	// 단일 검색
	public ForetDTO foretSelect(int group_no) {
		return sqlSession.selectOne("mybatis.foretMapper.foretSelect", group_no);
	}
	
}
