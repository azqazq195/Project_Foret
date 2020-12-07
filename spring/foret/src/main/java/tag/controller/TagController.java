package tag.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import tag.bean.TagDTO;

@Controller
public class TagController {
	@Autowired
	TagService tagService;
	
	@RequestMapping(value = "/tag/tag_insert.do")
	public ModelAndView tagWrite(HttpServletRequest request) throws Exception {
		request.setCharacterEncoding("UTF-8");
		
	    String tag_name = request.getParameter("tag_name");
	    TagDTO tagDTO = new TagDTO();
	    tagDTO.setTag_name(tag_name);
	    
	    int tagResult = tagService.tagWrite(tagDTO);
	    
	    String RT = "FAIL";   
	    if(tagResult > 0) RT = "OK";
	    
	    JSONObject json = new JSONObject();
	    json.put("RT", RT);
	    
	    // 화면 네비게이션
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.addObject("json", json);
	    modelAndView.setViewName("tag.jsp");
	       
	    return modelAndView;
	}
	
	@RequestMapping(value = "/tag/tag_delete.do")
	public ModelAndView tagDelete(HttpServletRequest request) throws Exception {
		request.setCharacterEncoding("UTF-8");
		
		int tag_id = 0;
		String tag_id_string = request.getParameter("tag_id");
		if(tag_id_string != null) {
			tag_id = Integer.parseInt(tag_id_string);
		}
	    String tag_name = request.getParameter("tag_name");
	    
	    TagDTO tagDTO = new TagDTO();
	    tagDTO.setTag_id(tag_id);
	    tagDTO.setTag_name(tag_name);
	    
	    int tagResult = tagService.tagDelete(tagDTO);
	    
	    String RT = "FAIL";   
	    if(tagResult > 0) RT = "OK";
	    
	    JSONObject json = new JSONObject();
	    json.put("RT", RT);
	    
	    // 화면 네비게이션
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.addObject("json", json);
	    modelAndView.setViewName("tag.jsp");
	       
	    return modelAndView;
	}
	
	@RequestMapping(value = "/tag/tag_list.do")
	public ModelAndView tagList(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : tag_list --");
		request.setCharacterEncoding("UTF-8");
		
		List<TagDTO> list = tagService.tagList(); 
		
		int total = list.size();
		
		JSONObject json = new JSONObject();
	    json.put("total", total);
	    
	    JSONArray array = new JSONArray();
	    for(TagDTO tagDTO : list) {
	    	JSONObject temp = new JSONObject();
	    	temp.put("tag_id", tagDTO.getTag_id());
	    	temp.put("tag_name", tagDTO.getTag_name());
	    	array.put(temp);
	    }
	    json.put("tag", array);
	    
	    // 화면 네비게이션
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.addObject("json", json);
	    modelAndView.setViewName("tag.jsp");
	    System.out.println("-- 함수 종료 : tag_list --");
	    return modelAndView;
	}
	
	@RequestMapping(value = "/tag/tag_rank.do")
	public ModelAndView tagRank(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : tag_rank --");
		request.setCharacterEncoding("UTF-8");
		
		int rank = haveId(request.getParameter("rank"));
		List<TagDTO> list = tagService.tagRank(rank); 
		
		int total = list.size();
		
		JSONObject json = new JSONObject();
	    json.put("total", total);
	    
	    JSONArray array = new JSONArray();
	    for(TagDTO tagDTO : list) {
	    	JSONObject temp = new JSONObject();
	    	temp.put("cnt", tagDTO.getCnt());
	    	temp.put("tag_id", tagDTO.getTag_id());
	    	temp.put("tag_name", tagDTO.getTag_name());
	    	array.put(temp);
	    }
	    json.put("tag", array);
	    
	    // 화면 네비게이션
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.addObject("json", json);
	    modelAndView.setViewName("tag.jsp");
	    System.out.println("-- 함수 종료 : tag_rank --");
	    return modelAndView;
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
}
