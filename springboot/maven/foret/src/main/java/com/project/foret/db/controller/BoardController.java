package com.project.foret.db.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.project.foret.db.helper.Helper;
import com.project.foret.db.model.Board;
import com.project.foret.db.model.Photo;
import com.project.foret.db.service.BoardService;
import com.project.foret.db.service.LinkService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BoardController {
    @Autowired
    BoardService boardService;
    @Autowired
    LinkService linkService;
    @Autowired
    Helper helper;

    private static final int BOARD = 3;

    @RequestMapping(value = "/board/insert", method = RequestMethod.POST)
    public ModelAndView insert(HttpServletRequest request, MultipartFile[] photo) throws Exception {
        System.out.println("--- board insert 실행 ---");
        request.setCharacterEncoding("UTF-8");

        String boardPhotoRT = "EMPTY";

        int writer_id = helper.isNum(request.getParameter("writer_id"));
        int foret_id = helper.isNum(request.getParameter("foret_id"));
        int type = helper.isNum(request.getParameter("type"));
        String subject = request.getParameter("subject");
        String content = request.getParameter("content");

        Board board = new Board();
        board.setWriter_id(writer_id);
        board.setForet_id(foret_id);
        board.setType(type);
        board.setSubject(subject);
        board.setContent(content);

        boardService.boardInsert(board);
        int board_id = board.getId();
        String boardRT = helper.isOK(board_id);

        JSONObject json = new JSONObject();
        json.put("boardRT", boardRT);
        json.put("board_id", board_id);

        if (boardRT.equals("OK")) {
            List<Photo> photos = helper.makePhotoList(board_id, request, photo);
            if (photo != null) {
                boardPhotoRT = helper.isOK(linkService.linkPhotoInsert(photos, BOARD));
            }
            json.put("boardPhotoRT", boardPhotoRT);
        }

        System.out.println("--- board insert 종료 ---\n");
        return helper.modelAndView(json, "board");
    }

    @RequestMapping(value = "/board/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest request, MultipartFile[] photo) throws Exception {
        System.out.println("--- board update 실행 ---");
        request.setCharacterEncoding("UTF-8");

        String boardPhotoRT = "EMPTY";

        int board_id = helper.isNum(request.getParameter("board_id"));
        int writer_id = helper.isNum(request.getParameter("writer_id"));
        int foret_id = helper.isNum(request.getParameter("foret_id"));
        int type = helper.isNum(request.getParameter("type"));
        String subject = request.getParameter("subject");
        String content = request.getParameter("content");

        Board board = new Board();
        board.setId(board_id);
        board.setWriter_id(writer_id);
        board.setForet_id(foret_id);
        board.setType(type);
        board.setSubject(subject);
        board.setContent(content);

        int result;

        try {
            result = boardService.boardUpdate(board);
        } catch (Exception e) {
            System.out.println(e.getCause());
            result = 0;
        }

        String boardRT = helper.isOK(result);

        JSONObject json = new JSONObject();
        json.put("boardRT", boardRT);
        json.put("board_id", board_id);

        if (boardRT.equals("OK")) {
            linkService.linkPhotoDelete(board_id, BOARD);
            List<Photo> photos = helper.makePhotoList(board_id, request, photo);
            if (photo != null) {
                boardPhotoRT = helper.isOK(linkService.linkPhotoInsert(photos, BOARD));
            }
            json.put("boardPhotoRT", boardPhotoRT);
        }

        System.out.println("--- board update 종료 ---\n");
        return helper.modelAndView(json, "board");
    }

    @RequestMapping(value = "/board/delete", method = RequestMethod.POST)
    public ModelAndView delete(HttpServletRequest request) throws Exception {
        System.out.println("--- board delete 실행 ---");
        request.setCharacterEncoding("UTF-8");

        int board_id = helper.isNum(request.getParameter("board_id"));

        Board board = new Board(board_id);

        int result = boardService.boardDelete(board);
        String RT = helper.isOK(result);

        JSONObject json = new JSONObject();
        json.put("boardRT", RT);

        System.out.println("--- board delete 종료 ---\n");
        return helper.modelAndView(json, "board");
    }
}
