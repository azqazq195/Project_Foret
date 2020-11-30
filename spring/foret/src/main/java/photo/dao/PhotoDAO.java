package photo.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import photo.bean.PhotoDTO;

@Repository
public class PhotoDAO {
	@Autowired
	SqlSessionTemplate sqlSession;
	
	public int memberPhotoWrite(PhotoDTO photoDTO) {
		return sqlSession.insert("mybatis.photoMapper.memberPhotoWrite", photoDTO);
	}
	public int memberPhotoDelete(PhotoDTO photoDTO) {
		return sqlSession.insert("mybatis.photoMapper.memberPhotoDelete", photoDTO);
	}
	
	public int foretPhotoWrite(PhotoDTO photoDTO) {
		return sqlSession.insert("mybatis.photoMapper.foretPhotoWrite", photoDTO);
	}
	public int foretPhotoDelete(PhotoDTO photoDTO) {
		return sqlSession.insert("mybatis.photoMapper.foretPhotoDelete", photoDTO);
	}
	
	public int boardPhotoWrite(List<PhotoDTO> list) {
		return sqlSession.insert("mybatis.photoMapper.boardPhotoWrite", list);
	}
	public int boardPhotoDelete(int id) {
		return sqlSession.insert("mybatis.photoMapper.boardPhotoDelete", id);
	}
}
