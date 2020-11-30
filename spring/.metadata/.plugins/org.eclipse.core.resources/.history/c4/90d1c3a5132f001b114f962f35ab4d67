package foret.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import foret.bean.ForetBoardDTO;
import foret.bean.ForetDTO;
import foret.dao.ForetBoardDAO;
import foret.dao.ForetDAO;
import photo.bean.PhotoDTO;
import photo.dao.PhotoDAO;

@Service
public class ForetServiceImpl implements ForetService{
	@Autowired
	ForetDAO foretDAO;
	@Autowired
	ForetBoardDAO foretBoardDAO;
	@Autowired
	PhotoDAO photoDAO;
	
	// 포레 추가
	@Override
	public int foretWrite(ForetDTO foretDTO) {
		return foretDAO.foretWrite(foretDTO);
	}

	// 포레 삭제
	@Override
	public int foretDelete(int group_no, String group_leader) {
		return foretDAO.foretDelete(group_no, group_leader);
	}

	// 포레 전체 목록
	@Override
	public List<ForetDTO> foretList() {
		return foretDAO.foretList();
	}
	
	// 포레 선택 보기
	@Override
	public ForetDTO foretSelect(int group_no) {
		return foretDAO.foretSelect(group_no);
	}
	
	@Override
	// 포레 수정
	public int foretModify(ForetDTO foretDTO) {
		return foretDAO.foretModify(foretDTO);
	}

	// 리더 변경
	@Override
	public int foretChangeLeader(String new_leader, int group_no, String group_leader) {
		return foretDAO.foretChangeLeader(new_leader, group_no, group_leader);
	}
	
	///////////////////////////////
	
	// 포레 게시글 추가
	public int foretBoardWrite(ForetBoardDTO foretBoardDTO) {
		return foretBoardDAO.foretBoardWrite(foretBoardDTO);
	}
	
	// 포레 게시글 삭제
	public int foretBoardDelete(int board_no, String board_writer) {
		return foretBoardDAO.foretBoardDelete(board_no, board_writer);
	}
	
	// 포레 게시글 수정
	public int foretBoardModify(ForetBoardDTO foretBoardDTO) {
		return foretBoardDAO.foretBoardModify(foretBoardDTO);
	}
		
	// 전체 검색
	public List<ForetBoardDTO> foretBoardList(int group_no) {
		return foretBoardDAO.foretBoardList(group_no);
	}
	
	// 단일 검색
	public ForetBoardDTO foretBoardSelect(int board_no) {
		return foretBoardDAO.foretBoardSelect(board_no);
	}
	
	///////////////////////////////////////////
	// 사진 추가
	@Override
	public int photoWrite(PhotoDTO photoDTO) {
		return photoDAO.photoWrite(photoDTO);
	}

	// 사진 있는지 확인
	@Override
	public String checkPhoto(int photo_id) {
		return photoDAO.checkPhoto(photo_id);
	}

	// 사진 삭제
	@Override
	public int photoDelete(int photo_id, String photo_path) {
		return photoDAO.photoDelete(photo_id, photo_path);
	}

	// seq 가져오기
	@Override
	public int getMaxPhotoId() {
		return photoDAO.getMaxPhotoId();
	}

	// 사진 선택 보기
	@Override
	public PhotoDTO photoSelect(int photo_id) {
		return photoDAO.photoSelect(photo_id);
	}

}
