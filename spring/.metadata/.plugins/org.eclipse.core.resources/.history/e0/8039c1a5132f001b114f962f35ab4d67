package foret.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import foret.bean.ForetBoardDTO;
import foret.bean.ForetDTO;
import photo.bean.PhotoDTO;

@Controller
public class ForetController {
	@Autowired
	ForetService foretService;
	
	// 매개변수 MultipartFile의 photo는 <input type="file" name="photo"/> 태그의 name과 일치해야함
	@RequestMapping(value = "/foret/foret_write.do")
	public ModelAndView foretWrite(HttpServletRequest request, MultipartFile photo) throws Exception {
	request.setCharacterEncoding("UTF-8");

	String group_name = request.getParameter("group_name");
	int group_currunt_member_count = Integer.parseInt(request.getParameter("group_currunt_member_count"));
	String group_leader = request.getParameter("group_leader");
	String group_profile = request.getParameter("group_profile");
	String group_tag = request.getParameter("group_tag");
	String group_region = request.getParameter("group_region");
	int group_max_member = Integer.parseInt(request.getParameter("group_max_member"));
	
	ForetDTO foretDTO = new ForetDTO();
	
	if(photo != null) {
		PhotoDTO photoDTO = new PhotoDTO();
		String photo_path = request.getSession().getServletContext().getRealPath("/storage");
		String photo_name = photo.getOriginalFilename();
		// 저장할 사진의 확장자를 원본이름에서 추출
		int lastIndex = photo_name.lastIndexOf(".");
		String photo_type = photo_name.substring(lastIndex + 1);
		
		// 사진 복사
		File file = new File(photo_path, photo_name);
        FileCopyUtils.copy(photo.getInputStream(), new FileOutputStream(file));
		
		// 사진 dto 저장
		photoDTO.setPhoto_name(photo_name);
		photoDTO.setPhoto_path(photo_path);
		photoDTO.setPhoto_type(photo_type);
		
		int photoResult = foretService.photoWrite(photoDTO);
		if(photoResult > 0) { 
			int max_seq = foretService.getMaxPhotoId();
			String photo_id = foretService.checkPhoto(max_seq);
			foretDTO.setGroup_photo(photo_id);
		}
	} else {
		foretDTO.setGroup_photo("0");
	}
	
	// 포레 DB
	foretDTO.setGroup_name(group_name);
	foretDTO.setGroup_currunt_member_count(group_currunt_member_count);
	foretDTO.setGroup_leader(group_leader);
	foretDTO.setGroup_profile(group_profile);
	foretDTO.setGroup_tag(group_tag);
	foretDTO.setGroup_region(group_region);
	foretDTO.setGroup_max_member(group_max_member);
	
	// 포레 정보 저장
	int writeResult = foretService.foretWrite(foretDTO);
	
	String rt = "FAIL";	
	if(writeResult > 0) rt = "OK";
	
	JSONObject json = new JSONObject();
	json.put("rt", rt);
	
	ModelAndView modelAndView = new ModelAndView();
	modelAndView.addObject("json", json);
	modelAndView.setViewName("foret.jsp");
	    
	return modelAndView;
	}
	
	// 목록 보기
	@RequestMapping(value = "/foret/foret_list.do")
	public ModelAndView foretList(HttpServletRequest request) throws Exception {
		List<ForetDTO> list = new ArrayList<ForetDTO>();
		list = foretService.foretList();
		int total = list.size();
		
		String rt = "FAIL";
		if(total > 0) {
			rt = "OK";
		}
		
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		json.put("total", total);
		JSONArray item = new JSONArray();
		
		for(int a=0; a<total; a++) {
			ForetDTO foretDTO = list.get(a);
			JSONObject temp = new JSONObject();
			temp.put("group_no", foretDTO.getGroup_no());
			temp.put("group_name", foretDTO.getGroup_name());
			temp.put("group_currunt_member_count", foretDTO.getGroup_currunt_member_count());
			temp.put("group_leader", foretDTO.getGroup_leader());
			temp.put("group_profile", foretDTO.getGroup_profile());
			temp.put("group_photo", foretDTO.getGroup_photo());
			temp.put("group_tag", foretDTO.getGroup_tag());
			temp.put("group_region", foretDTO.getGroup_region());
			temp.put("group_max_member", foretDTO.getGroup_max_member());
			temp.put("group_date_issued", foretDTO.getGroup_date_issued());
			
			String photoURL = "";
			if(!foretDTO.getGroup_photo().equals("0")) {
				String photo_id = foretService.checkPhoto(
					Integer.parseInt(foretDTO.getGroup_photo()));
				
				PhotoDTO photoDTO = foretService.photoSelect(Integer.parseInt(photo_id));
				String photo_name = photoDTO.getPhoto_name();	
				
				photoURL = "http://192.168.0.2:8081/project/storage/" + photo_name;
			}
			temp.put("photo_name", photoURL);	
			item.put(a, temp);
		}
		json.put("item", item);
		
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.addObject("json", json);
	    modelAndView.setViewName("foret.jsp");
	    
	    return modelAndView;
	}
	
