package search.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import member.bean.MemberDTO;

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
	public ModelAndView commentWrite(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : search_member.do --");
		request.setCharacterEncoding("UTF-8");
		
		String RT = "FAIL";

		MemberDTO memberDTO = searchService.memberSelect(request.getParameter("email"));
		
		JSONObject json = new JSONObject();
		if(memberDTO != null) {
			RT = "OK";
			json.put("RT", RT);
			JSONArray array = new JSONArray();
			JSONObject temp = new JSONObject();
			temp.put("id", memberDTO.getId());
			temp.put("name", memberDTO.getName());
			temp.put("email", memberDTO.getEmail());
			temp.put("password", memberDTO.getPassword());
			temp.put("ninckname", memberDTO.getNickname());
			temp.put("birth", memberDTO.getBirth());
			temp.put("reg_date", memberDTO.getReg_date());
			temp.put("photo", memberDTO.getPhoto());
			array.put(temp);
			json.put("member", array);
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
