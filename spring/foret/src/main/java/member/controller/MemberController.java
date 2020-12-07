package member.controller;

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

import member.bean.MemberDTO;
import member.bean.MemberLikeDTO;
import member.bean.MemberRegionDTO;
import member.bean.MemberTagDTO;
import photo.bean.PhotoDTO;
import photo.controller.PhotoService;
import region.bean.RegionDTO;
import region.controller.RegionService;
import tag.bean.TagDTO;
import tag.controller.TagService;

@Controller
public class MemberController {
	@Autowired
	MemberService memberService;
	@Autowired
	PhotoService photoService;
	@Autowired
	TagService tagService;
	@Autowired
	RegionService regionService;
	
	@RequestMapping(value = "/member/member_insert.do")
	public ModelAndView memberWrite(HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("-- 함수 실행 : member_insert.do --");
		request.setCharacterEncoding("UTF-8");
		
		String memberRT = "FAIL"; 
		String memberTagRT = "FAIL";
		String memberRegionRT = "FAIL";
		String memberPhotoRT = "FAIL";
	    
	    // 회원 등록 후 회원 아이디 가져오기
		int member_id = insertMember(request);
		
	    memberRT = getResult(member_id);
	    if(memberRT.equals("OK")) {
	    	// 태그, 지역, 사진 등록
	    	memberTagRT = getResult(insertMemberTag(member_id, request));
	    	memberRegionRT = getResult(insertMemberRegion(member_id, request));
	    	memberPhotoRT = getResult(insertMemberPhoto(member_id, request, photo));
	    }
	    
	    JSONObject json = new JSONObject();
	    json.put("memberRT", memberRT);
	    json.put("memberTagRT", memberTagRT);
	    json.put("memberRegionRT", memberRegionRT);
	    json.put("memberPhotoRT", memberPhotoRT);
	    json.put("id", member_id);
	      
	    System.out.println("-- 함수 종료 : member_insert.do --\n");
	    return modelAndView(json);
	}
	@RequestMapping(value = "/member/member_modify.do")
	public ModelAndView memberModify(HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("-- 함수 실행 : member_modify.do --");
		request.setCharacterEncoding("UTF-8");
	    
		String memberRT = "FAIL"; 
		String memberTagRT = "FAIL";
		String memberRegionRT = "FAIL";
		String memberPhotoRT = "FAIL";
		
		// 회원 수정
		memberRT = getResult(modifyMember(request));
		if(memberRT.equals("OK")) {
	    	// 태그, 지역, 사진 등록
	    	memberTagRT = getResult(modifyMemberTag(request));
	    	memberRegionRT = getResult(modifyMemberRegion(request));
	    	memberPhotoRT = getResult(modifyMemberPhoto(request, photo));
	    }
	    
	    JSONObject json = new JSONObject();
	    json.put("memberRT", memberRT);
	    json.put("memberTagRT", memberTagRT);
	    json.put("memberRegionRT", memberRegionRT);
	    json.put("memberPhotoRT", memberPhotoRT);
	      
	    System.out.println("-- 함수 종료 : member_modify.do --\n");
	    return modelAndView(json);
	}
	@RequestMapping(value = "/member/member_delete.do")
	public ModelAndView memberDelete(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : member_delete.do --");
		request.setCharacterEncoding("UTF-8");
		String memberRT = "FAIL";
		memberRT = getResult(deleteMember(request));
		JSONObject json = new JSONObject();
	    json.put("memberRT", memberRT);
	    System.out.println("-- 함수 종료 : member_delete.do --\n");
		return modelAndView(json);
	}
	@RequestMapping(value = "/member/member_board_like.do")
	public ModelAndView memberBoardLike(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : member_board_like.do --");
		request.setCharacterEncoding("UTF-8");
		String memberRT = "FAIL";
		memberRT = getResult(insertMemberBoardLike(request));
		JSONObject json = new JSONObject();
	    json.put("memberRT", memberRT);
	    System.out.println("-- 함수 종료 : member_board_like.do --\n");
		return modelAndView(json);
	}
	@RequestMapping(value = "/member/member_board_dislike.do")
	public ModelAndView memberBoardDisLike(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : member_board_dislike.do --");
		request.setCharacterEncoding("UTF-8");
		String memberRT = "FAIL";
		memberRT = getResult(deleteMemberBoardLike(request));
		JSONObject json = new JSONObject();
	    json.put("memberRT", memberRT);
	    System.out.println("-- 함수 종료 : member_board_dislike.do --\n");
		return modelAndView(json);
	}
	@RequestMapping(value = "/member/member_comment_like.do")
	public ModelAndView memberCommentLike(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : member_comment_like.do --");
		request.setCharacterEncoding("UTF-8");
		String memberRT = "FAIL";
		memberRT = getResult(insertMemberCommentLike(request));
		JSONObject json = new JSONObject();
	    json.put("memberRT", memberRT);
	    System.out.println("-- 함수 종료 : member_comment_like.do --\n");
		return modelAndView(json);
	}
	@RequestMapping(value = "/member/member_comment_dislike.do")
	public ModelAndView memberCommentDisLike(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : member_comment_dislike.do --");
		request.setCharacterEncoding("UTF-8");
		String memberRT = "FAIL";
		memberRT = getResult(deleteMemberCommentLike(request));
		JSONObject json = new JSONObject();
	    json.put("memberRT", memberRT);
	    System.out.println("-- 함수 종료 : member_comment_dislike.do --\n");
		return modelAndView(json);
	}
	
