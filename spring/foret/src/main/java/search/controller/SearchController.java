package search.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import member.bean.MemberDTO;
import member.bean.MemberForetDTO;
import member.bean.MemberLikeDTO;
import member.bean.MemberRegionDTO;
import member.bean.MemberTagDTO;

@Controller
public class SearchController {
	@Autowired
	SearchService searchService;
	@RequestMapping(value = "/search/check_email.do")
	public ModelAndView emailCheck(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : check_email.do --");
		request.setCharacterEncoding("UTF-8");
		
		String RT = "OK";
		MemberDTO memberDTO = searchService.memberSelect(request.getParameter("email"));
		if(memberDTO != null) {
			RT = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("RT", RT);
		System.out.println("-- 함수 실행 : check_email.do --");
		return modelAndView(json);
	}
	@RequestMapping(value = "/search/search_member.do")
	public ModelAndView memberSelect(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : search_member.do --");
		request.setCharacterEncoding("UTF-8");
		
		String RT = "FAIL";

		System.out.println("멤버, 사진 정보 불러오기");
		MemberDTO memberDTO = searchService.memberSelect(request.getParameter("email"));
		
		JSONObject json = new JSONObject();
		if(memberDTO != null) {
			RT = "OK";
			System.out.println("멤버_태그 정보 불러오기");
			List<MemberTagDTO> tagList= searchService.memberTagSelect(memberDTO.getId());
			System.out.println("멤버_지역 정보 불러오기");
			List<MemberRegionDTO> regionList = searchService.memberRegionSelect(memberDTO.getId());
			System.out.println("멤버_보드_좋아요 정보 불러오기");
			List<MemberLikeDTO> boardLikeList = searchService.memberBoardLikeSelect(memberDTO.getId());
			System.out.println("멤버_댓글_좋아요 사진 정보 불러오기");
			List<MemberLikeDTO> commentLikeList = searchService.memberCommentLikeSelect(memberDTO.getId());
			System.out.println("멤버_포레 정보 불러오기");
			List<MemberForetDTO> memberForetList = searchService.memberForetSelect(memberDTO.getId());
			json.put("RT", RT);
			JSONArray member = new JSONArray();
			JSONObject memberTemp = new JSONObject();
			memberTemp.put("id", memberDTO.getId());
			memberTemp.put("name", memberDTO.getName());
			memberTemp.put("email", memberDTO.getEmail());
			memberTemp.put("password", memberDTO.getPassword());
			memberTemp.put("ninckname", memberDTO.getNickname());
			memberTemp.put("birth", memberDTO.getBirth());
			memberTemp.put("reg_date", memberDTO.getReg_date());
			memberTemp.put("photo", memberDTO.getPhoto());
			
			if(tagList != null) {
				JSONArray tag = new JSONArray();
				for(MemberTagDTO memberTagDTO : tagList) {
					tag.put(memberTagDTO.getTag_name());
				}
				memberTemp.put("tag", tag);
			}
			if(regionList != null) {
				JSONArray region_si = new JSONArray();
				JSONArray region_gu = new JSONArray();
				for(MemberRegionDTO memberRegionDTO : regionList) {
					region_si.put(memberRegionDTO.getRegion_si());
					region_gu.put(memberRegionDTO.getRegion_gu());
				}
				memberTemp.put("region_si", region_si);
				memberTemp.put("region_gu", region_gu);
			}
			if(boardLikeList != null) {
				JSONArray like_board = new JSONArray();
				for(MemberLikeDTO memberLikeDTO : boardLikeList) {
					like_board.put(memberLikeDTO.getBoard_id());
				}
				memberTemp.put("like_board", like_board);
			}
			if(commentLikeList != null) {
				JSONArray like_comment = new JSONArray();
				for(MemberLikeDTO memberLikeDTO : commentLikeList) {
					like_comment.put(memberLikeDTO.getComment_id());
				}
				memberTemp.put("like_comment", like_comment);
			}
			if(memberForetList != null) {
				JSONArray member_foret = new JSONArray();
				for(MemberForetDTO memberForetDTO : memberForetList) {
					member_foret.put(memberForetDTO.getForet_id());
				}
				memberTemp.put("member_foret", member_foret);
			}
			member.put(memberTemp);
			json.put("member", member);
		} else {
			json.put("RT", RT);
		}
		
		System.out.println("-- 함수 종료 : search_member.do --\n");
		return modelAndView(json);
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
	    modelAndView.setViewName("search.jsp");
	    return modelAndView;
	}
}
