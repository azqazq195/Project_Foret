package com.project.foret.db.controller;

import javax.servlet.http.HttpServletRequest;

import com.project.foret.db.dto.MemberDTO;
import com.project.foret.db.service.MemberService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MemberController {
    @Autowired
    MemberService memberService;

    @RequestMapping("/member/emailCheck")
    public ModelAndView emailCheck(HttpServletRequest request) throws Exception {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        MemberDTO memberDTO = new MemberDTO(email);

        memberDTO = memberService.emailCheck(memberDTO);
        String RT = memberDTO.getId() > 0 ? "FAIL" : "OK";

        JSONObject json = new JSONObject();
        json.put("RT", RT);

        return modelAndView(json);
    }

    @RequestMapping("/member/login")
    public ModelAndView loginCheck(HttpServletRequest request) throws Exception {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        MemberDTO memberDTO = new MemberDTO(email, password);

        memberDTO = memberService.login(memberDTO);
        String RT = memberDTO.getId() > 0 ? "OK" : "FAIL";

        JSONObject json = new JSONObject();
        json.put("RT", RT);

        if (RT.equals("OK")) {
            JSONArray array = new JSONArray();
            JSONObject member = new JSONObject();
            member.put("id", memberDTO.getId());
            member.put("name", memberDTO.getName());
            member.put("email", memberDTO.getEmail());
            member.put("password", memberDTO.getPassword());
            member.put("nickname", memberDTO.getNickname());
            member.put("birth", memberDTO.getBirth());
            member.put("reg_date", memberDTO.getReg_date());
            array.put(member);
            json.put("member", array);
        }

        return modelAndView(json);
    }

    @RequestMapping("/member/insert")
    public ModelAndView insert(HttpServletRequest request) throws Exception {
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String birth = request.getParameter("birth");

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName(name);
        memberDTO.setEmail(email);
        memberDTO.setPassword(password);
        memberDTO.setNickname(nickname);
        memberDTO.setBirth(birth);

        int result = memberService.insert(memberDTO);
        String RT = result > 0 ? "OK" : "FAIL";

        JSONObject json = new JSONObject();
        json.put("RT", RT);

        return modelAndView(json);
    }

    public ModelAndView modelAndView(JSONObject json) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("json", json);
        modelAndView.setViewName("member");
        return modelAndView;
    }
}
