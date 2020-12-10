package foret.controller;

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

import foret.bean.ForetDTO;
import foret.bean.ForetMemberDTO;
import foret.bean.ForetRegionDTO;
import foret.bean.ForetTagDTO;
import photo.bean.PhotoDTO;
import photo.controller.PhotoService;
import region.bean.RegionDTO;
import region.controller.RegionService;
import tag.bean.TagDTO;
import tag.controller.TagService;

@Controller
public class ForetController {
	@Autowired
	ForetService foretService;
	@Autowired
	PhotoService photoService;
	@Autowired
	TagService tagService;
	@Autowired
	RegionService regionService;
	
	@RequestMapping(value = "/foret/foret_insert.do")
	public ModelAndView foretWrite(HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("-- 함수 실행 : foret_insert.do --");
		request.setCharacterEncoding("UTF-8");
		
		String foretRT = "FAIL"; 
		String foretTagRT = "FAIL";
		String foretRegionRT = "FAIL";
		String foretPhotoRT = "FAIL";
		String foretMemberRT = "FAIL";
	    
	    // 포레 등록 후 회원 아이디 가져오기
		int foret_id = insertForet(request);
		
		foretRT = getResult(foret_id);
	    if(foretRT.equals("OK")) {
	    	// 태그, 지역, 사진 등록
	    	foretTagRT = getResult(insertForetTag(foret_id, request));
	    	foretRegionRT = getResult(insertForetRegion(foret_id, request));
	    	foretPhotoRT = getResult(insertForetPhoto(foret_id, request, photo));
	    	foretMemberRT = getResult(insertForetMember(foret_id, request));
	    }
	    
	    JSONObject json = new JSONObject();
	    json.put("foretRT", foretRT);
	    json.put("foretTagRT", foretTagRT);
	    json.put("foretRegionRT", foretRegionRT);
	    json.put("foretPhotoRT", foretPhotoRT);
	    json.put("foretMemberRT", foretMemberRT);
	    json.put("foret_id", foret_id);
	    
		System.out.println("-- 함수 종료 : foret_insert.do --\n");
		return modelAndView(json);
	}
	@RequestMapping(value = "/foret/foret_modify.do")
	public ModelAndView foretModify(HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("-- 함수 실행 : foret_modify.do --");
		request.setCharacterEncoding("UTF-8");
	    
		String foretRT = "FAIL"; 
		String foretTagRT = "FAIL";
		String foretRegionRT = "FAIL";
		String foretPhotoRT = "FAIL";
		
		// 회원 수정
		foretRT = getResult(modifyForet(request));
		if(foretRT.equals("OK")) {
	    	// 태그, 지역, 사진 등록
			foretTagRT = getResult(modifyForetTag(request));
			foretRegionRT = getResult(modifyForetRegion(request));
			foretPhotoRT = getResult(modifyForetPhoto(request, photo));
	    }
	    
	    JSONObject json = new JSONObject();
	    json.put("foretRT", foretRT);
	    json.put("foretTagRT", foretTagRT);
	    json.put("foretRegionRT", foretRegionRT);
	    json.put("foretPhotoRT", foretPhotoRT);
	    
		System.out.println("-- 함수 종료 : foret_modify.do --\n");
		return modelAndView(json);
	}
	@RequestMapping(value = "/foret/foret_delete.do")
	public ModelAndView foretDelete(HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("-- 함수 실행 : foret_delete.do --");
		request.setCharacterEncoding("UTF-8");
		String foretRT = "FAIL";
		foretRT = getResult(deleteForet(request));
		JSONObject json = new JSONObject();
	    json.put("foretRT", foretRT);
		System.out.println("-- 함수 종료 : foret_delete.do --\n");
		return modelAndView(json);
	}
	@RequestMapping(value = "/foret/foret_member_insert.do")
	public ModelAndView foretMemberWrite(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : foret_member_insert.do --");
		request.setCharacterEncoding("UTF-8");
		String foretMemberRT = "FAIL";
		foretMemberRT = getResult(insertForetMember(request));
		JSONObject json = new JSONObject();
	    json.put("foretMemberRT", foretMemberRT);
		System.out.println("-- 함수 종료 : foret_member_insert.do --\n");
		return modelAndView(json);
	}
	@RequestMapping(value = "/foret/foret_member_delete.do")
	public ModelAndView foretMemberDelete(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : foret_member_delete.do --");
		request.setCharacterEncoding("UTF-8");
		String foretMemberRT = "FAIL";
		foretMemberRT = getResult(deleteForetMember(request));
		JSONObject json = new JSONObject();
	    json.put("foretMemberRT", foretMemberRT);
		System.out.println("-- 함수 종료 : foret_member_delete.do --\n");
		return modelAndView(json);
	}
	
	
	private int insertForet(HttpServletRequest request) {
		System.out.println("함수 실행 : insertForet");
		int foret_id = 0;
		// 기본 정보
		int leader_id = haveId(request.getParameter("leader_id"));
		String name = request.getParameter("name");
		String introduce = request.getParameter("introduce");
		int max_member = haveId(request.getParameter("max_member"));
	    // 포레 작성
		ForetDTO foretDTO = new ForetDTO();
		foretDTO.setLeader_id(leader_id);
		foretDTO.setName(name);
		foretDTO.setIntroduce(introduce);
		foretDTO.setMax_member(max_member);
	    
		foretService.foretWrite(foretDTO);
		foret_id = foretDTO.getId();
		System.out.println("함수 종료 : insertForet");
		return foret_id;
	}
	private int insertForetTag(int foret_id, HttpServletRequest request) {
		System.out.println("함수 실행 : insertForetTag");
		String tag[] = request.getParameterValues("tag");
		int result = 0;
		if(tag != null) {
			result = foretTagWrite(foret_id, getTagId(tag));
	    }
		System.out.println("함수 종료 : insertForetTag");
		return result;
	}
	private int insertForetRegion(int foret_id, HttpServletRequest request) {
		System.out.println("함수 실행 : insertForetRegion");
		String region_si[] = request.getParameterValues("region_si");
	    String region_gu[] = request.getParameterValues("region_gu");
		int result = 0;
	    if(region_si != null && region_gu != null) {
	    	result = foretRegionWrite(foret_id, getRegionId(region_si, region_gu));
	    }
		System.out.println("함수 종료 : insertForetRegion");
		return result;
	}
	private int insertForetPhoto(int foret_id, HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("함수 실행 : insertForetPhoto");
		int result = 0;
		if(photo != null) {
			result = foretPhotoWrite(foret_id, request, photo);
		}
		System.out.println("함수 종료 : insertForetPhoto");
		return result;
	}
	private int insertForetMember(HttpServletRequest request) {
		System.out.println("함수 실행 : insertForetMember");
		int foret_id = haveId(request.getParameter("foret_id"));
		int member_id = haveId(request.getParameter("member_id"));
		int result = 0;
		result = foretMemberWrite(foret_id, member_id);
		System.out.println("함수 종료 : insertForetMember");
		return result;
	}
	private int insertForetMember(int foret_id, HttpServletRequest request) {
		System.out.println("함수 실행 : insertForetMember");
		int member_id = haveId(request.getParameter("leader_id"));
		int result = 0;
		result = foretMemberWrite(foret_id, member_id);
		System.out.println("함수 종료 : insertForetMember");
		return result;
	}

