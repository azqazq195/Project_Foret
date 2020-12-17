package com.project.foret.db.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.project.foret.db.helper.Helper;
import com.project.foret.db.model.Foret;
import com.project.foret.db.model.Link;
import com.project.foret.db.model.Photo;
import com.project.foret.db.model.Region;
import com.project.foret.db.model.Tag;
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
    private static final int FORETMEMBER = 4;

    @RequestMapping(value = "/foret/insert", method = RequestMethod.POST)
    public ModelAndView insert(HttpServletRequest request, MultipartFile[] photo) throws Exception {
        System.out.println("--- foret insert 실행 ---");
        request.setCharacterEncoding("UTF-8");

        String foretTagRT = "EMPTY";
        String foretRegionRT = "EMPTY";
        String foretPhotoRT = "EMPTY";

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
        String foretRT = helper.isOK(foret_id);

        JSONObject json = new JSONObject();
        json.put("foretRT", foretRT);

        if (foretRT.equals("OK")) {
            List<Tag> tags = helper.makeTagList(foret_id, request);
            List<Region> regions = helper.makeRegionList(foret_id, request);
            List<Photo> photos = helper.makePhotoList(foret_id, request, photo);
            if (tags != null) {
                foretTagRT = helper.isOK(linkService.linkTagInsert(tags, FORET));
            }
            if (regions != null) {
                foretRegionRT = helper.isOK(linkService.linkRegionInsert(regions, FORET));
            }
            if (photo != null) {
                foretPhotoRT = helper.isOK(linkService.linkPhotoInsert(photos, FORET));
            }
            String foretMemberRT = helper.isOK(linkService.linkInsert(new Link(leader_id, foret_id), FORETMEMBER));

            json.put("foretTagRT", foretTagRT);
            json.put("foretRegionRT", foretRegionRT);
            json.put("foretPhotoRT", foretPhotoRT);
            json.put("foretMemberRT", foretMemberRT);
        }

        System.out.println("--- foret insert 종료 ---\n");
        return helper.modelAndView(json, "foret");
    }

    @RequestMapping(value = "/foret/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest request, MultipartFile[] photo) throws Exception {
        System.out.println("--- foret update 실행 ---");
        request.setCharacterEncoding("UTF-8");

        String foretTagRT = "EMPTY";
        String foretRegionRT = "EMPTY";
        String foretPhotoRT = "EMPTY";

        int foret_id = helper.isNum(request.getParameter("foret_id"));
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
            System.out.println("result : " + result);
        } catch (Exception e) {
            System.out.println(e.getCause());
            result = 0;
        }

        String foretRT = helper.isOK(result);

        JSONObject json = new JSONObject();
        json.put("foretRT", foretRT);

        if (foretRT.equals("OK")) {
            linkService.linkTagDelete(foret_id, FORET);
            linkService.linkRegionDelete(foret_id, FORET);
            linkService.linkPhotoDelete(foret_id, FORET);

            List<Tag> tags = helper.makeTagList(foret_id, request);
            List<Region> regions = helper.makeRegionList(foret_id, request);
            List<Photo> photos = helper.makePhotoList(foret_id, request, photo);
            if (tags != null) {
                foretTagRT = helper.isOK(linkService.linkTagInsert(tags, FORET));
            }
            if (regions != null) {
                foretRegionRT = helper.isOK(linkService.linkRegionInsert(regions, FORET));
            }
            if (photo != null) {
                foretPhotoRT = helper.isOK(linkService.linkPhotoInsert(photos, FORET));
            }

            json.put("foretTagRT", foretTagRT);
            json.put("foretRegionRT", foretRegionRT);
            json.put("foretPhotoRT", foretPhotoRT);
        }

        System.out.println("--- foret update 종료 ---\n");
        return helper.modelAndView(json, "foret");
    }

    @RequestMapping(value = "/foret/delete", method = RequestMethod.POST)
    public ModelAndView delete(HttpServletRequest request) throws Exception {
        System.out.println("--- foret delete 실행 ---");
        request.setCharacterEncoding("UTF-8");

        int foret_id = helper.isNum(request.getParameter("foret_id"));

        Foret foret = new Foret(foret_id);

        int result = foretService.foretDelete(foret);
        String RT = helper.isOK(result);

        JSONObject json = new JSONObject();
        json.put("foretRT", RT);

        System.out.println("--- foret delete 종료 ---\n");
        return helper.modelAndView(json, "foret");
    }

}
