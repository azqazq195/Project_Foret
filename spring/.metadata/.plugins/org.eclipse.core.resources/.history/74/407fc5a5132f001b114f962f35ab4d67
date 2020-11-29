package foret.controller;

import java.util.List;

import foret.bean.ForetBoardDTO;
import foret.bean.ForetDTO;
import photo.bean.PhotoDTO;

public interface ForetService {
	/*포레*/
	// 저장
	public int foretWrite(ForetDTO foretDTO);
	// 삭제
	public int foretDelete(int group_no, String group_leader);
	// 전체 보기
	public List<ForetDTO> foretList();
	// 단일 검색
	public ForetDTO foretSelect(int group_no);
	// 수정
	public int foretModify(ForetDTO foretDTO);
	// 리더 변경
	public int foretChangeLeader(String new_leader, int group_no, String group_leader);
	
	/*포레 게시판*/
	// 추가
	public int foretBoardWrite(ForetBoardDTO foretBoardDTO);
	// 삭제
	public int foretBoardDelete(int board_no, String board_writer);
	// 수정
	public int foretBoardModify(ForetBoardDTO foretBoardDTO);
	// 전체 검색
	public List<ForetBoardDTO> foretBoardList(int group_no);
	// 단일 검색
	public ForetBoardDTO foretBoardSelect(int board_no);
	
	/* 사진 */
	// 사진 저장
	public int photoWrite(PhotoDTO photoDTO);
	// 마지막 photo_id를 가져옴
	public int getMaxPhotoId();
	// 사진이 있는지 검사
	public String checkPhoto(int photo_id);
	// 사진 삭제 : 1. 실제 사진 삭제  2. db 삭제
	public int photoDelete(int photo_id, String photo_path);
	// 사진 보기 
	public PhotoDTO photoSelect(int photo_id);

}
