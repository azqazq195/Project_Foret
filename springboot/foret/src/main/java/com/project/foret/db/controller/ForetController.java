package com.project.foret.db.controller;

import javax.servlet.http.HttpServletRequest;

import com.project.foret.db.helper.Helper;
import com.project.foret.db.model.Foret;
import com.project.foret.db.service.ForetService;
import com.project.foret.db.service.LinkService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ForetController {
    @Autowired
    ForetService foretService;
    @Autowired
    LinkService linkService;
    @Autowired
    Helper helper;

    // kind
    private static final int FORET = 2;

    @RequestMapping(value = "/foret/insert", method = RequestMethod.POST)
    public ModelAndView insert(HttpServletRequest request, MultipartFile photo) throws Exception {
        System.out.println("--- foret insert 실행 ---");
        request.setCharacterEncoding("UTF-8");

        int leader_id = helper.isNum(request.getParameter("leader_id"));
        String name = request.getParameter("name");
        String introduce = request.getParameter("introduce");
        int max_member = helper.isNum(request.getParameter("max_member"));

        Foret foret = new Foret();
        foret.setLeader_id(leader_id);
        foret.setName(name);
        foret.setIntroduce(introduce);
        foret.setMax_member(max_member);

        foretService.foretInsert(foret);
        int foret_id = foret.getId();
        String foretRT = foret_id > 0 ? "OK" : "FAIL";

        JSONObject json = new JSONObject();
        json.put("foretRT", foretRT);

        if (foretRT.equals("OK")) {
            json.put("foretTagRT", helper.linkTagInsert(FORET, foret_id, request));
            json.put("foretRegionRT", helper.linkRegionInsert(FORET, foret_id, request));
            json.put("foretPhotoRT", helper.linkPhotoInsert(FORET, foret_id, request, photo));
        }

        System.out.println("--- foret insert 종료 ---\n");
        return helper.modelAndView(json);
    }

    @RequestMapping(value = "/foret/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest request, MultipartFile photo) throws Exception {
        System.out.println("--- foret update 실행 ---");
        request.setCharacterEncoding("UTF-8");

        int foret_id = helper.isNum(request.getParameter("id"));
        int leader_id = helper.isNum(request.getParameter("leader_id"));
        String name = request.getParameter("name");
        String introduce = request.getParameter("introduce");
        int max_member = helper.isNum(request.getParameter("max_member"));

        Foret foret = new Foret();
        foret.setId(foret_id);
        foret.setLeader_id(leader_id);
        foret.setName(name);
        foret.setIntroduce(introduce);
        foret.setMax_member(max_member);

        int result;

        try {
            result = foretService.foretUpdate(foret);
        } catch (Exception e) {
            System.out.println(e.getCause());
            result = 0;
        }

        String foretRT = result > 0 ? "OK" : "FAIL";

        JSONObject json = new JSONObject();
        json.put("foretRT", foretRT);

        if (foretRT.equals("OK")) {
            helper.linkTagDelete(foret_id, FORET);
            helper.linkRegionDelete(foret_id, FORET);
            helper.linkPhotoDelete(foret_id, FORET);
            json.put("foretTagRT", helper.linkTagInsert(FORET, foret_id, request));
            json.put("foretRegionRT", helper.linkRegionInsert(FORET, foret_id, request));
            json.put("foretPhotoRT", helper.linkPhotoInsert(FORET, foret_id, request, photo));
        }

        System.out.println("--- foret update 종료 ---\n");
        return helper.modelAndView(json);
    }

    @RequestMapping(value = "/foret/delete", method = RequestMethod.POST)
    public ModelAndView delete(HttpServletRequest request) throws Exception {
        System.out.println("--- foret delete 실행 ---");
        request.setCharacterEncoding("UTF-8");

        int foret_id = helper.isNum(request.getParameter("id"));

        Foret foret = new Foret(foret_id);

        int result = foretService.foretDelete(foret);
        String RT = result > 0 ? "OK" : "FAIL";

        JSONObject json = new JSONObject();
        json.put("foretRT", RT);

        System.out.println("--- foret delete 종료 ---\n");
        return helper.modelAndView(json);
    }

}
