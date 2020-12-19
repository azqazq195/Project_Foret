package com.project.foret.db.service;

import java.util.List;

import com.project.foret.db.mapper.ReadMapper;
import com.project.foret.db.model.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReadService {
    @Autowired
    ReadMapper readMapper;

    public List<Tag> getTagList() throws Exception {
        System.out.println("--- getTagList");
        return readMapper.getTagList();
    }
}
