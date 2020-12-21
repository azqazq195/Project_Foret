package com.project.foret.db.mapper;

import java.util.List;

import com.project.foret.db.model.Member;
import com.project.foret.db.model.Tag;

public interface ReadMapper {
    public List<Member> getMember(int member_id) throws Exception;

    public List<Tag> getTagList() throws Exception;

    public List<Tag> getTagRank() throws Exception;
}
