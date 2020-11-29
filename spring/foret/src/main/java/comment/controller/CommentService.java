package comment.controller;

import comment.bean.CommentDTO;

public interface CommentService {
	// 댓글 데이터
	public int commentWrite(CommentDTO commentDTO);
	public int commentModify(CommentDTO commentDTO);
	public int commentDelete(CommentDTO commentDTO);
	public int commentNull(CommentDTO commentDTO);
	
	public int getReCommentCnt(CommentDTO commentDTO);
}
