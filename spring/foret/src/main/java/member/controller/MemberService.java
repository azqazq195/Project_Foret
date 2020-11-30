package member.controller;

import member.bean.MemberDTO;
import member.bean.MemberLikeDTO;

public interface MemberService {
	// 멤버 데이터
	public int memberWrite(MemberDTO memberDTO);
	public int memberModify(MemberDTO memberDTO);
	public int memberDelete(MemberDTO memberDTO);
	
	// 멤버 좋아요
	public int memberBoardLike(MemberLikeDTO memberLikeDTO);
	public int memberBoardDisLike(MemberLikeDTO memberLikeDTO);
	public int memberCommentLike(MemberLikeDTO memberLikeDTO);
	public int memberCommentDisLike(MemberLikeDTO memberLikeDTO);
}