	private int modifyForet(HttpServletRequest request) {
		System.out.println("함수 실행 : modifyForet");
		int result = 0;
		// 기본 정보
		int id = haveId(request.getParameter("id"));
		int leader_id = haveId(request.getParameter("leader_id"));
		String name = request.getParameter("name");
		String introduce = request.getParameter("introduce");
		int max_member = haveId(request.getParameter("max_member"));
	    // 멤버 작성
		ForetDTO foretDTO = new ForetDTO();
		foretDTO.setId(id);
		foretDTO.setLeader_id(leader_id);
		foretDTO.setName(name);
		foretDTO.setIntroduce(introduce);
		foretDTO.setMax_member(max_member);
	    
	    result = foretService.foretModify(foretDTO);
		System.out.println("함수 종료 : modifyForet");
		return result;
	}
	private int modifyForetTag(HttpServletRequest request) {
		System.out.println("함수 실행 : modifyForetTag");
		int foret_id = haveId(request.getParameter("id"));
		String tag[] = request.getParameterValues("tag");
		int result = 0;
		// 삭제 여부 알 수 없음
		foretTagDelete(foret_id);
		if(tag != null) {
			result = foretTagWrite(foret_id, getTagId(tag));
	    }
		System.out.println("함수 종료 : modifyForetTag");
		return result;
	}
	private int modifyForetRegion(HttpServletRequest request) {
		System.out.println("함수 실행 : modifyForetRegion");
		int foret_id = haveId(request.getParameter("id"));
		String region_si[] = request.getParameterValues("region_si");
	    String region_gu[] = request.getParameterValues("region_gu");
	    int result = 0;
		// 삭제 여부 알 수 없음
	    foretRegionDelete(foret_id);
	    if(region_si != null && region_gu != null) {
	    	result = foretRegionWrite(foret_id, getRegionId(region_si, region_gu));
	    }
		System.out.println("함수 종료 : modifyForetRegion");
		return result;
	}
	private int modifyForetPhoto(HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("함수 실행 : modifyForetPhoto");
		int foret_id = haveId(request.getParameter("id"));
		int result = 0;
		if(photo != null) {
			// 사진 수정
			foretPhotoDelete(foret_id);
			result = foretPhotoWrite(foret_id, request, photo);
		} else {
			// 사진 내리기
			result = foretPhotoDelete(foret_id);
		}
		System.out.println("함수 종료 : modifyForetPhoto");
		return result;
	}
	
