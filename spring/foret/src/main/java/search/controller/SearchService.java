package search.controller;

import java.util.List;

import member.bean.MemberDTO;
import member.bean.MemberForetDTO;
import member.bean.MemberLikeDTO;
import member.bean.MemberRegionDTO;
import member.bean.MemberTagDTO;

public interface SearchService {
	
	public MemberDTO emailCheck(String email);
	public MemberDTO memberSelect(String email);
	public int memberLogin(String email, String password);
	
	public List<MemberTagDTO> memberTagSelect(int member_id);
	public List<MemberRegionDTO> memberRegionSelect(int member_id);
	public List<MemberLikeDTO> memberBoardLikeSelect(int member_id);
	public List<MemberLikeDTO> memberCommentLikeSelect(int member_id);
	public List<MemberForetDTO> memberForetSelect(int member_id);
}