	public int insertMember(HttpServletRequest request) {
		System.out.println("함수 실행 : insertMember");
		int member_id = 0;
		// 기본 정보
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
	    String nickname = request.getParameter("nickname");
	    String birth = request.getParameter("birth");
	    String deviceToken = request.getParameter("deviceToken");
	    // 멤버 작성
	    MemberDTO memberDTO = new MemberDTO();
	    memberDTO.setName(name);
	    memberDTO.setEmail(email);
	    memberDTO.setPassword(password);
	    memberDTO.setNickname(nickname);
	    memberDTO.setBirth(birth);
	    memberDTO.setDeviceToken(deviceToken);
	    
	    memberService.memberWrite(memberDTO);
	    member_id = memberDTO.getId();
	    System.out.println("함수 종료 : insertMember");
		return member_id;
	}
	public int insertMemberTag(int member_id, HttpServletRequest request) {
		System.out.println("함수 실행 : insertMemberTag");
		String tag[] = request.getParameterValues("tag");
		int result = 0;
		if(tag != null) {
			result = memberTagWrite(member_id, getTagId(tag));
	    }
		System.out.println("함수 종료 : insertMemberTag");
		return result;
	}
	public int insertMemberRegion(int member_id, HttpServletRequest request) {
		System.out.println("함수 실행 : insertMemberRegion");
		String region_si[] = request.getParameterValues("region_si");
	    String region_gu[] = request.getParameterValues("region_gu");
		int result = 0;
	    if(region_si != null && region_gu != null) {
	    	result = memberRegionWrite(member_id, getRegionId(region_si, region_gu));
	    }
	    System.out.println("함수 종료 : insertMemberRegion");
	    return result;
	}
	public int insertMemberPhoto(int member_id, HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("함수 실행 : insertMemberPhoto");
		int result = 0;
		if(photo != null) {
			result = memberPhotoWrite(member_id, request, photo);
		}
		System.out.println("함수 종료 : insertMemberPhoto");
		return result;
	}
	public int insertMemberBoardLike(HttpServletRequest request) {
		System.out.println("함수 실행 : insertMemberBoardLike");
		int result = 0;
		MemberLikeDTO memberLikeDTO = new MemberLikeDTO();
		memberLikeDTO.setId(haveId(request.getParameter("id")));
		memberLikeDTO.setReference_id(haveId(request.getParameter("board_id")));
		result = memberService.memberBoardLike(memberLikeDTO);
	    System.out.println("함수 종료 : insertMemberBoardLike");
	    return result;
	}
	public int insertMemberCommentLike(HttpServletRequest request) {
		System.out.println("함수 실행 : insertMemberCommentLike");
		int result = 0;
		MemberLikeDTO memberLikeDTO = new MemberLikeDTO();
		memberLikeDTO.setId(haveId(request.getParameter("id")));
		memberLikeDTO.setReference_id(haveId(request.getParameter("comment_id")));
		result = memberService.memberCommentLike(memberLikeDTO);
	    System.out.println("함수 종료 : insertMemberCommentLike");
	    return result;
	}
	
