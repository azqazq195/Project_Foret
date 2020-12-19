package com.project.foret.db.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.project.foret.db.helper.Helper;
import com.project.foret.db.model.Member;
import com.project.foret.db.model.Tag;
import com.project.foret.db.service.ReadService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReadController {
    @Autowired
    ReadService readService;
    @Autowired
    Helper helper;

    @RequestMapping(value = "/get/member", method = RequestMethod.POST)
    public ModelAndView getMember(HttpServletRequest request) throws Exception {
        System.out.println("--- getMember 실행 ---");
        request.setCharacterEncoding("UTF-8");
        String RT = "FAIL";

        int member_id = helper.isNum(request.getParameter("id"));

        List<Member> list = readService.getMember(member_id);
        List<String> stringlist1 = new ArrayList<String>();
        List<String> stringlist2 = new ArrayList<String>();
        List<Integer> intlist1 = new ArrayList<Integer>();
        List<Integer> intlist2 = new ArrayList<Integer>();
        List<Integer> intlist3 = new ArrayList<Integer>();

        JSONObject json = new JSONObject();
        if (list != null) {
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
            temp.put("deviceToken", list.get(0).getDevice_token());

            JSONArray tagArray = new JSONArray();
            JSONArray region_siArray = new JSONArray();
            JSONArray region_guArray = new JSONArray();
            JSONArray like_boardArray = new JSONArray();
            JSONArray like_commentArray = new JSONArray();
            JSONArray foret_idArray = new JSONArray();

            for (Member memberALLDTO : list) {
                String tag_name = memberALLDTO.getTag_name();
                String region_si = memberALLDTO.getRegion_si();
                String region_gu = memberALLDTO.getRegion_gu();
                String region = region_si + region_gu;
                int like_board = memberALLDTO.getLike_board();
                int like_comment = memberALLDTO.getLike_comment();
                int foret_id = memberALLDTO.getForet_id();

                if (!stringlist1.contains(tag_name)) {
                    stringlist1.add(tag_name);
                    tagArray.put(tag_name);
                }
                if (!stringlist2.contains(region)) {
                    stringlist2.add(region);
                    region_siArray.put(region_si);
                    region_guArray.put(region_gu);
                }
                if (!intlist1.contains(like_board)) {
                    intlist1.add(like_board);
                    like_boardArray.put(like_board);
                }
                if (!intlist2.contains(like_comment)) {
                    intlist2.add(like_comment);
                    like_commentArray.put(like_comment);
                }
                if (!intlist3.contains(foret_id)) {
                    intlist3.add(foret_id);
                    foret_idArray.put(foret_id);
                }
            }

            if (list.get(0).getFilename() == null) {
                temp.put("photo", 0);
            } else {
                temp.put("photo", list.get(0).getFilename());
            }
            if (tagArray.isNull(0)) {
                temp.put("tag", 0);
            } else {
                temp.put("tag", tagArray);
            }
            if (region_siArray.isNull(0)) {
                temp.put("region_si", 0);
                temp.put("region_gu", 0);
            } else {
                temp.put("region_si", region_siArray);
                temp.put("region_gu", region_guArray);
            }
            if (like_boardArray.getInt(0) == 0) {
                temp.put("like_board", 0);
            } else {
                temp.put("like_board", like_boardArray);
            }
            if (like_commentArray.getInt(0) == 0) {
                temp.put("like_comment", 0);
            } else {
                temp.put("like_comment", like_commentArray);
            }
            if (foret_idArray.getInt(0) == 0) {
                temp.put("foret_id", 0);
            } else {
                temp.put("foret_id", foret_idArray);
            }
            array.put(temp);
            json.put("member", array);
        }
        json.put("RT", RT);

        System.out.println("--- getMember 종료 ---\n");
        return helper.modelAndView(json, "member");
    }

    @RequestMapping(value = "/get/tagList", method = RequestMethod.POST)
    public ModelAndView getTagList(HttpServletRequest request) throws Exception {
        System.out.println("--- getTagList 실행 ---");
        request.setCharacterEncoding("UTF-8");

        List<Tag> tagList = readService.getTagList();
        String RT = helper.isOK(tagList.size());

        JSONObject json = new JSONObject();
        json.put("tagRT", RT);

        if (tagList != null) {
            JSONArray array = new JSONArray();
            for (Tag tag : tagList) {
                JSONObject temp = new JSONObject();
                temp.put("tag_id", tag.getId());
                temp.put("tag_name", tag.getTag_name());
                array.put(temp);
            }
            json.put("tag", array);
        }

        System.out.println("--- getTagList 종료 ---\n");
        return helper.modelAndView(json, "tag");
    }
}
