package com.project.foret.db.mapper;

import java.util.List;

import com.project.foret.db.model.Tag;

public interface ReadMapper {
    public List<Tag> getTagList() throws Exception;
}
