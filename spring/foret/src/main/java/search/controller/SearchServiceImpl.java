package search.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import member.bean.MemberDTO;
import member.bean.MemberLikeDTO;
import member.bean.MemberRegionDTO;
import member.bean.MemberTagDTO;
import search.dao.SearchDAO;

@Service
public class SearchServiceImpl implements SearchService {
	@Autowired
	SearchDAO searchDAO;

	@Override
	public MemberDTO emailCheck(String email) {
		return searchDAO.emailCheck(email);
	}
	
	@Override
	public MemberDTO memberSelect(String email) {
		return searchDAO.memberSelect(email);
	}

	@Override
	public List<MemberTagDTO> memberTagSelect(int member_id) {
		return searchDAO.memberTagSelect(member_id);
	}

	@Override
	public List<MemberRegionDTO> memberRegionSelect(int member_id) {
		return searchDAO.memberRegionSelect(member_id);
	}

	@Override
	public List<MemberLikeDTO> memberBoardLikeSelect(int member_id) {
		return searchDAO.memberBoardLikeSelect(member_id);
	}

	@Override
	public List<MemberLikeDTO> memberCommentLikeSelect(int member_id) {
		return searchDAO.memberCommentLikeSelect(member_id);
	}


	
}
