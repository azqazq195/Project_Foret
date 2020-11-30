package photo.controller;

import photo.bean.PhotoDTO;

public interface PhotoService {
	// 멤버 사진
	public int memberPhotoWrite(PhotoDTO photoDTO);
	public int memberPhotoDelete(PhotoDTO photoDTO);
	
	// 포레 사진
	public int foretPhotoWrite(PhotoDTO photoDTO);
	public int foretPhotoDelete(PhotoDTO photoDTO);
	
	// 게시판 사진
	public int boardPhotoWrite(PhotoDTO photoDTO);
	public int boardPhotoDelete(PhotoDTO photoDTO);
}
