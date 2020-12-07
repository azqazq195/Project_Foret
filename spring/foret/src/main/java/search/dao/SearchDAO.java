package search.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

@Repository
public class SearchDAO {
	@Autowired
	SqlSessionTemplate sqlSession;
	public MemberDTO emailCheck(String email) {
		return sqlSession.selectOne("mybatis.searchMapper.emailCheck", email);
	}
	public List<MemberALLDTO> memberSelect(int id) {
		return sqlSession.selectList("mybatis.searchMapper.memberSelectId", id);
	}
	public MemberDTO memberSelect(String email) {
		return sqlSession.selectOne("mybatis.searchMapper.memberSelectEmail", email);
	}
	public int memberLogin(String email, String password) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("email", email);
		map.put("password", password);
		return sqlSession.selectOne("mybatis.searchMapper.memberLogin", map);
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
	public List<MyForetDTO> myForetSelect(int member_id) {
		return sqlSession.selectList("mybatis.searchMapper.myForetSelect", member_id);
	}
	public List<BoardDTO> etcBoardListRecent(int type) {
		return sqlSession.selectList("mybatis.searchMapper.etcBoardListRecent", type);
	}
	public List<BoardDTO> etcBoardListHit(int type) {
		return sqlSession.selectList("mybatis.searchMapper.etcBoardListHit", type);
	}
	public List<BoardDTO> etcBoardListLike(int type) {
		return sqlSession.selectList("mybatis.searchMapper.etcBoardListLike", type);
	}
	public List<BoardDTO> etcBoardListComment(int type) {
		return sqlSession.selectList("mybatis.searchMapper.etcBoardListComment", type);
	}
	public List<BoardDTO> boardListRecent(BoardDTO boardDTO) {
		return sqlSession.selectList("mybatis.searchMapper.boardListRecent", boardDTO);
	}
	public List<BoardDTO> boardListHit(BoardDTO boardDTO) {
		return sqlSession.selectList("mybatis.searchMapper.boardListHit", boardDTO);
	}
	public List<BoardDTO> boardListLike(BoardDTO boardDTO) {
		return sqlSession.selectList("mybatis.searchMapper.boardListLike", boardDTO);
	}
	public List<BoardDTO> boardListComment(BoardDTO boardDTO) {
		return sqlSession.selectList("mybatis.searchMapper.boardListComment", boardDTO);
	}
	public List<BoardDTO> homeFragement(int member_id) {
		return sqlSession.selectList("mybatis.searchMapper.homeFragement", member_id);
	}
	public List<CommentDTO> commentList(int board_id) {
		return sqlSession.selectList("mybatis.searchMapper.commentList", board_id);
	}
	
	
	public List<BoardDTO> etcBoardListRecentPage(int type, int startNum, int endNum) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("type", type);
		map.put("startNum", startNum);
		map.put("endNum", endNum);
		return sqlSession.selectList("mybatis.searchMapper.etcBoardListRecentPage", map);
	}
	public List<BoardDTO> etcBoardListHitPage(int type, int startNum, int endNum) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("type", type);
		map.put("startNum", startNum);
		map.put("endNum", endNum);
		return sqlSession.selectList("mybatis.searchMapper.etcBoardListHitPage", map);
	}
	public List<BoardDTO> etcBoardListLikePage(int type, int startNum, int endNum) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("type", type);
		map.put("startNum", startNum);
		map.put("endNum", endNum);
		return sqlSession.selectList("mybatis.searchMapper.etcBoardListLikePage", map);
	}
	public List<BoardDTO> etcBoardListCommentPage(int type, int startNum, int endNum) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("type", type);
		map.put("startNum", startNum);
		map.put("endNum", endNum);
		return sqlSession.selectList("mybatis.searchMapper.etcBoardListCommentPage", map);
	}
	
	
	public List<BoardDTO> boardListRecentPage(int type, int foret_id, int startNum, int endNum) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("type", type);
		map.put("foret_id", foret_id);
		map.put("startNum", startNum);
		map.put("endNum", endNum);
		return sqlSession.selectList("mybatis.searchMapper.boardListRecentPage", map);
	}
	public List<BoardDTO> boardListHitPage(int type, int foret_id, int startNum, int endNum) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("type", type);
		map.put("foret_id", foret_id);
		map.put("startNum", startNum);
		map.put("endNum", endNum);
		return sqlSession.selectList("mybatis.searchMapper.boardListHitPage", map);
	}
	public List<BoardDTO> boardListLikePage(int type, int foret_id, int startNum, int endNum) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("type", type);
		map.put("foret_id", foret_id);
		map.put("startNum", startNum);
		map.put("endNum", endNum);
		return sqlSession.selectList("mybatis.searchMapper.boardListLikePage", map);
	}
	public List<BoardDTO> boardListCommentPage(int type, int foret_id, int startNum, int endNum) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("type", type);
		map.put("foret_id", foret_id);
		map.put("startNum", startNum);
		map.put("endNum", endNum);
		return sqlSession.selectList("mybatis.searchMapper.boardListCommentPage", map);
	}
	
	
	public List<ForetDTO> foretSearchName(String name){
		return sqlSession.selectList("mybatis.searchMapper.foretSearchName", name);
	}
	public List<ForetTagDTO> foretSearchTag(String tag){
		return sqlSession.selectList("mybatis.searchMapper.foretSearchTag", tag);
	}
	public List<ForetRegionDTO> foretSearchRegion(String region_si, String region_gu){
		Map<String, String> map = new HashMap<String, String>();
		map.put("region_si", region_si);
		map.put("region_gu", region_gu);
		return sqlSession.selectList("mybatis.searchMapper.foretSearchRegion", map);
	}
	
	public List<ForetALLDTO> foretSelect(int id){
		return sqlSession.selectList("mybatis.searchMapper.foretSelect", id);
	}
	public List<ForetALLDTO> foretRank(int rank){
		return sqlSession.selectList("mybatis.searchMapper.foretRank", rank);
	}
	public List<ForetTagDTO> foretTagSelect(int foret_id){
		return sqlSession.selectList("mybatis.searchMapper.foretTagSelect", foret_id);
	}
	public List<ForetRegionDTO> foretRegionSelect(int foret_id){
		return sqlSession.selectList("mybatis.searchMapper.foretRegionSelect", foret_id);
	}
	public List<ForetMemberDTO> foretMemberSelect(int foret_id){
		return sqlSession.selectList("mybatis.searchMapper.foretMemberSelect", foret_id);
	}
	public List<BoardALLDTO> homeSelect(int id) {
		return sqlSession.selectList("mybatis.searchMapper.homeSelect", id);
	}
	public List<BoardALLDTO> boardListPage(Map<String, Integer> map) {
		return sqlSession.selectList("mybatis.searchMapper.boardListPage", map);
	}
	public List<BoardALLDTO> boardList(Map<String, Integer> map) {
		return sqlSession.selectList("mybatis.searchMapper.boardList", map);
	}
	public List<BoardALLDTO> boardSelect(int id) {
		return sqlSession.selectList("mybatis.searchMapper.boardSelect", id);
	}
	public List<ForetALLDTO> foretkeywordSearch(Map<String, String> map){
		return sqlSession.selectList("mybatis.searchMapper.foretkeywordSearch", map);
	}
	public List<KeywordDTO> searchKeyword(){
		return sqlSession.selectList("mybatis.searchMapper.searchKeyword");
	}
}
