package com.project.foret.db.service;

import com.project.foret.db.mapper.ReadMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReadService {
    @Autowired
    ReadMapper readMapper;
}
