package tag.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import foret.bean.ForetTagDTO;
import member.bean.MemberTagDTO;
import tag.bean.TagDTO;

@Repository
public class TagDAO {
	@Autowired
	SqlSessionTemplate sqlSession;
	
	public int tagWrite(TagDTO tagDTO) {
		return sqlSession.insert("mybatis.tagMapper.tagWrite", tagDTO);
	}
	public int tagDelete(TagDTO tagDTO) {
		return sqlSession.delete("mybatis.tagMapper.tagDelete", tagDTO);
	}
	public List<TagDTO> tagList() {
		return sqlSession.selectList("mybatis.tagMapper.tagList");
	}
	public List<TagDTO> tagRank(int rank) {
		return sqlSession.selectList("mybatis.tagMapper.tagRank", rank);
	}
	
	public List<TagDTO> getTagId(List<TagDTO> list) {
		return sqlSession.selectList("mybatis.tagMapper.getTagId", list);
	}
	
	public int memberTagWrite(List<MemberTagDTO> list) {
		return sqlSession.insert("mybatis.tagMapper.memberTagWrite", list);
	}
	public int memberTagDelete(int id) {
		return sqlSession.delete("mybatis.tagMapper.memberTagDelete", id);
	}
	
	public int foretTagWrite(List<ForetTagDTO> list) {
		return sqlSession.insert("mybatis.tagMapper.foretTagWrite", list);
	}
	public int foretTagDelete(int id) {
		return sqlSession.delete("mybatis.tagMapper.foretTagDelete", id);
	}
}