	private int deleteForet(HttpServletRequest request) {
		System.out.println("함수 실행 : deleteForet");
		int foret_id = haveId(request.getParameter("id"));
		int result = 0;
		ForetDTO foretDTO = new ForetDTO();
		foretDTO.setId(foret_id);
		result = foretService.foretDelete(foretDTO);
		System.out.println("함수 종료 : deleteForet");
		return result;
	}
	private int deleteForetMember(HttpServletRequest request) {
		System.out.println("함수 실행 : deleteForetMember");
		int foret_id = haveId(request.getParameter("foret_id"));
		int member_id = haveId(request.getParameter("member_id"));
		int result = 0;
		result = foretMemberDelete(foret_id, member_id);
		System.out.println("함수 종료 : deleteForetMember");
		return result;
	}
	
	public List<TagDTO> getTagId(String[] tag){
		System.out.println("함수 실행 : getTagId");
    	List<TagDTO> list = new ArrayList<TagDTO>();
	    for(String tag_name : tag) {
	    	TagDTO tagDTO = new TagDTO();
	    	tagDTO.setTag_name(tag_name);
	    	list.add(tagDTO);
	    }
	    System.out.println("함수 종료 : getTagId");
	    return tagService.getTagId(list);
	}
	public List<RegionDTO> getRegionId(String[] region_si, String[] region_gu){
		System.out.println("함수 실행 : getRegionId");
		List<RegionDTO> list = new ArrayList<RegionDTO>();
		for(int i = 0; i< region_si.length; i++) {
			RegionDTO regionDTO = new RegionDTO();
			regionDTO.setRegion_si(region_si[i]);
			regionDTO.setRegion_gu(region_gu[i]);
			list.add(regionDTO);
		}
		System.out.println("함수 종료 : getRegionId");
		return regionService.getRegionId(list);
	}
	
	public int foretTagWrite(int foret_id, List<TagDTO> tagList) {
		System.out.println("함수 실행 : foretTagWrite");
		List<ForetTagDTO> list = new ArrayList<ForetTagDTO>();
	    for(TagDTO tagDTO : tagList) {
	    	ForetTagDTO foretTagDTO = new ForetTagDTO();
	    	foretTagDTO.setId(foret_id);
	    	foretTagDTO.setTag_id(tagDTO.getTag_id());
	    	list.add(foretTagDTO);
	    }
	    System.out.println("함수 종료 : foretTagWrite");
	    return tagService.foretTagWrite(list);
	}
	public int foretTagDelete(int foret_id) {
		System.out.println("함수 실행 : foretTagDelete");
		System.out.println("함수 종료 : foretTagDelete");
		return tagService.foretTagDelete(foret_id);
	}
	public int foretRegionWrite(int foret_id, List<RegionDTO> regionList) {
		System.out.println("함수 실행 : foretRegionWrite");
		List<ForetRegionDTO> list = new ArrayList<ForetRegionDTO>();
		for(RegionDTO regionDTO : regionList) {
			ForetRegionDTO foretRegionDTO = new ForetRegionDTO();
			foretRegionDTO.setId(foret_id);
			foretRegionDTO.setRegion_id(regionDTO.getRegion_id());
			list.add(foretRegionDTO);
		}
		System.out.println("함수 종료 : foretRegionWrite");
		return regionService.foretRegionWrite(list);
	}
	public int foretRegionDelete(int foret_id) {
		System.out.println("함수 실행 : foretRegionDelete");
		System.out.println("함수 종료 : foretRegionDelete");
		return regionService.foretRegionDelete(foret_id);
	}
	public int foretPhotoWrite(int foret_id, HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("함수 실행 : foretPhotoWrite");
		int result = 0;
		String dir = request.getSession().getServletContext().getRealPath("/storage");
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
        
        result = photoService.foretPhotoWrite(photoDTO);
        System.out.println("함수 종료 : foretPhotoWrite");
		return result;
	}
	public int foretPhotoDelete(int foret_id) {
		System.out.println("함수 실행 : foretPhotoDelete");
		int result = 0;
		PhotoDTO photoDTO = new PhotoDTO();
		photoDTO.setReference_id(foret_id);
		result = photoService.foretPhotoDelete(photoDTO);
		System.out.println("함수 종료 : foretPhotoDelete");
		return result;
	}
	public int foretMemberWrite(int foret_id, int member_id) {
		System.out.println("함수 실행 : foretMemberWrite");
		ForetMemberDTO foretMemberDTO = new ForetMemberDTO();
		foretMemberDTO.setForet_id(foret_id);
		foretMemberDTO.setMember_id(member_id);
	    System.out.println("함수 종료 : foretMemberWrite");
	    return foretService.foretMemberWrite(foretMemberDTO);
	}
	public int foretMemberDelete(int foret_id, int member_id) {
		System.out.println("함수 실행 : foretMemberDelete");
		ForetMemberDTO foretMemberDTO = new ForetMemberDTO();
		foretMemberDTO.setForet_id(foret_id);
		foretMemberDTO.setMember_id(member_id);
	    System.out.println("함수 종료 : foretMemberDelete");
	    return foretService.foretMemberDelete(foretMemberDTO);
	}
	
	public int haveId(String id) {
		System.out.println("함수 실행 : haveId");
		if(id == null) {
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
	    modelAndView.setViewName("foret.jsp");
	    return modelAndView;
	}
}
