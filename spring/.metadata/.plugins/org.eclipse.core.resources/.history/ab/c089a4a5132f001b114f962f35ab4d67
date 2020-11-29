package member.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import member.bean.Member;

@Repository
public class MemberDAO {

	@Autowired
	SqlSessionTemplate sqlSession;
	
	public int writeMember(Member member) {
		return sqlSession.insert("mybatis.member.writeMember", member);
	}
	
	public int modifyMember(Member member) {
		return sqlSession.update("mybatis.member.modifyMember", member);
	}
	
}
