package comment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import comment.bean.CommentDTO;
import comment.dao.CommentDAO;

@Service
public class CommentServiceImpl implements CommentService{
	@Autowired
	CommentDAO commentDAO;

	@Override
	public int commentWrite(CommentDTO commentDTO) {
		return commentDAO.commentWrite(commentDTO);
	}

	@Override
	public int commentModify(CommentDTO commentDTO) {
		return commentDAO.commentModify(commentDTO);
	}

	@Override
	public int commentDelete(CommentDTO commentDTO) {
		return commentDAO.commentDelete(commentDTO);
	}

	@Override
	public int commentNull(CommentDTO commentDTO) {
		return commentDAO.commentNull(commentDTO);
	}
	
	@Override
	public int getReCommentCnt(CommentDTO commentDTO) {
		return commentDAO.getReCommentCnt(commentDTO);
	}
}