	// 포레 삭제
	@RequestMapping(value = "/foret/foret_delete.do")
	public ModelAndView foretDelete(HttpServletRequest request) throws Exception {
		request.setCharacterEncoding("UTF-8");
		
		int group_no = Integer.parseInt(request.getParameter("group_no"));
		String group_leader = request.getParameter("group_leader");
		
		ForetDTO foretDTO = foretService.foretSelect(group_no);
		
		int deleteResult = foretService.foretDelete(group_no, group_leader);

		String rt = "FAIL";
		if(deleteResult > 0) rt = "OK";
		
		// 글을 지운후, 사진 삭제
		if(rt.equals("OK")) {
			// 실제 폴더 위치
			String photo_path = request.getSession().getServletContext().getRealPath("/storage");
			// 사진이 있는지 검사
			String photo_id = foretService.checkPhoto(Integer.parseInt(foretDTO.getGroup_photo()));
			
			if(photo_id != null) { // 사진이 있으면 삭제
				PhotoDTO photoDTO = foretService.photoSelect(Integer.parseInt(photo_id));
				photo_path = photoDTO.getPhoto_path() + "/" + photoDTO.getPhoto_name();
				int result = foretService.photoDelete(Integer.parseInt(photo_id), photo_path);
			}
		}
		
		JSONObject json = new JSONObject();
		json.put("rt", rt);
	    
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.addObject("json", json);
	    modelAndView.setViewName("foret.jsp");
	    
	    return modelAndView;
	}
	
	// 포레 수정
	@RequestMapping(value = "/foret/foret_modify.do")
	public ModelAndView foretModify(HttpServletRequest request, MultipartFile photo) throws Exception {
		request.setCharacterEncoding("UTF-8");
		
		String group_name = request.getParameter("group_name");
		String group_profile = request.getParameter("group_profile");
		String group_tag = request.getParameter("group_tag");
		String group_region = request.getParameter("group_region");
		int group_max_member = Integer.parseInt(request.getParameter("group_max_member"));
		int group_no = Integer.parseInt(request.getParameter("group_no"));
		String group_leader = request.getParameter("group_leader");

		ForetDTO foretDTO = foretService.foretSelect(group_no);
		
		if(photo != null) { // 사진도 수정하면
			System.out.println("사진도 수정하면 => " + photo);
			PhotoDTO photoDTO = new PhotoDTO();
			// 사진이 있는지 검사
			String photo_id = foretService.checkPhoto(Integer.parseInt(foretDTO.getGroup_photo()));
			
			if(photo_id != null) { // 기존 포레에 사진이 있으면
				System.out.println("기존포레 사진 있으면 => " + photo_id);
				photoDTO = foretService.photoSelect(Integer.parseInt(photo_id));
				
				String photo_path = photoDTO.getPhoto_path() + "/" + photoDTO.getPhoto_name();
				// 사진을 삭제후 
				int deleteResult = foretService.photoDelete(Integer.parseInt(photo_id), photo_path);
				
				if(deleteResult > 0) { // 사진이 삭제되었으면 새롭게 저장
					System.out.println("삭제 됐으면 새로 저장 => " + photo_id);
					photo_path = request.getSession().getServletContext().getRealPath("/storage");
					String photo_name = photo.getOriginalFilename();
					// 저장할 사진의 확장자를 원본이름에서 추출
					int lastIndex = photo_name.lastIndexOf(".");
					String photo_type = photo_name.substring(lastIndex + 1);
					
					// 사진 복사
					File file = new File(photo_path, photo_name);
			        FileCopyUtils.copy(photo.getInputStream(), new FileOutputStream(file));

					// 사진 dto 저장
					photoDTO.setPhoto_name(photo_name);
					photoDTO.setPhoto_path(photo_path);
					photoDTO.setPhoto_type(photo_type);
					
					int writeResult = foretService.photoWrite(photoDTO);	
					if(writeResult > 0) { 
						int max_seq = foretService.getMaxPhotoId();
						photo_id = foretService.checkPhoto(max_seq);
						foretDTO.setGroup_photo(photo_id);
					}
				}
			} else { // 기존 포레에 사진이 없으면
				System.out.println("기존포레 사진없으면");
				String photo_path = request.getSession().getServletContext().getRealPath("/storage");
				String photo_name = photo.getOriginalFilename();
				// 저장할 사진의 확장자를 원본이름에서 추출
				int lastIndex = photo_name.lastIndexOf(".");
				String photo_type = photo_name.substring(lastIndex + 1);
				
				// 사진 복사
				File file = new File(photo_path, photo_name);
		        FileCopyUtils.copy(photo.getInputStream(), new FileOutputStream(file));

				// 사진 dto 저장
				photoDTO.setPhoto_name(photo_name);
				photoDTO.setPhoto_path(photo_path);
				photoDTO.setPhoto_type(photo_type);
				
				int writeResult = foretService.photoWrite(photoDTO);	
				if(writeResult > 0) { 
					int max_seq = foretService.getMaxPhotoId();
					photo_id = foretService.checkPhoto(max_seq);
					foretDTO.setGroup_photo(photo_id);
				}
			}
		}

		// 포레 DB
		foretDTO.setGroup_name(group_name);
		foretDTO.setGroup_profile(group_profile);
		foretDTO.setGroup_tag(group_tag);
		foretDTO.setGroup_region(group_region);
		foretDTO.setGroup_max_member(group_max_member);
		foretDTO.setGroup_leader(group_leader);
		foretDTO.setGroup_no(group_no);
		
		// 포레 정보 저장
		int ModifyResult = foretService.foretModify(foretDTO);
		
		// json
		String rt = "FAIL";	
		if(ModifyResult > 0) rt = "OK";
		
		// json 출력
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		
		// 화면 네비게이션
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("json", json);
		modelAndView.setViewName("foret.jsp");
		    
		return modelAndView;
	}
	
