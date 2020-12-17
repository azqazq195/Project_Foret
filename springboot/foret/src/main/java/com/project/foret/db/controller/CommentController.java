package com.project.foret.db.controller;

import javax.servlet.http.HttpServletRequest;

import com.project.foret.db.helper.Helper;
import com.project.foret.db.model.Comment;
import com.project.foret.db.service.CommentService;
import com.project.foret.db.service.LinkService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    LinkService linkService;
    @Autowired
    Helper helper;

    @RequestMapping(value = "/comment/insert", method = RequestMethod.POST)
    public ModelAndView insert(HttpServletRequest request) throws Exception {
        System.out.println("--- comment insert 실행 ---");
        request.setCharacterEncoding("UTF-8");

        int board_id = helper.isNum(request.getParameter("board_id"));
        int writer_id = helper.isNum(request.getParameter("writer_id"));
        int group_id = helper.isNum(request.getParameter("group_id"));
        String content = request.getParameter("content");

        Comment comment = new Comment();
        comment.setBoard_id(board_id);
        comment.setWriter_id(writer_id);
        comment.setGroup_id(group_id);
        comment.setContent(content);

        commentService.commentInsert(comment);
        int comment_id = comment.getId();
        String commentRT = helper.isOK(comment_id);

        JSONObject json = new JSONObject();
        json.put("commentRT", commentRT);
        json.put("comment_id", comment_id);

        System.out.println("--- comment insert 종료 ---\n");
        return helper.modelAndView(json, "comment");
    }

    @RequestMapping(value = "/comment/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest request) throws Exception {
        System.out.println("--- comment update 실행 ---");
        request.setCharacterEncoding("UTF-8");

        int comment_id = helper.isNum(request.getParameter("comment_id"));
        String content = request.getParameter("content");

        Comment comment = new Comment();
        comment.setId(comment_id);
        comment.setContent(content);

        int result;

        try {
            result = commentService.commentUpdate(comment);
        } catch (Exception e) {
            System.out.println(e.getCause());
            result = 0;
        }

        String commentRT = helper.isOK(result);

        JSONObject json = new JSONObject();
        json.put("commentRT", commentRT);

        System.out.println("--- comment update 종료 ---\n");
        return helper.modelAndView(json, "comment");
    }

    @RequestMapping(value = "/comment/delete", method = RequestMethod.POST)
    public ModelAndView delete(HttpServletRequest request) throws Exception {
        System.out.println("--- comment delete 실행 ---");
        request.setCharacterEncoding("UTF-8");

        int comment_id = helper.isNum(request.getParameter("comment_id"));

        Comment comment = new Comment(comment_id);

        int result = commentService.commentDelete(comment);
        String RT = helper.isOK(result);

        JSONObject json = new JSONObject();
        json.put("commentRT", RT);

        System.out.println("--- comment delete 종료 ---\n");
        return helper.modelAndView(json, "comment");
    }
}