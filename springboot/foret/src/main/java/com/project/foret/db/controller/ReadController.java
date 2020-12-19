package com.project.foret.db.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.project.foret.db.helper.Helper;
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

    @RequestMapping(value = "/tag/list", method = RequestMethod.POST)
    public ModelAndView getList(HttpServletRequest request) throws Exception {
        System.out.println("--- tag getList 실행 ---");
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

        System.out.println("--- tag getList 종료 ---\n");
        return helper.modelAndView(json, "tag");
    }
}