	// 리더 변경
	@RequestMapping(value = "/foret/foret_change_leader.do")
	public ModelAndView foretChangeLeader(HttpServletRequest request) throws Exception {
		request.setCharacterEncoding("UTF-8");
		
		String new_leader = request.getParameter("new_leader");
		int group_no = Integer.parseInt(request.getParameter("group_no"));
		String group_leader = request.getParameter("group_leader");

		// 리더 변경
		int ModifyResult = foretService.foretChangeLeader(new_leader, group_no, group_leader);
		
		String rt = "FAIL";	
		if(ModifyResult > 0) rt = "OK";
		
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("json", json);
		modelAndView.setViewName("foret.jsp");
		    
		return modelAndView;
	}
	
	// 포레 게시글 추가
	@RequestMapping(value = "/foret/foret_board_write.do")
	public ModelAndView foretBoardWrite(HttpServletRequest request, MultipartFile photo) throws Exception {
		request.setCharacterEncoding("UTF-8");
		
		int group_no = Integer.parseInt(request.getParameter("group_no"));
		ForetDTO foretDTO = foretService.foretSelect(group_no);
		
		int board_type = Integer.parseInt(request.getParameter("board_type"));
		String board_writer = request.getParameter("board_writer");
		String board_subject = request.getParameter("board_subject");
		String board_content = request.getParameter("board_content");
		
		ForetBoardDTO foretBoardDTO = new ForetBoardDTO();
		
		if(photo != null) {
			PhotoDTO photoDTO = new PhotoDTO();
			String photo_path = request.getSession().getServletContext().getRealPath("/storage");
			String photo_name = photo.getOriginalFilename();
			// 저장할 사진의 확장자를 원본이름에서 추출
			int lastIndex = photo_name.lastIndexOf(".");
			String photo_type = photo_name.substring(lastIndex + 1);
			
			// 사진 복사
			File file = new File(photo_path, photo_name);
	        FileCopyUtils.copy(photo.getInputStream(), new FileOutputStream(file));
			
			// 사진 dto 저장
			photoDTO.setPhoto_name(photo_name);
			photoDTO.setPhoto_path(photo_path);
			photoDTO.setPhoto_type(photo_type);
			
			int photoResult = foretService.photoWrite(photoDTO);
			if(photoResult > 0) { 
				int max_seq = foretService.getMaxPhotoId();
				String board_photo_name = foretService.checkPhoto(max_seq);
				foretBoardDTO.setBoard_photo_name(board_photo_name);
			}
		} else {
			foretBoardDTO.setBoard_photo_name("0");
		}
		
		// 포레 게시글 DB
		foretBoardDTO.setGroup_no(foretDTO.getGroup_no());
		foretBoardDTO.setBoard_type(board_type);
		foretBoardDTO.setBoard_writer(board_writer);
		foretBoardDTO.setBoard_subject(board_subject);
		foretBoardDTO.setBoard_content(board_content);
		foretBoardDTO.setBoard_hit(0);
		foretBoardDTO.setBoard_like_count(0);
		foretBoardDTO.setBoard_comment_count(0);

		// 포레 게시글 정보 저장
		int writeResult = foretService.foretBoardWrite(foretBoardDTO);
		
		String rt = "FAIL";	
		if(writeResult > 0) rt = "OK";
		
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("json", json);
		modelAndView.setViewName("foret.jsp");
		    
		return modelAndView;
	}
	
