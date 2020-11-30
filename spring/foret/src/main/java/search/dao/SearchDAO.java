package search.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import member.bean.MemberDTO;
import member.bean.MemberForetDTO;
import member.bean.MemberLikeDTO;
import member.bean.MemberRegionDTO;
import member.bean.MemberTagDTO;

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
	
	public List<MemberTagDTO> memberTagSelect(int member_id) {
		return sqlSession.selectList("mybatis.searchMapper.memberTagSelect", member_id);
	}
	public List<MemberRegionDTO> memberRegionSelect(int member_id) {
		return sqlSession.selectList("mybatis.searchMapper.memberRegionSelect", member_id);
	}
	public List<MemberLikeDTO> memberBoardLikeSelect(int member_id) {
		return sqlSession.selectList("mybatis.searchMapper.memberBoardLikeSelect", member_id);
	}
	public List<MemberLikeDTO> memberCommentLikeSelect(int member_id) {
		return sqlSession.selectList("mybatis.searchMapper.memberCommentLikeSelect", member_id);
	}
	public List<MemberForetDTO> memberForetSelect(int member_id) {
		return sqlSession.selectList("mybatis.searchMapper.memberForetSelect", member_id);
	}
	
}
