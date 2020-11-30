package board.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import board.bean.BoardDTO;
import photo.bean.PhotoDTO;
import photo.controller.PhotoService;

@Controller
public class BoardController {
	@Autowired
	BoardService boardService;
	@Autowired
	PhotoService photoService;
	
	@RequestMapping(value = "/board/board_insert.do")
	public ModelAndView boardWrite(HttpServletRequest request, MultipartFile[] photo) throws Exception {
		System.out.println("-- 함수 실행 : board_insert.do --");
		request.setCharacterEncoding("UTF-8");
		
		String boardRT = "FAIL";
		String boardPhotoRT = "FAIL";
		
		// 보드 등록 후 보드 아이디 가져오기
		int board_id = insertBoard(request);
		
		boardRT = getResult(board_id);
		if(boardRT.equals("OK")) {
			// 사진 등록
			boardPhotoRT = getResult(insertBoardPhoto(board_id, request, photo));
		}
		
		JSONObject json = new JSONObject();
		json.put("boardRT", boardRT);
		json.put("boardPhotoRT", boardPhotoRT);
		
		System.out.println("-- 함수 종료 : board_insert.do --\n");
		return modelAndView(json);
	}
	@RequestMapping(value = "/board/board_modify.do")
	public ModelAndView boardModify(HttpServletRequest request, MultipartFile[] photo) throws Exception {
		System.out.println("-- 함수 실행 : board_modify.do --");
		request.setCharacterEncoding("UTF-8");
	    
		String boardRT = "FAIL"; 
		String boardPhotoRT = "FAIL";
		
		// 회원 수정
		boardRT = getResult(modifyBoard(request));
		if(boardRT.equals("OK")) {
			boardPhotoRT = getResult(modifyBoardPhoto(request, photo));
	    }
	    
	    JSONObject json = new JSONObject();
	    json.put("boardRT", boardRT);
	    json.put("boardPhotoRT", boardPhotoRT);
	      
		System.out.println("-- 함수 종료 : board_modify.do --\n");
		return modelAndView(json);
	}

	@RequestMapping(value = "/board/board_delete.do")
	public ModelAndView boardDelete(HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("-- 함수 실행 : board_delete.do --");
		request.setCharacterEncoding("UTF-8");
		String boardRT = "FAIL";
		boardRT = getResult(deleteBoard(request));
		JSONObject json = new JSONObject();
	    json.put("boardRT", boardRT);
		System.out.println("-- 함수 종료 : board_delete.do --\n");
		return modelAndView(json);
	}

	public int insertBoard(HttpServletRequest request) {
		System.out.println("함수 실행 : insertBoard");
		int board_id = 0;
		// 기본 정보
		int writer = haveId(request.getParameter("writer"));
		int foret_id = haveId(request.getParameter("foret_id"));
		int type = haveId(request.getParameter("type"));
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		// 보드 작성
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setWriter(writer);
		if(foret_id != 0) {
			boardDTO.setForet_id(foret_id);
		}
		boardDTO.setType(type);
		boardDTO.setSubject(subject);
		boardDTO.setContent(content);
		
		boardService.boardWrite(boardDTO);
		board_id = boardDTO.getId();
		System.out.println("함수 종료 : insertBoard");
		return board_id;
	}
	public int insertBoardPhoto(int foret_id, HttpServletRequest request, MultipartFile[] photo) throws Exception {
		System.out.println("함수 실행 : insertBoardPhoto");
		int result = 0;
		if(photo.length > 0) {
			result = boardPhotoWrite(foret_id, request, photo);
		}
		System.out.println("함수 종료 : insertBoardPhoto");
		return result;
	}

	public int modifyBoard(HttpServletRequest request) {
		System.out.println("함수 실행 : modifyBoard");
		int result = 0;
		// 기본 정보
		int id = haveId(request.getParameter("id"));
		int writer = haveId(request.getParameter("writer"));
		int foret_id = haveId(request.getParameter("foret_id"));
		int type = haveId(request.getParameter("type"));
		int hit = haveId(request.getParameter("hit"));
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		// 보드 작성
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setId(id);
		boardDTO.setWriter(writer);
		if(foret_id != 0) {
			boardDTO.setForet_id(foret_id);
		}
		boardDTO.setType(type);
		boardDTO.setHit(hit);
		boardDTO.setSubject(subject);
		boardDTO.setContent(content);
		
		result = boardService.boardModify(boardDTO);
	    System.out.println("함수 종료 : modifyBoard");
		return result;
	}
	public int modifyBoardPhoto(HttpServletRequest request, MultipartFile[] photo) throws Exception {
		System.out.println("함수 실행 : modifyBoardPhoto");
		int foret_id = haveId(request.getParameter("id"));
		int result = 0;
		// 삭제 여부 알 수 없음
		boardPhotoDelete(foret_id);
		if(photo.length > 0) {
			result = boardPhotoWrite(foret_id, request, photo);
		}
		System.out.println("함수 종료 : modifyBoardPhoto");
		return result;
	}
	
	public int deleteBoard(HttpServletRequest request) {
		System.out.println("함수 실행 : deleteBoard");
		int board_id = haveId(request.getParameter("id"));
		int result = 0;
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setId(board_id);
		result = boardService.boardDelete(boardDTO);
		System.out.println("함수 종료 : deleteBoard");
		return result;
	}
	
	public int boardPhotoWrite(int foret_id, HttpServletRequest request, MultipartFile[] photos) throws Exception {
		System.out.println("함수 실행 : boardPhotoWrite");
		int result = 0;
		String dir = request.getSession().getServletContext().getRealPath("/storage");
		List<PhotoDTO> list = new ArrayList<PhotoDTO>();
		for(MultipartFile photo : photos) {
			String originname = photo.getOriginalFilename();	
			String filename = photo.getOriginalFilename();
			int lastIndex = originname.lastIndexOf(".");
	        String filetype = originname.substring(lastIndex + 1);
	        int filesize = (int)photo.getSize();
	        File file = new File(dir, filename);
	        FileCopyUtils.copy(photo.getInputStream(), new FileOutputStream(file));
	        
	        PhotoDTO photoDTO = new PhotoDTO();
	        photoDTO.setDir(dir);
	        photoDTO.setOriginname(originname);
	        photoDTO.setFilename(filename);
	        photoDTO.setFiletype(filetype);
	        photoDTO.setFilesize(filesize);
	        photoDTO.setReference_id(foret_id);
	        list.add(photoDTO);
		}
        result = photoService.boardPhotoWrite(list);
        System.out.println("함수 종료 : boardPhotoWrite");
		return result;
	}
	public int boardPhotoDelete(int foret_id) {
		System.out.println("함수 실행 : boardPhotoDelete");
		System.out.println("함수 종료 : boardPhotoDelete");
		return photoService.boardPhotoDelete(foret_id);
	}
	
	public int haveId(String id) {
		System.out.println("함수 실행 : haveId");
		if(id == null || id.equals("")) {
			System.out.println("함수 종료 : haveId");
			return 0;
		} else {
			System.out.println("함수 종료 : haveId");
			return Integer.parseInt(id);
		}
	}
	public String getResult(int result) {
		return result > 0 ? "OK" : "FAIL";
	}
	public ModelAndView modelAndView(JSONObject json) {
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.addObject("json", json);
	    modelAndView.setViewName("board.jsp");
	    return modelAndView;
	}
}