	// 포레 게시글 삭제
	@RequestMapping(value = "/foret/foret_board_delete.do")
	public ModelAndView foretBoardDelete(HttpServletRequest request) throws Exception {
		request.setCharacterEncoding("UTF-8");
		
		int board_no = Integer.parseInt(request.getParameter("board_no"));
		String board_writer = request.getParameter("board_writer");
		
		ForetBoardDTO foretBoardDTO = foretService.foretBoardSelect(board_no);
		
		int deleteResult = foretService.foretBoardDelete(board_no, board_writer);

		String rt = "FAIL";
		if(deleteResult > 0) rt = "OK";
		
		// 글을 지운후, 사진 삭제
		if(rt.equals("OK")) {
			// 실제 폴더 위치
			String photo_path = request.getSession().getServletContext().getRealPath("/storage");
			// 사진이 있는지 검사
			String photo_id = foretService.checkPhoto(Integer.parseInt(foretBoardDTO.getBoard_photo_name()));
			
			if(photo_id != null) { // 사진이 있으면 삭제
				PhotoDTO photoDTO = foretService.photoSelect(Integer.parseInt(photo_id));
				photo_path = photoDTO.getPhoto_path() + "/" + photoDTO.getPhoto_name();
				int result = foretService.photoDelete(Integer.parseInt(photo_id), photo_path);
			}
		}
		
		JSONObject json = new JSONObject();
		json.put("rt", rt);
	    
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.addObject("json", json);
	    modelAndView.setViewName("foret.jsp");
	    
	    return modelAndView;
	}
	
