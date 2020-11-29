package photo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import photo.bean.PhotoDTO;
import photo.dao.PhotoDAO;

@Service
public class PhotoServiceImpl implements PhotoService{
	@Autowired
	PhotoDAO photoDAO;
	
	@Override
	public int memberPhotoWrite(PhotoDTO photoDTO) {
		return photoDAO.memberPhotoWrite(photoDTO);
	}
	@Override
	public int memberPhotoDelete(PhotoDTO photoDTO) {
		return photoDAO.memberPhotoDelete(photoDTO);
	}

	@Override
	public int foretPhotoWrite(PhotoDTO photoDTO) {
		return photoDAO.foretPhotoWrite(photoDTO);
	}
	@Override
	public int foretPhotoDelete(PhotoDTO photoDTO) {
		return photoDAO.foretPhotoDelete(photoDTO);
	}

	@Override
	public int boardPhotoWrite(List<PhotoDTO> list) {
		return photoDAO.boardPhotoWrite(list);
	}
	@Override
	public int boardPhotoDelete(int id) {
		return photoDAO.boardPhotoDelete(id);
	}
	
}
