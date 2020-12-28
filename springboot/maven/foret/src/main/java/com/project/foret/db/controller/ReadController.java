package com.project.foret.db.controller;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import com.project.foret.db.helper.Helper;
import com.project.foret.db.model.Foret;
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

        System.out.println(list.toString());

        JSONObject json = new JSONObject();

        if (list != null) {
            RT = "OK";
            int tempId = 0;
            TreeSet<String> tagTree = new TreeSet<>();
            TreeSet<String> regionTree = new TreeSet<>();
            TreeSet<String> photoTree = new TreeSet<>();
            TreeSet<Integer> likeBoardTree = new TreeSet<>();
            TreeSet<Integer> likeCommentTree = new TreeSet<>();
            TreeSet<Integer> foretTree = new TreeSet<>();
            JSONArray array = new JSONArray();

            for (int i = 0; i < list.size();) {
                JSONObject member = new JSONObject();
                tempId = list.get(i).getId();
                member.put("id", list.get(i).getId());
                member.put("name", list.get(i).getName());
                member.put("email", list.get(i).getEmail());
                member.put("password", list.get(i).getPassword());
                member.put("nickname", list.get(i).getNickname());
                member.put("birth", list.get(i).getBirth());
                member.put("reg_date", list.get(i).getReg_date());
                member.put("device_token", list.get(i).getDevice_token());

                while (i < list.size() && tempId == list.get(i).getId()) {
                    String tag = list.get(i).getTag_name();
                    String region = list.get(i).getRegion_si();
                    String photo = list.get(i).getFilename();
                    int like_board = list.get(i).getLike_board();
                    int like_comment = list.get(i).getLike_comment();
                    int foret_id = list.get(i).getForet_id();

                    if (tag != null) {
                        tagTree.add(tag);
                    }
                    if (region != null) {
                        regionTree.add(region + " " + list.get(i).getRegion_gu());
                    }
                    if (photo != null) {
                        photoTree.add(photo);
                    }
                    if (like_board != 0) {
                        likeBoardTree.add(like_board);
                    }
                    if (like_comment != 0) {
                        likeCommentTree.add(like_comment);
                    }
                    if (foret_id != 0) {
                        foretTree.add(foret_id);
                    }
                    i++;
                }

                JSONArray tagArray = new JSONArray();
                JSONArray regionSiArray = new JSONArray();
                JSONArray regionGuArray = new JSONArray();
                JSONArray photoArray = new JSONArray();
                JSONArray likeBoardArray = new JSONArray();
                JSONArray likeCommentArray = new JSONArray();
                JSONArray foretArray = new JSONArray();

                Iterator<Integer> iterInt;
                Iterator<String> iterStr;
                iterStr = tagTree.iterator();
                while (iterStr.hasNext()) {
                    tagArray.put(iterStr.next());
                }
                iterStr = regionTree.iterator();
                while (iterStr.hasNext()) {
                    String[] region = iterStr.next().toString().split(" ");
                    regionSiArray.put(region[0]);
                    regionGuArray.put(region[1]);
                }
                iterStr = photoTree.iterator();
                while (iterStr.hasNext()) {
                    photoArray.put(iterStr.next());
                }
                iterInt = likeBoardTree.iterator();
                while (iterInt.hasNext()) {
                    likeBoardArray.put(iterInt.next());
                }
                iterInt = likeCommentTree.iterator();
                while (iterInt.hasNext()) {
                    likeCommentArray.put(iterInt.next());
                }
                iterInt = foretTree.iterator();
                while (iterInt.hasNext()) {
                    foretArray.put(iterInt.next());
                }

                putArray("tag", tagArray, member);
                putArray("region_si", regionSiArray, member);
                putArray("region_gu", regionGuArray, member);
                putArray("photo", photoArray, member);
                putArray("like_board", likeBoardArray, member);
                putArray("like_comment", likeCommentArray, member);
                putArray("foret_id", foretArray, member);
                array.put(member);

                tagTree.clear();
                regionTree.clear();
                photoTree.clear();
                likeBoardTree.clear();
                likeCommentTree.clear();
                foretTree.clear();
            }
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

    @RequestMapping(value = "/get/tagRank", method = RequestMethod.POST)
    public ModelAndView getTagRank(HttpServletRequest request) throws Exception {
        System.out.println("--- getTagRank 실행 ---");
        request.setCharacterEncoding("UTF-8");

        List<Tag> tagList = readService.getTagRank();
        String RT = helper.isOK(tagList.size());

        JSONObject json = new JSONObject();
        json.put("tagRT", RT);

        if (tagList != null) {
            JSONArray array = new JSONArray();
            for (Tag tag : tagList) {
                JSONObject temp = new JSONObject();
                temp.put("tag_name", tag.getTag_name());
                array.put(temp);
            }
            json.put("tag", array);
        }

        System.out.println("--- getTagRank 종료 ---\n");
        return helper.modelAndView(json, "tag");
    }

    @RequestMapping(value = "/get/foretRank", method = RequestMethod.POST)
    public ModelAndView getForetRank(HttpServletRequest request) throws Exception {
        System.out.println("--- getForetRank 실행 ---");
        request.setCharacterEncoding("UTF-8");
        String RT = "FAIL";

        List<Foret> list = readService.getForetRank();
        JSONObject json = new JSONObject();

        if (list != null) {
            RT = "OK";
            int tempId = 0;
            TreeSet<String> tagTree = new TreeSet<>();
            TreeSet<String> regionTree = new TreeSet<>();
            TreeSet<String> photoTree = new TreeSet<>();
            TreeSet<Integer> memberTree = new TreeSet<>();
            JSONArray array = new JSONArray();

            for (int i = 0; i < list.size();) {
                JSONObject foret = new JSONObject();
                tempId = list.get(i).getId();
                foret.put("id", list.get(i).getId());
                foret.put("leader_id", list.get(i).getLeader_id());
                foret.put("name", list.get(i).getName());
                foret.put("introduce", list.get(i).getIntroduce());
                foret.put("max_member", list.get(i).getMax_member());
                foret.put("reg_date", list.get(i).getReg_date());

                while (i < list.size() && tempId == list.get(i).getId()) {
                    String tag = list.get(i).getTag_name();
                    String region = list.get(i).getRegion_si();
                    String photo = list.get(i).getFilename();
                    int member_id = list.get(i).getMember_id();

                    if (tag != null) {
                        tagTree.add(tag);
                    }
                    if (region != null) {
                        regionTree.add(region + " " + list.get(i).getRegion_gu());
                    }
                    if (photo != null) {
                        photoTree.add(photo);
                    }
                    if (member_id != 0) {
                        memberTree.add(member_id);
                    }
                    i++;
                }

                JSONArray tagArray = new JSONArray();
                JSONArray regionSiArray = new JSONArray();
                JSONArray regionGuArray = new JSONArray();
                JSONArray photoArray = new JSONArray();
                JSONArray memberArray = new JSONArray();

                Iterator<Integer> iterInt;
                Iterator<String> iterStr;
                iterStr = tagTree.iterator();
                while (iterStr.hasNext()) {
                    tagArray.put(iterStr.next());
                }
                iterStr = regionTree.iterator();
                while (iterStr.hasNext()) {
                    String[] region = iterStr.next().toString().split(" ");
                    regionSiArray.put(region[0]);
                    regionGuArray.put(region[1]);
                }
                iterStr = photoTree.iterator();
                while (iterStr.hasNext()) {
                    photoArray.put(iterStr.next());
                }
                iterInt = memberTree.iterator();
                while (iterInt.hasNext()) {
                    memberArray.put(iterInt.next());
                }

                putArray("tag", tagArray, foret);
                putArray("region_si", regionSiArray, foret);
                putArray("region_gu", regionGuArray, foret);
                putArray("photo", photoArray, foret);
                putArray("member_id", memberArray, foret);
                array.put(foret);

                tagTree.clear();
                regionTree.clear();
                photoTree.clear();
                memberTree.clear();
            }
            json.put("foret", array);
        }
        json.put("RT", RT);

        System.out.println("--- getForetRank 종료 ---\n");
        return helper.modelAndView(json, "foret");
    }

    public JSONObject putArray(String name, JSONArray array, JSONObject json) {
        if (array.isNull(0)) {
            json.put(name, new JSONArray());
        } else {
            json.put(name, array);
        }
        return json;
    }

}
