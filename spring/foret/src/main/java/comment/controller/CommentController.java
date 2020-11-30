package comment.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import comment.bean.CommentDTO;

@Controller
public class CommentController {
	@Autowired
	CommentService commentService;
	
	@RequestMapping(value = "/comment/comment_insert.do")
	public ModelAndView commentWrite(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : comment_insert.do --");
		request.setCharacterEncoding("UTF-8");
		
		String commentRT = "FAIL";

		commentRT = getResult(insertComment(request));
		
		JSONObject json = new JSONObject();
		json.put("commentRT", commentRT);
		
		System.out.println("-- 함수 종료 : comment_insert.do --\n");
		return modelAndView(json);
	}
	@RequestMapping(value = "/comment/comment_modify.do")
	public ModelAndView commentModify(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : comment_modify.do --");
		request.setCharacterEncoding("UTF-8");
		
		String commentRT = "FAIL";
		
		commentRT = getResult(modifyComment(request));

		JSONObject json = new JSONObject();
	    json.put("commentRT", commentRT);
	    
		System.out.println("-- 함수 실행 : comment_modify.do --\n");
		return modelAndView(json);
	}
	@RequestMapping(value = "/comment/comment_delete.do")
	public ModelAndView commentDelete(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : comment_delete.do --");
		request.setCharacterEncoding("UTF-8");
		String commentRT = "FAIL";
		int groupNoCnt = getReCommentCnt(haveId(request.getParameter("comment_id")));
		if(groupNoCnt > 1) {
			commentRT = getResult(nullComment(request));
		} else {
			commentRT = getResult(deleteComment(request));
		}
		JSONObject json = new JSONObject();
	    json.put("commentRT", commentRT);
		System.out.println("-- 함수 종료 : comment_delete.do --\n");
		return modelAndView(json);
	}
	@RequestMapping(value = "/comment/recomment_insert.do")
	public ModelAndView reCommentWrite(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : recomment_insert.do --");
		request.setCharacterEncoding("UTF-8");
		
		String commentRT = "FAIL";

		commentRT = getResult(insertReComment(request));
		
		JSONObject json = new JSONObject();
		json.put("commentRT", commentRT);
		
		System.out.println("-- 함수 종료 : recomment_insert.do --\n");
		return modelAndView(json);
	}
	@RequestMapping(value = "/comment/recomment_modify.do")
	public ModelAndView reCommentModify(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : recomment_modify.do --");
		request.setCharacterEncoding("UTF-8");
		
		String commentRT = "FAIL";
		
		commentRT = getResult(modifyReComment(request));

		JSONObject json = new JSONObject();
	    json.put("commentRT", commentRT);
	    
		System.out.println("-- 함수 실행 : recomment_modify.do --\n");
		return modelAndView(json);
	}
	@RequestMapping(value = "/comment/recomment_delete.do")
	public ModelAndView reCommentDelete(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : recomment_delete.do --");
		request.setCharacterEncoding("UTF-8");
		String commentRT = "FAIL";

		commentRT = getResult(deleteReComment(request));
		
		JSONObject json = new JSONObject();
	    json.put("commentRT", commentRT);
		System.out.println("-- 함수 종료 : recomment_delete.do --\n");
		return modelAndView(json);
	}
	
	public int insertComment(HttpServletRequest request) {
		System.out.println("함수 실행 : insertComment");
		int result = 0;
		
		int board_id = haveId(request.getParameter("board_id"));
		int writer = haveId(request.getParameter("writer"));
		String content = request.getParameter("content");
		
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setBoard_id(board_id);
		commentDTO.setWriter(writer);
		commentDTO.setContent(content);
		
		result = commentService.commentWrite(commentDTO);
		System.out.println("함수 종료 : insertComment");
		return result;
	}
	public int insertReComment(HttpServletRequest request) {
		System.out.println("함수 실행 : insertReComment");
		int result = 0;
		
		int comment_id = haveId(request.getParameter("comment_id"));
		int board_id = haveId(request.getParameter("board_id"));
		int writer = haveId(request.getParameter("writer"));
		String content = request.getParameter("content");
		
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setId(comment_id);
		commentDTO.setBoard_id(board_id);
		commentDTO.setWriter(writer);
		commentDTO.setContent(content);
		
		result = commentService.reCommentWrite(commentDTO);
		System.out.println("함수 종료 : insertReComment");
		return result;
	}
	
	public int modifyComment(HttpServletRequest request) {
		System.out.println("함수 실행 : modifyComment");
		int result = 0;
		
		int comment_id = haveId(request.getParameter("comment_id"));
		String content = request.getParameter("content");
		
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setId(comment_id);
		commentDTO.setContent(content);
		
		result = commentService.commentModify(commentDTO);
		System.out.println("함수 종료 : modifyComment");
		return result;
	}
	public int modifyReComment(HttpServletRequest request) {
		System.out.println("함수 실행 : modifyReComment");
		int result = 0;
		
		int comment_id = haveId(request.getParameter("comment_id"));
		String content = request.getParameter("content");
		
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setId(comment_id);
		commentDTO.setContent(content);
		
		result = commentService.reCommentModify(commentDTO);
		System.out.println("함수 종료 : modifyReComment");
		return result;
	}

	public int deleteComment(HttpServletRequest request) {
		System.out.println("함수 실행 : deleteComment");
		int comment_id = haveId(request.getParameter("comment_id"));
		int result = 0;
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setId(comment_id);
		result = commentService.commentDelete(commentDTO);
		System.out.println("함수 종료 : deleteComment");
		return result;
	}
	public int deleteReComment(HttpServletRequest request) {
		System.out.println("함수 실행 : deleteReComment");
		int comment_id = haveId(request.getParameter("comment_id"));
		int result = 0;
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setId(comment_id);
		result = commentService.reCommentDelete(commentDTO);
		System.out.println("함수 종료 : deleteReComment");
		return result;
	}
	public int nullComment(HttpServletRequest request) {
		System.out.println("함수 실행 : nullComment");
		int comment_id = haveId(request.getParameter("comment_id"));
		int result = 0;
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setId(comment_id);
		result = commentService.commentNull(commentDTO);
		System.out.println("함수 종료 : nullComment");
		return result;
	}
	
	public int getReCommentCnt(int group_no) {
		System.out.println("함수 실행 : getReCommentCnt");
		int result = 0;
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setGroup_no(group_no);
		result = commentService.getReCommentCnt(commentDTO);
		System.out.println("함수 종료 : getReCommentCnt");
		return result;
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
	    modelAndView.setViewName("comment.jsp");
	    return modelAndView;
	}
}
