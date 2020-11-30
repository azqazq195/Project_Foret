package freeboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freeboard.bean.FreeBoard;
import freeboard.bean.FreeBoardComment;
import freeboard.dao.FreeBoardDAO;
import freeboard.dao.FreeBoardLikeDAO;
import freeboard.dao.FreeBoardCommentDAO;

@Service
public class FreeBoardServiceImpl implements FreeBoardService {
	
	@Autowired
	FreeBoardDAO freeBoardDAO;
	@Autowired
	FreeBoardCommentDAO freeBoardCommentDAO;
	@Autowired
	FreeBoardLikeDAO freeboadLikeDAO;
	
	//자유게시판 관리 DAO-------------------------------------------------
	@Override
	public int writeFreeboard(FreeBoard freeBoard) {
		return freeBoardDAO.writeFreeboard(freeBoard);
	}

	@Override
	public int modifyFreeboard(FreeBoard freeBoard) {
		return freeBoardDAO.modifyFreeboard(freeBoard);
	}

	@Override
	public int updateHitFreeboard(int freeboard_no) {
		return freeBoardDAO.updateHitFreeboard(freeboard_no);
	}

	@Override
	public int updateReplyPlusFreeboard(int freeboard_no) {
		return freeBoardDAO.updateReplyPlusFreeboard(freeboard_no);
	}
	
	@Override
	public int updateReplyMinusFreeboard(int freeboard_no) {
		return freeBoardDAO.updateReplyMinusFreeboard(freeboard_no);
	}

	@Override
	public int deleteFreeboard(int freeboard_no, String member_id) {
		return freeBoardDAO.deleteFreeboard(freeboard_no, member_id);
	}

	@Override
	public FreeBoard readFreeboard(int freeboard_no) {
		return freeBoardDAO.readFreeboard(freeboard_no);
	}

	@Override
	public List<FreeBoard> listDateFreeboard(int startNum, int endNum) {
		return freeBoardDAO.listDateFreeboard(startNum, endNum);
	}

	@Override
	public List<FreeBoard> listReplyFreeboard(int startNum, int endNum) {
		return freeBoardDAO.listReplyFreeboard(startNum, endNum);
	}

	@Override
	public List<FreeBoard> listLikeFreeboard(int startNum, int endNum) {
		return freeBoardDAO.listLikeFreeboard(startNum, endNum);
	}
	
	@Override
	public int updateLikePlusFreeboard(int freeboard_no) {
		return freeBoardDAO.updateLikePlusFreeboard(freeboard_no);
	}
	
	@Override
	public int updateLikeMinusFreeboard(int freeboard_no) {
		return freeBoardDAO.updateLikeMinusFreeboard(freeboard_no);
	}
	
	//자유게시판 게시글 내가 누른 좋아요 상태 관리 DAO-------------------------------------
	
	@Override
	public int clickLike(String member_id, int text_no) {
		return freeboadLikeDAO.clickLike(member_id, text_no);
	}

	@Override
	public int unClickLike(String member_id, int text_no) {
		return freeboadLikeDAO.unClickLike(member_id, text_no);
	}

	@Override
	public int likeSeq(String member_id, int text_no) {
		return freeboadLikeDAO.likeSeq(member_id, text_no);
	}
	
	@Override
	public boolean isLiked(String member_id, int text_no) {
		return freeboadLikeDAO.isLiked(member_id, text_no);
	}
	
	//자유게시판 게시글 댓글 관리 DAO-------------------------------------------------
	
	@Override
	public int writeComment(FreeBoardComment freeBoardComment) {
		return freeBoardCommentDAO.writeComment(freeBoardComment);
	}

	@Override
	public int modifyComment(String freeboard_comment_content, int freeboard_comment_no,
			String freeboard_comment_writer) {
		return freeBoardCommentDAO.modifyComment(freeboard_comment_content, freeboard_comment_no, freeboard_comment_writer);
	}

	@Override
	public int deleteComment(int freeboard_comment_no, String freeboard_comment_writer) {
		return freeBoardCommentDAO.deleteComment(freeboard_comment_no, freeboard_comment_writer);
	}
	
	@Override
	public int deleteCommentALL(int freeboard_no) {
		return freeBoardCommentDAO.deleteCommentALL(freeboard_no);
	}

	@Override
	public List<FreeBoardComment> commentList(int freeboard_no) {
		return freeBoardCommentDAO.commentList(freeboard_no);
	}

	@Override
	public FreeBoardComment selectComment(int freeboard_comment_no, int freeboard_no) {
		return freeBoardCommentDAO.selectComment(freeboard_comment_no, freeboard_no);
	}

}
