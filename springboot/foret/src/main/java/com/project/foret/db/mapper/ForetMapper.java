package com.project.foret.db.mapper;

import com.project.foret.db.model.Foret;

public interface ForetMapper {
    public int foretInsert(Foret foret) throws Exception;

    public int foretUpdate(Foret foret) throws Exception;

    public int foretDelete(Foret foret) throws Exception;
}
