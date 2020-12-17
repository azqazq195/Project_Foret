package com.project.foret.db.service;

import com.project.foret.db.mapper.CommentMapper;
import com.project.foret.db.model.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;

    public int commentInsert(Comment comment) throws Exception {
        if (comment.getGroup_id() == 0) {
            System.out.println("--- commentInsert : Parent");
            commentMapper.commentInsert(comment);
            return commentMapper.setGroupId();
        } else {
            System.out.println("--- commentInsert : Child");
            return commentMapper.commentInsert(comment);
        }
    }

    public int commentUpdate(Comment comment) throws Exception {
        System.out.println("--- commentUpdate");
        return commentMapper.commentUpdate(comment);
    }

    public int commentDelete(Comment comment) throws Exception {
        System.out.println("--- commentDelete");
        return commentMapper.commentDelete(comment);
    }
}
