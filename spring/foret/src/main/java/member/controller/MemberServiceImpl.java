package member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import member.bean.MemberDTO;
import member.bean.MemberLikeDTO;
import member.dao.MemberDAO;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	MemberDAO memberDAO;

	@Override
	public int memberWrite(MemberDTO memberDTO) {
		return memberDAO.memberWrite(memberDTO);
	}

	@Override
	public int memberModify(MemberDTO memberDTO) {
		return memberDAO.memberModify(memberDTO);
	}

	@Override
	public int memberDelete(MemberDTO memberDTO) {
		return memberDAO.memberDelete(memberDTO);
	}

	@Override
	public int memberBoardLike(MemberLikeDTO memberLikeDTO) {
		return memberDAO.memberBoardLike(memberLikeDTO);
	}

	@Override
	public int memberBoardDisLike(MemberLikeDTO memberLikeDTO) {
		return memberDAO.memberBoardDisLike(memberLikeDTO);
	}

	@Override
	public int memberCommentLike(MemberLikeDTO memberLikeDTO) {
		return memberDAO.memberCommentLike(memberLikeDTO);
	}

	@Override
	public int memberCommentDisLike(MemberLikeDTO memberLikeDTO) {
		return memberDAO.memberCommentDisLike(memberLikeDTO);
	}
}
