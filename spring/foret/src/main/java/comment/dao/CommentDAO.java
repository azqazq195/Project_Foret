package comment.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import comment.bean.CommentDTO;

@Repository
public class CommentDAO {
	@Autowired
	SqlSessionTemplate sqlSession;
	public int commentWrite(CommentDTO commentDTO) {
		return sqlSession.insert("mybatis.commentMapper.commentWrite", commentDTO);
	}
	public int commentModify(CommentDTO commentDTO) {
		return sqlSession.update("mybatis.commentMapper.commentModify", commentDTO);
	}
	public int commentDelete(CommentDTO commentDTO) {
		return sqlSession.delete("mybatis.commentMapper.commentDelete", commentDTO);
	}
	public int commentNull(CommentDTO commentDTO) {
		return sqlSession.update("mybatis.commentMapper.commentNull", commentDTO);
	}
	
	public int getReCommentCnt(CommentDTO commentDTO) {
		return sqlSession.selectOne("mybatis.commentMapper.getReCommentCnt", commentDTO);
	}
}
