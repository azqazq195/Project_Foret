package com.project.foret.db.service;

import com.project.foret.db.mapper.ForetMapper;
import com.project.foret.db.model.Foret;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForetService {
    @Autowired
    ForetMapper foretMapper;

    public int foretInsert(Foret foret) throws Exception {
        System.out.println("--- foretInsert");
        return foretMapper.foretInsert(foret);
    }

    public int foretUpdate(Foret foret) throws Exception {
        System.out.println("--- foretUpdate");
        return foretMapper.foretUpdate(foret);
    }

    public int foretDelete(Foret foret) throws Exception {
        System.out.println("--- foretDelete");
        return foretMapper.foretDelete(foret);
    }

}
