package freeboard.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import freeboard.bean.FreeBoard;
import freeboard.bean.FreeBoardComment;

@Controller
public class FreeBoardController {
	
	// 임선미 작성

	@Autowired
	FreeBoardService service;
	
	//-----------------------------------------------------------게시판 처리-----------------------
	@RequestMapping("/freeboard/writeFreeboard.do")
	public ModelAndView writeFreeboard(HttpServletRequest request) throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		FreeBoard freeBoard = new FreeBoard();
		freeBoard.setMember_id(request.getParameter("member_id"));
		freeBoard.setFreeboard_subject(request.getParameter("freeboard_subject"));
		freeBoard.setFreeboard_content(request.getParameter("freeboard_content"));
		
		int result = service.writeFreeboard(freeBoard);
		
		JSONObject json = new JSONObject();
		String rt = null;
		if(result > 0) {
			rt = "OK";
		} else {
			rt = "FAIL";
		}
		json.put("rt", rt);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("json", json);
		modelAndView.setViewName("freeboard.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping("/freeboard/modifyFreeboard.do")
	public ModelAndView modifyFreeboard(HttpServletRequest request) throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		FreeBoard freeBoard = new FreeBoard();
		freeBoard.setFreeboard_subject(request.getParameter("freeboard_subject"));
		freeBoard.setFreeboard_content(request.getParameter("freeboard_content"));
		freeBoard.setFreeboard_no(Integer.parseInt(request.getParameter("freeboard_no")));
		freeBoard.setMember_id(request.getParameter("member_id"));
		
		int result = service.modifyFreeboard(freeBoard);
		
		JSONObject json = new JSONObject();
		String rt = null;
		if(result > 0) {
			rt = "OK";
		} else {
			rt = "FAIL";
		}
		json.put("rt", rt);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("json", json);
		modelAndView.setViewName("freeboard.jsp");
		
		return modelAndView;
	}
	
	
	
	@RequestMapping("/freeboard/deleteFreeboard.do")
	public ModelAndView deleteFreeboard(HttpServletRequest request) throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		int freeboard_no = Integer.parseInt(request.getParameter("freeboard_no"));
		String member_id = request.getParameter("member_id");
		
		int result = service.deleteFreeboard(freeboard_no, member_id);
		
