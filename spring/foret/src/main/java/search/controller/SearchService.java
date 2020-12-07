package search.controller;

import java.util.List;
import java.util.Map;

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

public interface SearchService {
	
	public MemberDTO emailCheck(String email);
	public List<MemberALLDTO> memberSelect(int id);
	public MemberDTO memberSelect(String email);
	public int memberLogin(String email, String password);
	
	public List<BoardALLDTO> homeSelect(int id);
	
	public List<BoardALLDTO> boardListPage(Map<String, Integer> map);
	public List<BoardALLDTO> boardList(Map<String, Integer> map);
	public List<BoardALLDTO> boardSelect(int id);
	
	public List<ForetALLDTO> foretSelect(int id);
	public List<ForetALLDTO> foretRank(int rank);
	
	public List<KeywordDTO> searchKeyword();
	public List<ForetALLDTO> foretkeywordSearch(Map<String, String> map);
	
	public List<MemberTagDTO> memberTagSelect(int member_id);
	public List<MemberRegionDTO> memberRegionSelect(int member_id);
	public List<MemberLikeDTO> memberBoardLikeSelect(int member_id);
	public List<MemberLikeDTO> memberCommentLikeSelect(int member_id);
	public List<MemberForetDTO> memberForetSelect(int member_id);
	
	
	public List<ForetTagDTO> foretTagSelect(int foret_id);
	public List<ForetRegionDTO> foretRegionSelect(int foret_id);
	public List<ForetMemberDTO> foretMemberSelect(int foret_id);
	
	
	public List<MyForetDTO> myForetSelect(int member_id);
	
	public List<BoardDTO> etcBoardListRecent(int type);
	public List<BoardDTO> etcBoardListHit(int type);
	public List<BoardDTO> etcBoardListLike(int type);
	public List<BoardDTO> etcBoardListComment(int type);
	public List<BoardDTO> etcBoardListRecentPage(int type, int startNum, int endNum);
	public List<BoardDTO> etcBoardListHitPage(int type, int startNum, int endNum);
	public List<BoardDTO> etcBoardListLikePage(int type, int startNum, int endNum);
	public List<BoardDTO> etcBoardListCommentPage(int type, int startNum, int endNum);
	
	
	public List<BoardDTO> boardListRecent(BoardDTO boardDTO);
	public List<BoardDTO> boardListHit(BoardDTO boardDTO);
	public List<BoardDTO> boardListLike(BoardDTO boardDTO);
	public List<BoardDTO> boardListComment(BoardDTO boardDTO);
	public List<BoardDTO> boardListRecentPage(int type, int foret_id, int startNum, int endNum);
	public List<BoardDTO> boardListHitPage(int type, int foret_id, int startNum, int endNum);
	public List<BoardDTO> boardListLikePage(int type, int foret_id, int startNum, int endNum);
	public List<BoardDTO> boardListCommentPage(int type, int foret_id, int startNum, int endNum);
	
	
	public List<ForetDTO> foretSearchName(String name);
	public List<ForetTagDTO> foretSearchTag(String tag);
	public List<ForetRegionDTO> foretSearchRegion(String region_si, String region_gu);
	
	public List<BoardDTO> homeFragement(int member_id);
	
	public List<CommentDTO> commentList(int board_id);
	
}
