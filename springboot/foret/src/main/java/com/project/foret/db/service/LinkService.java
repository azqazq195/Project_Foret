package com.project.foret.db.service;

import java.util.List;

import com.project.foret.db.mapper.LinkMapper;
import com.project.foret.db.model.Link;
import com.project.foret.db.model.Photo;
import com.project.foret.db.model.Region;
import com.project.foret.db.model.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkService {
    @Autowired
    LinkMapper linkMapper;

    // kind
    private static final int MEMBER = 1;
    private static final int FORET = 2;
    private static final int BOARD = 3;

    private static final int MEMBERFORET = 4;
    private static final int LIKEBOARD = 5;
    private static final int LIKECOMMENT = 6;

    public int linkTagInsert(List<Tag> tag, int kind) throws Exception {
        switch (kind) {
            case MEMBER:
                System.out.println("--- memberTagInsert");
                return linkMapper.memberTagInsert(tag);
            case FORET:
                System.out.println("--- foretTagInsert");
                return linkMapper.foretTagInsert(tag);
            default:
                return 0;
        }
    }

    public int linkTagDelete(int reference_id, int kind) throws Exception {
        switch (kind) {
            case MEMBER:
                System.out.println("--- memberTagDelete");
                return linkMapper.memberTagDelete(reference_id);
            case FORET:
                System.out.println("--- foretTagDelete");
                return linkMapper.foretTagDelete(reference_id);
            default:
                return 0;
        }
    }

    public int linkRegionInsert(List<Region> region, int kind) throws Exception {
        switch (kind) {
            case MEMBER:
                System.out.println("--- memberRegionInsert");
                return linkMapper.memberRegionInsert(region);
            case FORET:
                System.out.println("--- foretRegionInsert");
                return linkMapper.foretRegionInsert(region);
            default:
                return 0;
        }
    }

    public int linkRegionDelete(int reference_id, int kind) throws Exception {
        switch (kind) {
            case MEMBER:
                System.out.println("--- memberRegionDelete");
                return linkMapper.memberRegionDelete(reference_id);
            case FORET:
                System.out.println("--- foretRegionDelete");
                return linkMapper.foretRegionDelete(reference_id);
            default:
                return 0;
        }
    }

    public int linkPhotoInsert(List<Photo> photo, int kind) throws Exception {
        switch (kind) {
            case MEMBER:
                System.out.println("--- memberPhotoInsert");
                return linkMapper.memberPhotoInsert(photo);
            case FORET:
                System.out.println("--- foretPhotoInsert");
                return linkMapper.foretPhotoInsert(photo);
            case BOARD:
                System.out.println("--- boardPhotoInsert");
                return linkMapper.boardPhotoInsert(photo);
            default:
                return 0;
        }
    }

    public int linkPhotoDelete(int reference_id, int kind) throws Exception {
        switch (kind) {
            case MEMBER:
                System.out.println("--- memberPhotoDelete");
                return linkMapper.memberPhotoDelete(reference_id);
            case FORET:
                System.out.println("--- foretPhotoDelete");
                return linkMapper.foretPhotoDelete(reference_id);
            case BOARD:
                System.out.println("--- boardPhotoDelete");
                return linkMapper.boardPhotoDelete(reference_id);
            default:
                return 0;
        }
    }

    public int linkInsert(Link link, int kind) throws Exception {
        switch (kind) {
            case MEMBERFORET:
                System.out.println("--- memberForetInsert");
                return linkMapper.memberForetInsert(link);
            case LIKEBOARD:
                System.out.println("--- likeBoardInsert");
                return linkMapper.likeBoardInsert(link);
            case LIKECOMMENT:
                System.out.println("--- likeCommentInsert");
                return linkMapper.likeCommentInsert(link);
            default:
                return 0;
        }
    }

    public int linkDelete(Link link, int kind) throws Exception {
        switch (kind) {
            case MEMBERFORET:
                System.out.println("--- memberForetDelete");
                return linkMapper.memberForetDelete(link);
            case LIKEBOARD:
                System.out.println("--- likeBoardDelete");
                return linkMapper.likeBoardDelete(link);
            case LIKECOMMENT:
                System.out.println("--- likeCommentDelete");
                return linkMapper.likeCommentDelete(link);
            default:
                return 0;
        }
    }

}
