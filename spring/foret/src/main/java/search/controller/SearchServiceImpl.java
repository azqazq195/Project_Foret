package search.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import board.bean.BoardDTO;
import comment.bean.CommentDTO;
import foret.bean.ForetDTO;
import foret.bean.ForetMemberDTO;
import foret.bean.ForetRegionDTO;
import foret.bean.ForetTagDTO;
import member.bean.MemberDTO;
import member.bean.MemberForetDTO;
import member.bean.MemberLikeDTO;
import member.bean.MemberRegionDTO;
import member.bean.MemberTagDTO;
import search.bean.BoardALLDTO;
import search.bean.ForetALLDTO;
import search.bean.KeywordDTO;
import search.bean.MemberALLDTO;
import search.bean.MyForetDTO;
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
	public List<MemberALLDTO> memberSelect(int id) {
		return searchDAO.memberSelect(id);
	}
	@Override
	public MemberDTO memberSelect(String email) {
		return searchDAO.memberSelect(email);
	}
	
	@Override
	public int memberLogin(String email, String password) {
		return searchDAO.memberLogin(email, password);
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

	@Override
	public List<MemberForetDTO> memberForetSelect(int member_id) {
		return searchDAO.memberForetSelect(member_id);
	}

	@Override
	public List<MyForetDTO> myForetSelect(int member_id) {
		return searchDAO.myForetSelect(member_id);
	}

	@Override
	public List<BoardDTO> etcBoardListRecent(int type) {
		return searchDAO.etcBoardListRecent(type);
	}

	@Override
	public List<BoardDTO> etcBoardListHit(int type) {
		return searchDAO.etcBoardListHit(type);
	}

	@Override
	public List<BoardDTO> etcBoardListLike(int type) {
		return searchDAO.etcBoardListLike(type);
	}

	@Override
	public List<BoardDTO> etcBoardListComment(int type) {
		return searchDAO.etcBoardListComment(type);
	}

	@Override
	public List<BoardDTO> boardListRecent(BoardDTO boardDTO) {
		return searchDAO.boardListRecent(boardDTO);
	}

	@Override
	public List<BoardDTO> boardListHit(BoardDTO boardDTO) {
		return searchDAO.boardListHit(boardDTO);
	}

	@Override
	public List<BoardDTO> boardListLike(BoardDTO boardDTO) {
		return searchDAO.boardListLike(boardDTO);
	}

	@Override
	public List<BoardDTO> boardListComment(BoardDTO boardDTO) {
		return searchDAO.boardListComment(boardDTO);
	}

	@Override
	public List<BoardDTO> homeFragement(int member_id) {
		return searchDAO.homeFragement(member_id);
	}

	@Override
	public List<CommentDTO> commentList(int board_id) {
		return searchDAO.commentList(board_id);
	}

	@Override
	public List<BoardDTO> etcBoardListRecentPage(int type, int startNum, int endNum) {
		return searchDAO.etcBoardListRecentPage(type, startNum, endNum);
	}

	@Override
	public List<BoardDTO> etcBoardListHitPage(int type, int startNum, int endNum) {
		return searchDAO.etcBoardListHitPage(type, startNum, endNum);
	}

	@Override
	public List<BoardDTO> etcBoardListLikePage(int type, int startNum, int endNum) {
		return searchDAO.etcBoardListLikePage(type, startNum, endNum);
	}

	@Override
	public List<BoardDTO> etcBoardListCommentPage(int type, int startNum, int endNum) {
		return searchDAO.etcBoardListCommentPage(type, startNum, endNum);
	}

	@Override
	public List<BoardDTO> boardListRecentPage(int type, int foret_id, int startNum, int endNum) {
		return searchDAO.boardListRecentPage(type, foret_id, startNum, endNum);
	}

	@Override
	public List<BoardDTO> boardListHitPage(int type, int foret_id, int startNum, int endNum) {
		return searchDAO.boardListHitPage(type, foret_id, startNum, endNum);
	}

	@Override
	public List<BoardDTO> boardListLikePage(int type, int foret_id, int startNum, int endNum) {
		return searchDAO.boardListLikePage(type, foret_id, startNum, endNum);
	}

	@Override
	public List<BoardDTO> boardListCommentPage(int type, int foret_id, int startNum, int endNum) {
		return searchDAO.boardListCommentPage(type, foret_id, startNum, endNum);
	}

	@Override
	public List<ForetDTO> foretSearchName(String name) {
		return searchDAO.foretSearchName(name);
	}

	@Override
	public List<ForetTagDTO> foretSearchTag(String tag) {
		return searchDAO.foretSearchTag(tag);
	}

	@Override
	public List<ForetRegionDTO> foretSearchRegion(String region_si, String region_gu) {
		return searchDAO.foretSearchRegion(region_si, region_gu);
	}

	@Override
	public List<ForetALLDTO> foretSelect(int id) {
		return searchDAO.foretSelect(id);
	}

	@Override
	public List<ForetTagDTO> foretTagSelect(int foret_id) {
		return searchDAO.foretTagSelect(foret_id);
	}

	@Override
	public List<ForetRegionDTO> foretRegionSelect(int foret_id) {
		return searchDAO.foretRegionSelect(foret_id);
	}

	@Override
	public List<ForetMemberDTO> foretMemberSelect(int foret_id) {
		return searchDAO.foretMemberSelect(foret_id);
	}

	@Override
	public List<BoardALLDTO> homeSelect(int id) {
		return searchDAO.homeSelect(id);
	}

	@Override
	public List<BoardALLDTO> boardListPage(Map<String, Integer> map) {
		return searchDAO.boardListPage(map);
	}
	
	@Override
	public List<BoardALLDTO> boardList(Map<String, Integer> map) {
		return searchDAO.boardList(map);
	}

	@Override
	public List<BoardALLDTO> boardSelect(int id) {
		return searchDAO.boardSelect(id);
	}

	@Override
	public List<ForetALLDTO> foretRank(int rank) {
		return searchDAO.foretRank(rank);
	}

	@Override
	public List<ForetALLDTO> foretkeywordSearch(Map<String, String> map) {
		return searchDAO.foretkeywordSearch(map);
	}

	@Override
	public List<KeywordDTO> searchKeyword() {
		return searchDAO.searchKeyword();
	}
}
