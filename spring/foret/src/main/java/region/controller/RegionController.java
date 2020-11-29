package region.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import region.bean.RegionDTO;

@Controller
public class RegionController {
	@Autowired
	RegionService regionService;
	
	@RequestMapping(value = "/region/region_insert.do")
	public ModelAndView regionWrite(HttpServletRequest request) throws Exception {
		request.setCharacterEncoding("UTF-8");
		
		String region_si = request.getParameter("region_si");
		String region_gu = request.getParameter("region_gu");
		RegionDTO regionDTO = new RegionDTO();
		regionDTO.setRegion_si(region_si);
		regionDTO.setRegion_gu(region_gu);
		
		int regionResult = regionService.regionWrite(regionDTO);
		
		String RT = "FAIL";
		if(regionResult > 0) RT = "OK";
		
	    JSONObject json = new JSONObject();
	    json.put("RT", RT);
	    
	    // 화면 네비게이션
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.addObject("json", json);
	    modelAndView.setViewName("region.jsp");
	       
	    return modelAndView;
	}
	
	@RequestMapping(value = "/region/region_delete.do")
	public ModelAndView regionDelete(HttpServletRequest request) throws Exception {
		request.setCharacterEncoding("UTF-8");
		
		int region_id = 0;
		String region_id_string = request.getParameter("region_id");
		if(region_id_string != null) {
			region_id = Integer.parseInt(region_id_string);
		}
		String region_si = request.getParameter("region_si");
		String region_gu = request.getParameter("region_gu");
		
		RegionDTO regionDTO = new RegionDTO();
		regionDTO.setRegion_id(region_id);
		regionDTO.setRegion_si(region_si);
		regionDTO.setRegion_gu(region_gu);
		
		int regionResult = regionService.regionDelete(regionDTO);
		
		String RT = "FAIL";   
	    if(regionResult > 0) RT = "OK";
	    
	    JSONObject json = new JSONObject();
	    json.put("RT", RT);
	    
	    // 화면 네비게이션
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.addObject("json", json);
	    modelAndView.setViewName("region.jsp");
	       
	    return modelAndView;
	}
	
	@RequestMapping(value = "/region/region_list.do")
	public ModelAndView regionList(HttpServletRequest request) throws Exception {
		request.setCharacterEncoding("UTF-8");
		
		List<RegionDTO> list = regionService.regionList();
		
		int total = list.size();
		
		JSONObject json = new JSONObject();
	    json.put("total", total);
	    
	    JSONArray array = new JSONArray();
	    for(RegionDTO regionDTO : list) {
	    	JSONObject temp = new JSONObject();
	    	temp.put("region_id", regionDTO.getRegion_id());
	    	temp.put("region_si", regionDTO.getRegion_si());
	    	temp.put("region_gu", regionDTO.getRegion_gu());
	    	array.put(temp);
	    }
	    json.put("regions", array);
	    
	    // 화면 네비게이션
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.addObject("json", json);
	    modelAndView.setViewName("region.jsp");
	       
	    return modelAndView;
	}
}
