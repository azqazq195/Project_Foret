package foret.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import foret.bean.ForetDTO;
import foret.bean.ForetMemberDTO;

@Repository
public class ForetDAO {
	@Autowired
	SqlSessionTemplate sqlSession;
	public int foretWrite(ForetDTO foretDTO) {
		return sqlSession.insert("mybatis.foretMapper.foretWrite", foretDTO);
	}
	public int foretModify(ForetDTO foretDTO) {
		return sqlSession.update("mybatis.foretMapper.foretModify", foretDTO);
	}
	public int foretDelete(ForetDTO foretDTO) {
		return sqlSession.delete("mybatis.foretMapper.foretDelete", foretDTO);
	}
	
	public int foretMemberWrite(ForetMemberDTO foretMemberDTO) {
		return sqlSession.insert("mybatis.foretMapper.foretMemberWrite", foretMemberDTO);
	}
	public int foretMemberDelete(ForetMemberDTO foretMemberDTO) {
		return sqlSession.insert("mybatis.foretMapper.foretMemberDelete", foretMemberDTO);
	}
}
