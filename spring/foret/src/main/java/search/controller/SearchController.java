package search.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import board.bean.BoardDTO;
import comment.bean.CommentDTO;
import foret.bean.ForetDTO;
import foret.bean.ForetRegionDTO;
import foret.bean.ForetTagDTO;
import member.bean.MemberDTO;
import search.bean.BoardALLDTO;
import search.bean.ForetALLDTO;
import search.bean.KeywordDTO;
import search.bean.MemberALLDTO;

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
	@RequestMapping(value = "/search/member.do")
	public ModelAndView memberSelect(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : member.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		
		int id = haveId(request.getParameter("id"));
		List<MemberALLDTO> list = searchService.memberSelect(id);
		List<String> stringlist1 = new ArrayList<String>();
		List<String> stringlist2 = new ArrayList<String>();
		List<Integer> intlist1 = new ArrayList<Integer>();
		List<Integer> intlist2 = new ArrayList<Integer>();
		List<Integer> intlist3 = new ArrayList<Integer>();
		
		JSONObject json = new JSONObject();
		if(list != null) {
			RT = "OK";
			JSONArray array = new JSONArray();
			JSONObject temp = new JSONObject();
			temp.put("id", list.get(0).getId());
			temp.put("name", list.get(0).getName());
			temp.put("email", list.get(0).getEmail());
			temp.put("password", list.get(0).getPassword());
			temp.put("nickname", list.get(0).getNickname());
			temp.put("birth", list.get(0).getBirth());
			temp.put("reg_date", list.get(0).getReg_date());
			temp.put("deviceToken", list.get(0).getDeviceToken());
			temp.put("photo", list.get(0).getPhoto());
			JSONArray tagArray = new JSONArray();
			JSONArray region_siArray = new JSONArray();
			JSONArray region_guArray = new JSONArray();
			JSONArray like_boardArray = new JSONArray();
			JSONArray like_commentArray = new JSONArray();
			JSONArray foret_idArray = new JSONArray();
			
			for(MemberALLDTO memberALLDTO : list) {
				String tag_name = memberALLDTO.getTag_name();
				String region_si = memberALLDTO.getRegion_si();
				String region_gu = memberALLDTO.getRegion_gu();
				String region = region_si + region_gu;
				int like_board = memberALLDTO.getLike_board();
				int like_comment = memberALLDTO.getLike_comment();
				int foret_id = memberALLDTO.getForet_id();

				if(!stringlist1.contains(tag_name)) {
					stringlist1.add(tag_name);
					tagArray.put(tag_name);
				}
				if(!stringlist2.contains(region)) {
					stringlist2.add(region);
					region_siArray.put(region_si);
					region_guArray.put(region_gu);
				}
				if(!intlist1.contains(like_board)) {
					intlist1.add(like_board);
					like_boardArray.put(like_board);
				}
				if(!intlist2.contains(like_comment)) {
					intlist2.add(like_comment);
					like_commentArray.put(like_comment);
				}
				if(!intlist3.contains(foret_id)) {
					intlist3.add(foret_id);
					foret_idArray.put(foret_id);
				}
			}
			
			temp.put("tag", tagArray);
			temp.put("region_si", region_siArray);
			temp.put("region_gu", region_guArray);
			temp.put("like_board", like_boardArray);
			temp.put("like_comment", like_commentArray);
			temp.put("foret_id", foret_idArray);
			array.put(temp);
			json.put("member", array);
		}
		json.put("RT", RT);
		
		System.out.println("-- 함수 종료 : member.do --\n");
		return modelAndView(json);
	}
	@RequestMapping(value = "/search/member_login.do")
	public ModelAndView memberLogin(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : member_login.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		int id = searchService.memberLogin(email, password);
		
		JSONObject json = new JSONObject();
		if(id > 0) {
			RT = "OK";
			json.put("id", id);
		}
		json.put("RT", RT);
		
		System.out.println("-- 함수 종료 : member_login.do --\n");
		return modelAndView(json);
	}
	
	@RequestMapping(value = "/search/home.do")
	public ModelAndView homeSelect(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : home.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		
		int tempBoardId = 0;
		int tempForetId = 0;
		
		int board_id = haveId(request.getParameter("id"));
		int foret_id;
		List<BoardALLDTO> list = searchService.homeSelect(board_id);
		
		JSONObject json = new JSONObject();
		JSONArray foretArray = new JSONArray();
		JSONObject foretTemp = new JSONObject();
		JSONArray boardArray = new JSONArray();
		JSONObject boardTemp = new JSONObject();
		// JSONArray photoArray = new JSONArray();
		
		if(list != null) {
			RT = "OK";
			
			for(BoardALLDTO boardALLDTO : list) {
				foret_id = boardALLDTO.getForet_id();
				board_id = boardALLDTO.getId();
				
				if(board_id != 0) {
					if(tempBoardId != board_id) {
						if(tempBoardId != 0) {
							boardArray.put(boardTemp);
							boardTemp = new JSONObject();
						}
						tempBoardId = board_id;
						boardTemp.put("id", board_id);
						boardTemp.put("writer", boardALLDTO.getWriter());
						boardTemp.put("type", boardALLDTO.getType());
						boardTemp.put("hit", boardALLDTO.getHit());
						boardTemp.put("subject", boardALLDTO.getSubject());
						boardTemp.put("content", boardALLDTO.getContent());
						boardTemp.put("reg_date", boardALLDTO.getReg_date());
						boardTemp.put("edit_date", boardALLDTO.getEdit_date());
						boardTemp.put("board_like", boardALLDTO.getBoard_like());
						boardTemp.put("board_comment", boardALLDTO.getBoard_comment());
						boardTemp.put("photo", boardALLDTO.getBoard_photo());
					}
				}
				if(tempForetId != foret_id) {
					if(tempForetId != 0) {
						foretTemp.put("board", boardArray);
						foretArray.put(foretTemp);
						foretTemp = new JSONObject();
						boardArray = new JSONArray();
					}
					tempForetId = foret_id;
					foretTemp.put("id", foret_id);
					foretTemp.put("name", boardALLDTO.getForet_name());
					foretTemp.put("photo", boardALLDTO.getForet_photo());
				}	
				
			}
			boardArray.put(boardTemp);
			foretTemp.put("board", boardArray);
			foretArray.put(foretTemp);
			json.put("foretTotal", foretArray.length());
			json.put("foret", foretArray);
		}
		
		
		json.put("RT", RT);
		
		System.out.println("-- 함수 실행 : home.do --");
		return modelAndView(json);
	}
	
	@RequestMapping(value = "/search/boardList.do")
	public ModelAndView boardListPage(HttpServletRequest request) throws Exception {
		// inquiry type
		// 1 : 최신
		// 2 : 조회수
		// 3 : 좋아요
		// 4 : 댓글
		System.out.println("-- 함수 실행 : boardList.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		int temp = 0;
		int id = 0;
		int pg = haveId(request.getParameter("pg"));
		int size = haveId(request.getParameter("size"));
		
		int foret_id = haveId(request.getParameter("foret_id"));
		int type = haveId(request.getParameter("type"));
		int inquiry_type = haveId(request.getParameter("inquiry_type"));
		int endNum = pg * size;
		int startNum = endNum - (size - 1);
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("foret_id", foret_id);
		map.put("type", type);
		map.put("inquiry_type", inquiry_type);
		map.put("endNum", endNum);
		map.put("startNum", startNum);
		List<BoardALLDTO> list;
		if(pg == 0 && size == 0) {
			System.out.println("전체 조회");
			list = searchService.boardList(map);
		} else {
			System.out.println("페이지 조회");
			list = searchService.boardListPage(map);
		}
		
		JSONObject json = new JSONObject();
		if(list != null) {
			RT = "OK";
			JSONArray array = new JSONArray();
			JSONObject tempjson = new JSONObject();
			JSONArray photoArray = new JSONArray();
			
			for(BoardALLDTO boardALLDTO : list) {
				id = boardALLDTO.getId();
				if(temp != id) {
					if(temp != 0) {
						tempjson.put("photo", photoArray);
						array.put(tempjson);
					}
					tempjson = new JSONObject();
					photoArray = new JSONArray();
					temp = id;
					tempjson.put("id", id);
					tempjson.put("writer", boardALLDTO.getWriter());
					tempjson.put("foret_id", boardALLDTO.getForet_id());
					tempjson.put("type", boardALLDTO.getType());
					tempjson.put("hit", boardALLDTO.getHit());
					tempjson.put("subject", boardALLDTO.getSubject());
					tempjson.put("content", boardALLDTO.getContent());
					tempjson.put("reg_date", boardALLDTO.getReg_date());
					tempjson.put("edit_date", boardALLDTO.getEdit_date());
					tempjson.put("board_like", boardALLDTO.getBoard_like());
					tempjson.put("board_comment", boardALLDTO.getBoard_comment());
					photoArray.put(boardALLDTO.getPhoto());
					System.out.println(boardALLDTO.getPhoto());
				} else {
					photoArray.put(boardALLDTO.getPhoto());
				}
			}
			tempjson.put("photo", photoArray);
			array.put(tempjson);
			json.put("board", array);
		}
		json.put("RT", RT);
		
		System.out.println("-- 함수 종료 : boardList.do --");
		return modelAndView(json);
	}
	@RequestMapping(value = "/search/boardSelect.do")
	public ModelAndView boardSelect(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : boardSelect.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		JSONObject json = new JSONObject();
		int id = haveId(request.getParameter("id"));
		List<BoardALLDTO> list = searchService.boardSelect(id);
		List<String> stringlist1 = new ArrayList<String>();
		List<Integer> intlist1 = new ArrayList<Integer>();
		
		if(list != null) {
			RT = "OK";
			JSONArray boardArray = new JSONArray();
			JSONArray photoArray = new JSONArray();
			JSONArray commentArray = new JSONArray();
			JSONObject boardTemp = new JSONObject();
			
			
			boardTemp.put("id", list.get(0).getId());
			boardTemp.put("writer", list.get(0).getWriter());
			boardTemp.put("writer_nickname", list.get(0).getWriter_nickname());
			boardTemp.put("writer_photo", list.get(0).getWriter_photo());
			boardTemp.put("foret_id", list.get(0).getForet_id());
			boardTemp.put("type", list.get(0).getType());
			boardTemp.put("hit", list.get(0).getHit());
			boardTemp.put("subject", list.get(0).getSubject());
			boardTemp.put("content", list.get(0).getContent());
			boardTemp.put("reg_date", list.get(0).getReg_date());
			boardTemp.put("edit_date", list.get(0).getEdit_date());
			boardTemp.put("board_like", list.get(0).getBoard_like());
			boardTemp.put("board_comment", list.get(0).getBoard_comment());
			for(BoardALLDTO boardALLDTO : list) {
				String photo = boardALLDTO.getPhoto();
				int comment_id = boardALLDTO.getComment_id();
				if(!stringlist1.contains(photo)) {
					stringlist1.add(photo);
					photoArray.put(photo);
				}
				if(!intlist1.contains(comment_id)) {
					intlist1.add(comment_id);
					JSONObject commentTemp = new JSONObject();
					commentTemp.put("id", boardALLDTO.getComment_id());
					commentTemp.put("writer", boardALLDTO.getComment_writer());
					commentTemp.put("writer_nickname", boardALLDTO.getComment_writer_nickname());
					commentTemp.put("writer_photo", boardALLDTO.getComment_writer_photo());
					commentTemp.put("content", boardALLDTO.getComment_content());
					commentTemp.put("reg_date", boardALLDTO.getComment_reg_date());
					commentTemp.put("group_no", boardALLDTO.getComment_group_no());
					commentArray.put(commentTemp);
				}
			}
			boardTemp.put("photo", photoArray);
			boardTemp.put("comment", commentArray);
			boardArray.put(boardTemp);
			json.put("board", boardArray);
		}
		json.put("RT", RT);
		System.out.println("-- 함수 종료 : boardSelect.do --");
		return modelAndView(json);
	}
	
	
	@RequestMapping(value = "/search/foretSelect.do")
	public ModelAndView foretSelect(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : foretSelect.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		
		int foret_id = haveId(request.getParameter("foret_id"));
		int member_id = haveId(request.getParameter("member_id"));
		List<ForetALLDTO> list = searchService.foretSelect(foret_id);
		List<String> stringlist1 = new ArrayList<String>();
		List<String> stringlist2 = new ArrayList<String>();
		List<Integer> intlist1 = new ArrayList<Integer>();
		
		JSONObject json = new JSONObject();
		if(list != null) {
			RT = "OK";
			JSONArray array = new JSONArray();
			JSONObject temp = new JSONObject();
			temp.put("id", list.get(0).getId());
			temp.put("leader_id", list.get(0).getLeader_id());
			temp.put("name", list.get(0).getName());
			temp.put("introduce", list.get(0).getIntroduce());
			temp.put("max_member", list.get(0).getMax_member());
			temp.put("reg_date", list.get(0).getReg_date());
			temp.put("photo", list.get(0).getPhoto());
			JSONArray tagArray = new JSONArray();
			JSONArray region_siArray = new JSONArray();
			JSONArray region_guArray = new JSONArray();
			JSONArray memberArray = new JSONArray();
			
			for(ForetALLDTO foretALLDTO : list) {
				String tag_name = foretALLDTO.getTag_name();
				String region_si = foretALLDTO.getSi();
				String region_gu = foretALLDTO.getGu();
				String region = region_si + region_gu;
				int member = foretALLDTO.getMember_id();

				if(!stringlist1.contains(tag_name)) {
					stringlist1.add(tag_name);
					tagArray.put(tag_name);
				}
				if(!stringlist2.contains(region)) {
					stringlist2.add(region);
					region_siArray.put(region_si);
					region_guArray.put(region_gu);
				}
				if(!intlist1.contains(member)) {
					intlist1.add(member);
					memberArray.put(member);
				}
			}
			temp.put("tag", tagArray);
			temp.put("region_si", region_siArray);
			temp.put("region_gu", region_guArray);
			temp.put("member", memberArray);
			array.put(temp);
			json.put("foret", array);
		}
		if(intlist1.contains(member_id)) {
			if(list.get(0).getLeader_id() == member_id) {
				json.put("rank", "leader");
			} else {
				json.put("rank", "member");
			}
		} else {
			json.put("rank", "guest");
		}
		json.put("RT", RT);
		System.out.println("-- 함수 종료 : foretSelect.do --");
		return modelAndView(json);
	}	
	@RequestMapping(value = "/search/foret_rank.do")
	public ModelAndView foretRank(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : foret_rank.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		JSONObject json = new JSONObject();
		
		int tempId = 0;
		int rank = haveId(request.getParameter("rank"));
		List<ForetALLDTO> list = searchService.foretRank(rank);
		List<String> stringlist1 = new ArrayList<String>();
		List<String> stringlist2 = new ArrayList<String>();
		List<Integer> intlist1 = new ArrayList<Integer>();
		
		if(list != null) {
			RT = "OK";
			JSONArray array = new JSONArray();
			JSONObject temp = null;
			JSONArray tagArray = null;
			JSONArray region_siArray = null;
			JSONArray region_guArray = null;
			JSONArray memberArray = null;
			
			for(ForetALLDTO foretALLDTO : list) {
				// 전 아이디와 다를 경우
				if(tempId != foretALLDTO.getId()) {
					if(tempId != 0) {
						temp.put("tag", tagArray);
						temp.put("region_si", region_siArray);
						temp.put("region_gu", region_guArray);
						temp.put("member", memberArray);
						array.put(temp);
						stringlist1 = new ArrayList<String>();
						stringlist2 = new ArrayList<String>();
						intlist1 = new ArrayList<Integer>();
					}
					tempId = foretALLDTO.getId();
					temp = new JSONObject();
					temp.put("id", foretALLDTO.getId());
					temp.put("leader_id", foretALLDTO.getLeader_id());
					temp.put("name", foretALLDTO.getName());
					temp.put("introduce", foretALLDTO.getIntroduce());
					temp.put("max_member", foretALLDTO.getMax_member());
					temp.put("reg_date", foretALLDTO.getReg_date());
					temp.put("photo", foretALLDTO.getPhoto());
					temp.put("cnt", foretALLDTO.getCnt());
					tagArray = new JSONArray();
					region_siArray = new JSONArray();
					region_guArray = new JSONArray();
					memberArray = new JSONArray();
				}
				String tag_name = foretALLDTO.getTag_name();
				String region_si = foretALLDTO.getSi();
				String region_gu = foretALLDTO.getGu();
				String region = region_si + region_gu;
				int member = foretALLDTO.getMember_id();

				if(!stringlist1.contains(tag_name)) {
					stringlist1.add(tag_name);
					tagArray.put(tag_name);
				}
				if(!stringlist2.contains(region)) {
					stringlist2.add(region);
					region_siArray.put(region_si);
					region_guArray.put(region_gu);
				}
				if(!intlist1.contains(member)) {
					intlist1.add(member);
					memberArray.put(member);
				}
			}
			temp.put("tag", tagArray);
			temp.put("region_si", region_siArray);
			temp.put("region_gu", region_guArray);
			temp.put("member", memberArray);
			array.put(temp);
			json.put("foret", array);
		}
		
		json.put("RT", RT);
		System.out.println("-- 함수 종료 : foret_rank.do --");
		return modelAndView(json);
	}
	
	@RequestMapping(value = "/search/search_keyword.do")
	public ModelAndView searchKeyword(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : search_keyword.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		
		List<KeywordDTO> list = searchService.searchKeyword();
		JSONObject json = new JSONObject();
		if(list != null) {
			RT = "OK";
			JSONArray keyword = new JSONArray();
			for(KeywordDTO keywordDTO : list) {
				JSONObject temp = new JSONObject();
				temp.put("type", keywordDTO.getType());
				temp.put("name", keywordDTO.getName());
				keyword.put(temp);
			}
			json.put("keyword", keyword);
		}
		json.put("RT", RT);
		System.out.println("-- 함수 종료 : search_keyword.do --");
		return modelAndView(json);
	}
	@RequestMapping(value = "/search/foret_keyword_search.do")
	public ModelAndView foretkeywordSearch(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : foret_keyword_search.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		JSONObject json = new JSONObject();
		
		int tempId = 0;
		String type = request.getParameter("type");
		String name = request.getParameter("name");
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type);
		map.put("name", name);
		List<ForetALLDTO> list = searchService.foretkeywordSearch(map);
		List<String> stringlist1 = new ArrayList<String>();
		List<String> stringlist2 = new ArrayList<String>();
		List<Integer> intlist1 = new ArrayList<Integer>();
		
		if(list != null) {
			RT = "OK";
			JSONArray array = new JSONArray();
			JSONObject temp = null;
			JSONArray tagArray = null;
			JSONArray region_siArray = null;
			JSONArray region_guArray = null;
			JSONArray memberArray = null;
			
			for(ForetALLDTO foretALLDTO : list) {
				// 전 아이디와 다를 경우
				if(tempId != foretALLDTO.getId()) {
					if(tempId != 0) {
						temp.put("tag", tagArray);
						temp.put("region_si", region_siArray);
						temp.put("region_gu", region_guArray);
						temp.put("member", memberArray);
						array.put(temp);
						stringlist1 = new ArrayList<String>();
						stringlist2 = new ArrayList<String>();
						intlist1 = new ArrayList<Integer>();
					}
					tempId = foretALLDTO.getId();
					temp = new JSONObject();
					temp.put("id", foretALLDTO.getId());
					temp.put("leader_id", foretALLDTO.getLeader_id());
					temp.put("name", foretALLDTO.getName());
					temp.put("introduce", foretALLDTO.getIntroduce());
					temp.put("max_member", foretALLDTO.getMax_member());
					temp.put("reg_date", foretALLDTO.getReg_date());
					temp.put("photo", foretALLDTO.getPhoto());
					temp.put("cnt", foretALLDTO.getCnt());
					tagArray = new JSONArray();
					region_siArray = new JSONArray();
					region_guArray = new JSONArray();
					memberArray = new JSONArray();
				}
				String tag_name = foretALLDTO.getTag_name();
				String region_si = foretALLDTO.getSi();
				String region_gu = foretALLDTO.getGu();
				String region = region_si + region_gu;
				int member = foretALLDTO.getMember_id();

				if(!stringlist1.contains(tag_name)) {
					stringlist1.add(tag_name);
					tagArray.put(tag_name);
				}
				if(!stringlist2.contains(region)) {
					stringlist2.add(region);
					region_siArray.put(region_si);
					region_guArray.put(region_gu);
				}
				if(!intlist1.contains(member)) {
					intlist1.add(member);
					memberArray.put(member);
				}
			}
			temp.put("tag", tagArray);
			temp.put("region_si", region_siArray);
			temp.put("region_gu", region_guArray);
			temp.put("member", memberArray);
			array.put(temp);
			json.put("foret", array);
		}
		
		json.put("RT", RT);
		System.out.println("-- 함수 종료 : foret_keyword_search.do --");
		return modelAndView(json);
	}
	
	
	
	
	// 미완료
	public ModelAndView autoComplete(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : autoComplete.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		JSONObject json = new JSONObject();
		
		json.put("RT", RT);
		System.out.println("-- 함수 종료 : autoComplete.do --");
		return modelAndView(json);
	}
	// 미완료
	public ModelAndView searchSelect(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : searchSelect.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		JSONObject json = new JSONObject();
		json.put("RT", RT);
		System.out.println("-- 함수 종료 : searchSelect.do --");
		return modelAndView(json);
	}
	// 미완료
	@RequestMapping(value = "/search/foretSearchName.do")
	public ModelAndView foretSearchName(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : foretSearchName.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		
		String name = request.getParameter("name");
		List<ForetDTO> list = searchService.foretSearchName(name);
		if(list != null) {
			RT = "OK";
		}
		JSONObject json = new JSONObject();
//		if(list != null) {
//			RT = "OK";
//			json.put("RT", RT);
//			for(ForetDTO foretDTO : list){
//				ForetDTO foret = searchService.foretSelect(foretDTO.getId());
//				System.out.println("포레_태그 정보 불러오기");
//				List<ForetTagDTO> tagList= searchService.foretTagSelect(foretDTO.getId());
//				System.out.println("포레_지역 정보 불러오기");
//				List<ForetRegionDTO> regionList = searchService.foretRegionSelect(foretDTO.getId());
//				System.out.println("포레_멤버 정보 불러오기");
//				List<ForetMemberDTO> foretMemberList = searchService.foretMemberSelect(foretDTO.getId());
//				JSONArray foretArray = new JSONArray();
//				JSONObject foretTemp = new JSONObject();
//				foretTemp.put("id", foretDTO.getId());
//				foretTemp.put("leader_id", foretDTO.getLeader_id());
//				foretTemp.put("name", foretDTO.getName());
//				foretTemp.put("introduce", foretDTO.getIntroduce());
//				foretTemp.put("max_number", foretDTO.getMax_member());
//				foretTemp.put("reg_date", foretDTO.getReg_date());
//				foretTemp.put("photo", foretDTO.getPhoto());
//				
//				if(tagList != null) {
//					JSONArray tag = new JSONArray();
//					for(ForetTagDTO foretTagDTO : tagList) {
//						tag.put(foretTagDTO.getTag_name());
//					}
//					foretTemp.put("tag", tag);
//				}
//				if(regionList != null) {
//					JSONArray region_si = new JSONArray();
//					JSONArray region_gu = new JSONArray();
//					for(ForetRegionDTO foretRegionDTO : regionList) {
//						region_si.put(foretRegionDTO.getRegion_si());
//						region_gu.put(foretRegionDTO.getRegion_gu());
//					}
//					foretTemp.put("region_si", region_si);
//					foretTemp.put("region_gu", region_gu);
//				}
//				if(foretMemberList != null) {
//					JSONArray foret_member = new JSONArray();
//					for(ForetMemberDTO foretMemberDTO : foretMemberList) {
//						foret_member.put(foretMemberDTO.getMember_id());
//					}
//					foretTemp.put("foret_member", foret_member);
//				}
//				foretArray.put(foretTemp);
//				json.put("foret", foretArray);
//			}
//			
//			json.put("foret", list);
//		}
		json.put("RT", RT);
		System.out.println("-- 함수 종료 : foretSearchName.do --");
		return modelAndView(json);
	}
	@RequestMapping(value = "/search/foretSearchTag.do")
	public ModelAndView foretSearchTag(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : foretSearchTag.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		
		String tag = request.getParameter("tag");
		List<ForetTagDTO> list = searchService.foretSearchTag(tag);
		
		JSONObject json = new JSONObject();
		if(list != null) {
			RT = "OK";
			json.put("foret", list);
		}
		json.put("RT", RT);
		System.out.println("-- 함수 종료 : foretSearchTag.do --");
		return modelAndView(json);
	}
	@RequestMapping(value = "/search/foretSearchRegion.do")
	public ModelAndView foretSearchRegion(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : foretSearchRegion.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		
		String region_si = request.getParameter("region_si");
		String region_gu = request.getParameter("region_gu");
		
		List<ForetRegionDTO> list = searchService.foretSearchRegion(region_si, region_gu);
		
		JSONObject json = new JSONObject();
		if(list != null) {
			RT = "OK";
			json.put("foret", list);
		}
		json.put("RT", RT);
		System.out.println("-- 함수 종료 : foretSearchRegion.do --");
		return modelAndView(json);
	}
	
	
	
	@RequestMapping(value = "/search/homeFragement.do")
	public ModelAndView homeFragement(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : homeFragement.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		int member_id = haveId(request.getParameter("member_id"));
		List<BoardDTO> list = searchService.homeFragement(member_id);
		JSONObject json = new JSONObject();
		if(list != null) {
			RT = "OK";
			json.put("board", list);
		}
		json.put("RT", RT);
		System.out.println("-- 함수 종료 : homeFragement.do --");
		return modelAndView(json);
	}
	
	@RequestMapping(value = "/search/commentList.do")
	public ModelAndView commentList(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : commentList.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		int board_id = haveId(request.getParameter("board_id"));
		List<CommentDTO> list = searchService.commentList(board_id);
		JSONObject json = new JSONObject();
		if(list != null) {
			RT = "OK";
			json.put("comment", list);
		}
		json.put("RT", RT);
		System.out.println("-- 함수 종료 : commentList.do --");
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
