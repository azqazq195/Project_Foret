package search.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import member.bean.MemberDTO;

@Repository
public class SearchDAO {
	@Autowired
	SqlSessionTemplate sqlSession;
	public MemberDTO emailCheck(String email) {
		return sqlSession.selectOne("mybatis.searchMapper.emailCheck", email);
	}
	public MemberDTO memberSelect(String email) {
		return sqlSession.selectOne("mybatis.searchMapper.memberSelect", email);
	}
}
