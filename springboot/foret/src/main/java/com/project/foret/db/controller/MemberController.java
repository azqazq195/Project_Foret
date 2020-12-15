package com.project.foret.db.controller;

import javax.servlet.http.HttpServletRequest;

import com.project.foret.db.helper.Helper;
import com.project.foret.db.model.Member;
import com.project.foret.db.service.LinkService;
import com.project.foret.db.service.MemberService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MemberController {
    @Autowired
    MemberService memberService;
    @Autowired
    LinkService linkService;
    @Autowired
    Helper helper;

    // kind
    private static final int MEMBER = 1;

    @RequestMapping(value = "/member/emailCheck", method = RequestMethod.POST)
    public ModelAndView emailCheck(HttpServletRequest request) throws Exception {
        System.out.println("--- member emailCheck 실행 ---");
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        Member member = new Member(email);

        member = memberService.emailCheck(member);
        int result = member == null ? 0 : member.getId();
        String RT = result > 0 ? "FAIL" : "OK";

        JSONObject json = new JSONObject();
        json.put("RT", RT);

        System.out.println("--- member emailCheck 종료 ---\n");
        return helper.modelAndView(json);
    }

    @RequestMapping(value = "/member/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request) throws Exception {
        System.out.println("--- member login 실행 ---");
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Member member = new Member(email, password);

        member = memberService.login(member);
        int result = member == null ? 0 : member.getId();
        String RT = result > 0 ? "OK" : "FAIL";

        JSONObject json = new JSONObject();
        json.put("RT", RT);

        if (RT.equals("OK")) {
            JSONArray array = new JSONArray();
            JSONObject temp = new JSONObject();
            temp.put("id", member.getId());
            temp.put("name", member.getName());
            temp.put("email", member.getEmail());
            temp.put("password", member.getPassword());
            temp.put("nickname", member.getNickname());
            temp.put("birth", member.getBirth());
            temp.put("reg_date", member.getReg_date());
            array.put(temp);
            json.put("member", array);
        }

        System.out.println("--- member login 종료 ---\n");
        return helper.modelAndView(json);
    }

    @RequestMapping(value = "/member/insert", method = RequestMethod.POST)
    public ModelAndView insert(HttpServletRequest request, MultipartFile photo) throws Exception {
        System.out.println("--- member insert 실행 ---");
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String birth = request.getParameter("birth");

        Member member = new Member();
        member.setName(name);
        member.setEmail(email);
        member.setPassword(password);
        member.setNickname(nickname);
        member.setBirth(birth);

        memberService.memberInsert(member);
        int member_id = member.getId();
        String memberRT = member_id > 0 ? "OK" : "FAIL";

        JSONObject json = new JSONObject();
        json.put("memberRT", memberRT);

        if (memberRT.equals("OK")) {
            json.put("memberTagRT", helper.linkTagInsert(MEMBER, member_id, request));
            json.put("memberRegionRT", helper.linkRegionInsert(MEMBER, member_id, request));
            json.put("memberPhotoRT", helper.linkPhotoInsert(MEMBER, member_id, request, photo));
        }

        System.out.println("--- member insert 종료 ---\n");
        return helper.modelAndView(json);
    }

    @RequestMapping(value = "/member/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest request, MultipartFile photo) throws Exception {
        System.out.println("--- member update 실행 ---");
        request.setCharacterEncoding("UTF-8");

        int member_id = helper.isNum(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String birth = request.getParameter("birth");

        Member member = new Member();
        member.setId(member_id);
        member.setName(name);
        member.setEmail(email);
        member.setPassword(password);
        member.setNickname(nickname);
        member.setBirth(birth);

        int result;

        try {
            result = memberService.memberUpdate(member);
        } catch (Exception e) {
            System.out.println(e.getCause());
            result = 0;
        }

        String memberRT = result > 0 ? "OK" : "FAIL";

        JSONObject json = new JSONObject();
        json.put("memberRT", memberRT);

        if (memberRT.equals("OK")) {
            helper.linkTagDelete(member_id, MEMBER);
            helper.linkRegionDelete(member_id, MEMBER);
            helper.linkPhotoDelete(member_id, MEMBER);
            json.put("memberTagRT", helper.linkTagInsert(MEMBER, member_id, request));
            json.put("memberRegionRT", helper.linkRegionInsert(MEMBER, member_id, request));
            json.put("memberPhotoRT", helper.linkPhotoInsert(MEMBER, member_id, request, photo));
        }

        System.out.println("--- member update 종료 ---\n");
        return helper.modelAndView(json);
    }

    @RequestMapping(value = "/member/delete", method = RequestMethod.POST)
    public ModelAndView delete(HttpServletRequest request) throws Exception {
        System.out.println("--- member delete 실행 ---");
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");

        Member member = new Member(email);

        int result = memberService.memberDelete(member);
        String RT = result > 0 ? "OK" : "FAIL";

        JSONObject json = new JSONObject();
        json.put("memberRT", RT);

        System.out.println("--- member delete 종료 ---\n");
        return helper.modelAndView(json);
    }

}
