package foret.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import foret.bean.ForetDTO;

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
}
