package com.project.foret.db.mapper;

import com.project.foret.db.model.Comment;

public interface CommentMapper {
    public int commentInsert(Comment comment) throws Exception;

    public int commentUpdate(Comment comment) throws Exception;

    public int commentDelete(Comment comment) throws Exception;

    public int setGroupId() throws Exception;

}
