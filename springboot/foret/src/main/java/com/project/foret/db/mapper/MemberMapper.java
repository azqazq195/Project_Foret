package com.project.foret.db.mapper;

import com.project.foret.db.dto.MemberDTO;

public interface MemberMapper {
    public MemberDTO emailCheck(MemberDTO memberDTO) throws Exception;

    public MemberDTO login(MemberDTO memberDTO) throws Exception;

    public int insert(MemberDTO memberDTO) throws Exception;
}
