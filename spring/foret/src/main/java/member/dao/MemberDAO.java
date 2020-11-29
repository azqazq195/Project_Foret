package member.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import member.bean.MemberDTO;
import member.bean.MemberLikeDTO;

@Repository
public class MemberDAO {
	@Autowired
	SqlSessionTemplate sqlSession;
	
	public int memberWrite(MemberDTO memberDTO) {
		return sqlSession.insert("mybatis.memberMapper.memberWrite", memberDTO);
	}
	public int memberModify(MemberDTO memberDTO) {
		return sqlSession.update("mybatis.memberMapper.memberModify", memberDTO);
	}
	public int memberDelete(MemberDTO memberDTO) {
		return sqlSession.delete("mybatis.memberMapper.memberDelete", memberDTO);
	}
	
	public int memberBoardLike(MemberLikeDTO memberLikeDTO) {
		return sqlSession.insert("mybatis.memberMapper.memberBoardLike", memberLikeDTO);
	}
	public int memberBoardDisLike(MemberLikeDTO memberLikeDTO) {
		return sqlSession.delete("mybatis.memberMapper.memberBoardDisLike", memberLikeDTO);
	}
	public int memberCommentLike(MemberLikeDTO memberLikeDTO) {
		return sqlSession.insert("mybatis.memberMapper.memberCommentLike", memberLikeDTO);
	}
	public int memberCommentDisLike(MemberLikeDTO memberLikeDTO) {
		return sqlSession.delete("mybatis.memberMapper.memberCommentDisLike", memberLikeDTO);
	}
}