	// 포레 게시글 수정
	@RequestMapping(value = "/foret/foret_board_modify.do")
	public ModelAndView foretBoardModify(HttpServletRequest request, MultipartFile photo) throws Exception {
		request.setCharacterEncoding("UTF-8");
			
		int board_type = Integer.parseInt(request.getParameter("board_type"));
		String board_subject = request.getParameter("board_subject");
		String board_content = request.getParameter("board_content");
		int board_no = Integer.parseInt(request.getParameter("board_no"));
		
		ForetBoardDTO foretBoardDTO = foretService.foretBoardSelect(board_no);
		if(photo != null) { // 사진도 수정하면
			PhotoDTO photoDTO = new PhotoDTO();
			// 사진이 있는지 검사
			String photo_id = foretService.checkPhoto(Integer.parseInt(foretBoardDTO.getBoard_photo_name()));
			
			if(photo_id != null) { // 기존 포레게시글에 사진이 있으면
				photoDTO = foretService.photoSelect(Integer.parseInt(photo_id));
				
				String photo_path = photoDTO.getPhoto_path() + "/" + photoDTO.getPhoto_name();
				// 사진을 삭제후 
				int deleteResult = foretService.photoDelete(Integer.parseInt(photo_id), photo_path);
				
				if(deleteResult > 0) { // 사진이 삭제되었으면 새롭게 저장
					photo_path = request.getSession().getServletContext().getRealPath("/storage");
					String photo_name = photo.getOriginalFilename();
					// 저장할 사진의 확장자를 원본이름에서 추출
					int lastIndex = photo_name.lastIndexOf(".");
					String photo_type = photo_name.substring(lastIndex + 1);
					
					// 사진 복사
					File file = new File(photo_path, photo_name);
			        FileCopyUtils.copy(photo.getInputStream(), new FileOutputStream(file));

					// 사진 dto 저장
					photoDTO.setPhoto_name(photo_name);
					photoDTO.setPhoto_path(photo_path);
					photoDTO.setPhoto_type(photo_type);
					
					int writeResult = foretService.photoWrite(photoDTO);	
					if(writeResult > 0) { 
						int max_seq = foretService.getMaxPhotoId();
						photo_id = foretService.checkPhoto(max_seq);
						foretBoardDTO.setBoard_photo_name(photo_id);
					}
				}
			} else { // 기존 포레에 사진이 없으면
				String photo_path = request.getSession().getServletContext().getRealPath("/storage");
				String photo_name = photo.getOriginalFilename();
				// 저장할 사진의 확장자를 원본이름에서 추출
				int lastIndex = photo_name.lastIndexOf(".");
				String photo_type = photo_name.substring(lastIndex + 1);
				
				// 사진 복사
				File file = new File(photo_path, photo_name);
		        FileCopyUtils.copy(photo.getInputStream(), new FileOutputStream(file));

				// 사진 dto 저장
				photoDTO.setPhoto_name(photo_name);
				photoDTO.setPhoto_path(photo_path);
				photoDTO.setPhoto_type(photo_type);
				
				int writeResult = foretService.photoWrite(photoDTO);	
				if(writeResult > 0) { 
					int max_seq = foretService.getMaxPhotoId();
					photo_id = foretService.checkPhoto(max_seq);
					foretBoardDTO.setBoard_photo_name(photo_id);
				}
			}
		}

		// 포레 게시판 DB
		foretBoardDTO.setBoard_type(board_type);
		foretBoardDTO.setBoard_subject(board_subject);
		foretBoardDTO.setBoard_content(board_content);
		
		// 포레 정보 저장
		int ModifyResult = foretService.foretBoardModify(foretBoardDTO);
		
		// json
		String rt = "FAIL";	
		if(ModifyResult > 0) rt = "OK";
		
		// json 출력
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		
		// 화면 네비게이션
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("json", json);
		modelAndView.setViewName("foret.jsp");
		    
		return modelAndView;
	}
	
	// 목록 보기
	@RequestMapping(value = "/foret/foret_board_list.do")
	public ModelAndView foretBoardList(HttpServletRequest request) throws Exception {
		List<ForetBoardDTO> list = new ArrayList<ForetBoardDTO>();
		int group_no = Integer.parseInt(request.getParameter("group_no"));
		
		list = foretService.foretBoardList(group_no);
		int total = list.size();
		
		String rt = "FAIL";
		if(total > 0) {
			rt = "OK";
		}
		
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		json.put("total", total);
		JSONArray item = new JSONArray();
		
		for(int a=0; a<total; a++) {
			ForetBoardDTO foretBoardDTO = list.get(a);
			JSONObject temp = new JSONObject();
			temp.put("group_no", foretBoardDTO.getGroup_no());
			temp.put("board_no", foretBoardDTO.getBoard_no());
			temp.put("board_type", foretBoardDTO.getBoard_type());
			temp.put("board_writer", foretBoardDTO.getBoard_writer());
			temp.put("board_subject", foretBoardDTO.getBoard_subject());
			temp.put("board_content", foretBoardDTO.getBoard_content());
			temp.put("board_photo_name", foretBoardDTO.getBoard_photo_name());
			temp.put("board_hit", foretBoardDTO.getBoard_hit());
			temp.put("board_like_count", foretBoardDTO.getBoard_like_count());
			temp.put("board_comment_count", foretBoardDTO.getBoard_comment_count());
			temp.put("board_writed_date", foretBoardDTO.getBoard_writed_date());
			temp.put("board_edited_date", foretBoardDTO.getBoard_edited_date());
		
			String photoURL = "";
			if(!foretBoardDTO.getBoard_photo_name().equals("0")) {
				String photo_id = foretService.checkPhoto(
					Integer.parseInt(foretBoardDTO.getBoard_photo_name()));
				
				PhotoDTO photoDTO = foretService.photoSelect(Integer.parseInt(photo_id));
				String photo_name = photoDTO.getPhoto_name();	
				
				photoURL = "http://192.168.0.2:8081/project/storage/" + photo_name;
			}
			temp.put("photo_name", photoURL);	
			item.put(a, temp);
		}
		json.put("item", item);
		
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.addObject("json", json);
	    modelAndView.setViewName("foret.jsp");
	    
	    return modelAndView;
	}
}
