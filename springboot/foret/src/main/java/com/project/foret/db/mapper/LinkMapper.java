package com.project.foret.db.mapper;

import java.util.List;

import com.project.foret.db.model.Link;
import com.project.foret.db.model.Photo;
import com.project.foret.db.model.Region;
import com.project.foret.db.model.Tag;

public interface LinkMapper {
    public int memberTagInsert(List<Tag> tag) throws Exception;

    public int memberTagDelete(int member_id) throws Exception;

    public int memberRegionInsert(List<Region> region) throws Exception;

    public int memberRegionDelete(int member_id) throws Exception;

    public int memberPhotoInsert(List<Photo> photo) throws Exception;

    public int memberPhotoDelete(int member_id) throws Exception;

    public int memberForetInsert(Link link);

    public int memberForetDelete(Link link);

    public int likeBoardInsert(Link link);

    public int likeBoardDelete(Link link);

    public int likeCommentInsert(Link link);

    public int likeCommentDelete(Link link);

    public int foretTagInsert(List<Tag> tag) throws Exception;

    public int foretTagDelete(int foret_id) throws Exception;

    public int foretRegionInsert(List<Region> region) throws Exception;

    public int foretRegionDelete(int foret_id) throws Exception;

    public int foretPhotoInsert(List<Photo> photo) throws Exception;

    public int foretPhotoDelete(int foret_id) throws Exception;

    public int boardPhotoInsert(List<Photo> photo) throws Exception;

    public int boardPhotoDelete(int board_id) throws Exception;
}