	public int modifyMember(HttpServletRequest request) {
		System.out.println("함수 실행 : modifyMember");
		int result = 0;
		// 기본 정보
		int id = haveId(request.getParameter("id"));
		String name = request.getParameter("name");
		String password = request.getParameter("password");
	    String nickname = request.getParameter("nickname");
	    String birth = request.getParameter("birth");
	    // 멤버 작성
	    MemberDTO memberDTO = new MemberDTO();
	    memberDTO.setId(id);
	    memberDTO.setName(name);
	    memberDTO.setPassword(password);
	    memberDTO.setNickname(nickname);
	    memberDTO.setBirth(birth);
	    
	    result = memberService.memberModify(memberDTO);
	    System.out.println("함수 종료 : modifyMember");
		return result;
	}
	public int modifyMemberTag(HttpServletRequest request) {
		System.out.println("함수 실행 : modifyMemberTag");
		int member_id = haveId(request.getParameter("id"));
		String tag[] = request.getParameterValues("tag");
		int result = 0;
		// 삭제 여부 알 수 없음
		memberTagDelete(member_id);
		if(tag != null) {
			result = memberTagWrite(member_id, getTagId(tag));
	    }
		System.out.println("함수 종료 : modifyMemberTag");
		return result;
	}
	public int modifyMemberRegion(HttpServletRequest request) {
		System.out.println("함수 실행 : modifyMemberRegion");
		int member_id = haveId(request.getParameter("id"));
		String region_si[] = request.getParameterValues("region_si");
	    String region_gu[] = request.getParameterValues("region_gu");
	    int result = 0;
		// 삭제 여부 알 수 없음
	    memberRegionDelete(member_id);
	    if(region_si != null && region_gu != null) {
	    	result = memberRegionWrite(member_id, getRegionId(region_si, region_gu));
	    }
		System.out.println("함수 종료 : modifyMemberRegion");
		return result;
	}
	public int modifyMemberPhoto(HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("함수 실행 : modifyMemberPhoto");
		int member_id = haveId(request.getParameter("id"));
		int result = 0;
		if(photo != null) {
			// 사진 수정
			memberPhotoDelete(member_id);
			result = memberPhotoWrite(member_id, request, photo);
		} else {
			// 사진 내리기
			result = memberPhotoDelete(member_id);
		}
		System.out.println("함수 종료 : modifyMemberPhoto");
		return result;
	}

