package freeboard.controller;

import java.util.List;
import freeboard.bean.FreeBoard;
import freeboard.bean.FreeBoardComment;

public interface FreeBoardService {
	
	public int writeFreeboard(FreeBoard freeBoard);
	
	public int modifyFreeboard(FreeBoard freeBoard);
	
	public int updateHitFreeboard(int freeboard_no);
	
	public int updateLikePlusFreeboard(int freeboard_no);
	
	public int updateLikeMinusFreeboard(int freeboard_no);
	
	public int updateReplyPlusFreeboard(int freeboard_no);
	
	public int updateReplyMinusFreeboard(int freeboard_no);
	
	public int deleteFreeboard(int freeboard_no, String member_id);
	
	public FreeBoard readFreeboard(int freeboard_no);
	
	public List<FreeBoard> listDateFreeboard(int startNum, int endNum);
	
	public List<FreeBoard> listReplyFreeboard(int startNum, int endNum);
	
	public List<FreeBoard> listLikeFreeboard(int startNum, int endNum);
	
	public int writeComment(FreeBoardComment freeBoardComment);
	
	public int modifyComment(String freeboard_comment_content,
			int freeboard_comment_no, String freeboard_comment_writer);
	
	public int deleteComment(int freeboard_comment_no, String freeboard_comment_writer);
	
	public int deleteCommentALL(int freeboard_no);
	
	public List<FreeBoardComment> commentList(int freeboard_no);
	
	public FreeBoardComment selectComment(int freeboard_comment_no, int freeboard_no);
	
	public int clickLike(String member_id, int text_no);
	
	public int unClickLike(String member_id, int text_no);
	
	public int likeSeq(String member_id, int text_no);
	
	public boolean isLiked(String member_id, int text_no);
	
}