		JSONObject json = new JSONObject();
		String rt = null;
		if(result > 0) {
			rt = "OK";
			service.deleteCommentALL(freeboard_no);
		} else {
			rt = "FAIL";
		}
		json.put("rt", rt);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("json", json);
		modelAndView.setViewName("freeboard.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping("/freeboard/readFreeboard.do")
	public ModelAndView readFreeboard(HttpServletRequest request) throws Exception {
		
		int freeboard_no = Integer.parseInt(request.getParameter("freeboard_no"));
		String member_id = request.getParameter("member_id"); //좋아요 상태때문에 필요
		
		service.updateHitFreeboard(freeboard_no);
		FreeBoard freeBoard = service.readFreeboard(freeboard_no);
		
		JSONObject json = new JSONObject();
		String rt = null;
		if(freeBoard != null) {
			rt = "OK";
			JSONArray item = new JSONArray();
			JSONObject object = new JSONObject();
			object.put("freeboard_no", freeBoard.getFreeboard_no());
			object.put("member_id", freeBoard.getMember_id());
			object.put("freeboard_subject", freeBoard.getFreeboard_subject());
			object.put("freeboard_content", freeBoard.getFreeboard_content());
			object.put("freeboard_hit", freeBoard.getFreeboard_hit());
			object.put("freeboard_like_count", freeBoard.getFreeboard_like_count());
			object.put("freeboard_comment_count", freeBoard.getFreeboard_comment_count());
			object.put("freeboard_write_date", freeBoard.getFreeboard_write_date());
			object.put("freeboard_edit_date", freeBoard.getFreeboard_edit_date());
			boolean isLiked = service.isLiked(member_id, freeBoard.getFreeboard_no());
			object.put("like_state", isLiked);
			item.put(0, object);
			json.put("item", item);
		} else {
			rt = "FAIL";
		}
		json.put("rt", rt);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("json", json);
		modelAndView.setViewName("freeboard.jsp");
		
		return modelAndView;
	}
	
	
	@RequestMapping("/freeboard/listFreeboard.do")
	//정렬코드(SORTCODE- 0(최신순), 1(공감순), 2(댓글순))를 파라미터로 전달받아서 정렬하기(목록은 10개씩 받아옴)
	public ModelAndView listFreeboard(HttpServletRequest request) throws Exception {
		
		String member_id = request.getParameter("member_id"); //좋아요 상태때문에 필요
		int SORTCODE = Integer.parseInt(request.getParameter("SORTCODE"));
		int page = Integer.parseInt(request.getParameter("page"));
		
	    int startNum = (page-1)*10+1;
	    int endNum = startNum+9;
	    
		List<FreeBoard> list = new ArrayList();
		String sort = null;
		switch (SORTCODE) {
		case 0 :
			sort = "최신";
			list = service.listDateFreeboard(startNum, endNum);
			break;
		case 1 :
			sort = "공감";
			list = service.listLikeFreeboard(startNum, endNum);
			break;
		case 2 :
			sort = "댓글";
			list = service.listReplyFreeboard(startNum, endNum);
			break;
		}
		
		JSONObject json = new JSONObject();
		String rt = null;
		if(list.size()>0) {
			rt = "OK";
			JSONArray item = new JSONArray();
			for(int a=0; a<list.size(); a++) {
				FreeBoard freeBoard = list.get(a);
				JSONObject object = new JSONObject();
				object.put("freeboard_no", freeBoard.getFreeboard_no());
				object.put("member_id", freeBoard.getMember_id());
				object.put("freeboard_subject", freeBoard.getFreeboard_subject());
				object.put("freeboard_content", freeBoard.getFreeboard_content());
				object.put("freeboard_hit", freeBoard.getFreeboard_hit());
				object.put("freeboard_like_count", freeBoard.getFreeboard_like_count());
				object.put("freeboard_comment_count", freeBoard.getFreeboard_comment_count());
				object.put("freeboard_write_date", freeBoard.getFreeboard_write_date());
				object.put("freeboard_edit_date", freeBoard.getFreeboard_edit_date());
				boolean isLiked = service.isLiked(member_id, freeBoard.getFreeboard_no());
				object.put("like_state", isLiked);
				item.put(a, object);
			}
			json.put("item", item);	
		} else {
			rt = "FAIL";
		}
		json.put("rt", rt);
		json.put("sort", sort);
		json.put("total", list.size());
		json.put("page", page);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("json", json);
		modelAndView.setViewName("freeboard.jsp");
		
		return modelAndView;
	}
	
	//좋아요 처리 : 1.좋아요상태를 변화시킬 글 번호와 회원번호를 파라미터로 받아야 한다.
	//			2.좋아요를 증가(1;클릭)시킬 것인지, 감소(언클릭;-1)시킬 것인지 구분하는 코드(LIKECLICK)를 파라미터로 받아야 한다.
	//			3.좋아요 상태를 저장해야 한다. (개인데이터에 저장+글 정보 변경)
	@RequestMapping("/freeboard/likeChange.do")
	public ModelAndView likeChange(HttpServletRequest request) throws Exception {
		
		int seq = Integer.parseInt(request.getParameter("seq"));
		int LIKECLICK = Integer.parseInt(request.getParameter("LIKECLICK"));
		String member_id = request.getParameter("member_id");
		
		String rt = null;
		int result_1 = 0;
		int result_2 = 0;
		if(LIKECLICK == 1) {
			result_1 = service.clickLike(member_id, seq);
			result_2 = service.updateLikePlusFreeboard(seq);
		} else if (LIKECLICK == -1) {
			result_1 = service.unClickLike(member_id, seq);
			result_2 = service.updateLikeMinusFreeboard(seq);
		} else {
			rt="FAIL";
		}
		
		if(result_1>0 && result_2>0) {
			rt="OK";
		} else {
			rt="FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		json.put("boardtype", 0); // boardtype=0 ->익명게시판
		json.put("seq", seq);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("json", json);
		modelAndView.setViewName("freeboard.jsp");
		
		return modelAndView;
	}
	
	//-----------------------------------------------------------게시판 댓글 처리------------------
	@RequestMapping("/freeboard/writeComment.do")
	public ModelAndView writeComment(HttpServletRequest request) {
		
		FreeBoardComment freeBoardComment = new FreeBoardComment();
		freeBoardComment.setFreeboard_no(Integer.parseInt(request.getParameter("freeboard_no")));
		freeBoardComment.setFreeboard_comment_writer(request.getParameter("freeboard_comment_writer"));
		freeBoardComment.setFreeboard_writer(request.getParameter("freeboard_writer"));
		freeBoardComment.setFreeboard_comment_content(request.getParameter("freeboard_comment_content"));
		
		int result = service.writeComment(freeBoardComment);
		String rt = null;
		if (result>0) {
			service.updateReplyPlusFreeboard(freeBoardComment.getFreeboard_no());
			rt = "OK";
		} else {
			rt = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("json", json);
		modelAndView.setViewName("freeboard.jsp");
		
		return modelAndView;
		
	}
	
	@RequestMapping("/freeboard/deleteComment.do")
	public ModelAndView deleteComment(HttpServletRequest request) {
		
		int freeboard_no = Integer.parseInt(request.getParameter("freeboard_no"));
		int freeboard_comment_no = Integer.parseInt(request.getParameter("freeboard_comment_no"));
		String freeboard_comment_writer = request.getParameter("freeboard_comment_writer");
		
		int result = service.deleteComment(freeboard_comment_no, freeboard_comment_writer);
		String rt = null;
		if(result>0) {
			service.updateReplyMinusFreeboard(freeboard_no);
			rt = "OK";
		} else {
			rt = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("json", json);
		modelAndView.setViewName("freeboard.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping("/freeboard/modifyComment.do")
	public ModelAndView modifyComment(HttpServletRequest request) {
		
		int freeboard_comment_no = Integer.parseInt(request.getParameter("freeboard_comment_no"));
		String freeboard_comment_content = request.getParameter("freeboard_comment_content");
		String freeboard_comment_writer = request.getParameter("freeboard_comment_writer");
		
		int result = service.modifyComment(freeboard_comment_content, freeboard_comment_no, freeboard_comment_writer);
		String rt = null;
		if(result>0) {
			rt = "OK";
		} else {
			rt = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("rt", rt);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("json", json);
		modelAndView.setViewName("freeboard.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping("/freeboard/commentList.do")
	public ModelAndView commentList(HttpServletRequest request) {
		
		int freeboard_no = Integer.parseInt(request.getParameter("freeboard_no"));
		
		List<FreeBoardComment> list = service.commentList(freeboard_no);
		
		JSONObject json = new JSONObject();
		String rt = null;
		if(list.size()>0) {
			rt = "OK";
			JSONArray item = new JSONArray();
			for(int a=0; a<list.size(); a++) {
				FreeBoardComment freeBoardComment = list.get(a);
				JSONObject object = new JSONObject();
				object.put("freeboard_comment_no", freeBoardComment.getFreeboard_comment_no());
				object.put("freeboard_comment_writer", freeBoardComment.getFreeboard_comment_no());
				object.put("freeboard_comment_content", freeBoardComment.getFreeboard_comment_content());
				object.put("freeboard_comment_date", freeBoardComment.getFreeboard_comment_date());
				item.put(a, object);
			}
			json.put("item", item);
		} else {
			rt = "FAIL";
		}
		json.put("rt", rt);
		json.put("freeboard_no", freeboard_no);
		json.put("total", list.size()); //댓글 수
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("json", json);
		modelAndView.setViewName("freeboard.jsp");
		
		return modelAndView;
	}
	
	//대댓글 추가하기
	
}