	public int deleteMember(HttpServletRequest request) {
		System.out.println("함수 실행 : deleteMember");
		int member_id = haveId(request.getParameter("id"));
		int result = 0;
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setId(member_id);
		result = memberService.memberDelete(memberDTO);
		System.out.println("함수 종료 : deleteMember");
		return result;
	}
	public int deleteMemberBoardLike(HttpServletRequest request) {
		System.out.println("함수 실행 : deleteMemberBoardLike");
		int result = 0;
		MemberLikeDTO memberLikeDTO = new MemberLikeDTO();
		memberLikeDTO.setId(haveId(request.getParameter("id")));
		memberLikeDTO.setReference_id(haveId(request.getParameter("board_id")));
		result = memberService.memberBoardDisLike(memberLikeDTO);
	    System.out.println("함수 종료 : deleteMemberBoardLike");
	    return result;
	}
	public int deleteMemberCommentLike(HttpServletRequest request) {
		System.out.println("함수 실행 : deleteMemberCommentLike");
		int result = 0;
		MemberLikeDTO memberLikeDTO = new MemberLikeDTO();
		memberLikeDTO.setId(haveId(request.getParameter("id")));
		memberLikeDTO.setReference_id(haveId(request.getParameter("comment_id")));
		result = memberService.memberCommentDisLike(memberLikeDTO);
	    System.out.println("함수 종료 : deleteMemberCommentLike");
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

	public int memberTagWrite(int member_id, List<TagDTO> tagList) {
		System.out.println("함수 실행 : memberTagWrite");
		List<MemberTagDTO> list = new ArrayList<MemberTagDTO>();
	    for(TagDTO tagDTO : tagList) {
	    	MemberTagDTO memberTagDTO = new MemberTagDTO();
	    	memberTagDTO.setId(member_id);
	    	memberTagDTO.setTag_id(tagDTO.getTag_id());
	    	list.add(memberTagDTO);
	    }
	    System.out.println("함수 종료 : memberTagWrite");
	    return tagService.memberTagWrite(list);
	}
	public int memberTagDelete(int member_id) {
		System.out.println("함수 실행 : memberTagDelete");
		System.out.println("함수 종료 : memberTagDelete");
		return tagService.memberTagDelete(member_id);
	}
	public int memberRegionWrite(int member_id, List<RegionDTO> regionList) {
		System.out.println("함수 실행 : memberRegionWrite");
		List<MemberRegionDTO> list = new ArrayList<MemberRegionDTO>();
		for(RegionDTO regionDTO : regionList) {
			MemberRegionDTO memberRegionDTO = new MemberRegionDTO();
			memberRegionDTO.setId(member_id);
			memberRegionDTO.setRegion_id(regionDTO.getRegion_id());
			list.add(memberRegionDTO);
		}
		System.out.println("함수 종료 : memberRegionWrite");
		return regionService.memberRegionWrite(list);
	}
	public int memberRegionDelete(int member_id) {
		System.out.println("함수 실행 : memberRegionDelete");
		System.out.println("함수 종료 : memberRegionDelete");
		return regionService.memberRegionDelete(member_id);
	}
	public int memberPhotoWrite(int member_id, HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("함수 실행 : memberPhotoWrite");
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
        photoDTO.setReference_id(member_id);
        
        result = photoService.memberPhotoWrite(photoDTO);
        System.out.println("함수 종료 : memberPhotoWrite");
		return result;
	}
	public int memberPhotoDelete(int member_id) {
		System.out.println("함수 실행 : memberPhotoDelete");
		int result = 0;
		PhotoDTO photoDTO = new PhotoDTO();
		photoDTO.setReference_id(member_id);
		result = photoService.memberPhotoDelete(photoDTO);
		System.out.println("함수 종료 : memberPhotoDelete");
		return result;
	}
	
	public void log(MemberDTO memberDTO) {
		System.out.println();
	    System.out.println("------------------");
	    System.out.println("id : " + memberDTO.getId());
	    System.out.println("name : " + memberDTO.getName());
	    System.out.println("nickname : " + memberDTO.getNickname());
	    System.out.println("birth : " + memberDTO.getBirth());
	    System.out.println("email : " + memberDTO.getEmail());
	    if(memberDTO.getTag() != null) {
	    	for(String s : memberDTO.getTag()) {
		    	System.out.print("tag : ");
		    	System.out.println(s);
		    }
	    }
	    if(memberDTO.getRegion_si() != null) {
	    	for(String s : memberDTO.getRegion_si()) {
		    	System.out.print("region_si : ");
		    	System.out.println(s);
		    };
	    }
	    if(memberDTO.getRegion_gu() != null) {
	    	for(String s : memberDTO.getRegion_gu()) {
		    	System.out.print("region_gu : ");
		    	System.out.println(s);
		    }
	    }
	    System.out.println("------------------");
	    System.out.println();
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
	    modelAndView.setViewName("member.jsp");
	    return modelAndView;
	}
}
