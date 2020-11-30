package photo.dao;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import photo.bean.PhotoDTO;

@Repository
public class PhotoDAO {
	@Autowired
	SqlSessionTemplate sqlSession;
	
	// 저장
	public int photoWrite(PhotoDTO photoDTO) {
		return sqlSession.insert("mybatis.photoMapper.photoWrite", photoDTO);
	}
	
	// 마지막 photo_id를 가져옴
	public int getMaxPhotoId() {
		return sqlSession.selectOne("mybatis.photoMapper.getMaxPhotoId");
	}
	
	// 파일이 있는지 검사
	public String checkPhoto(int photo_id) {
	    return sqlSession.selectOne("mybatis.photoMapper.checkPhoto", photo_id);
	}

	// 파일 삭제 : 1. db 삭제  2. 사진 삭제
	public int photoDelete(int photo_id, String photo_path) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("photo_id", photo_id);
		map.put("photo_path", photo_path);
		int result = sqlSession.delete("mybatis.photoMapper.photoDelete", map);
		if(result > 0) { // 파일 삭제
			File file = new File(photo_path);
			file.delete();
		}
		return result;
	}
	
	// 사진 
	public PhotoDTO photoSelect(int photo_id) {
		return sqlSession.selectOne("mybatis.photoMapper.photoSelect", photo_id);
	}
}
