package com.project.foret.db.mapper;

import com.project.foret.db.model.Member;

public interface MemberMapper {
    public Member emailCheck(Member member) throws Exception;

    public Member login(Member member) throws Exception;

    public int memberInsert(Member member) throws Exception;

    public int memberUpdate(Member member) throws Exception;

    public int memberDelete(Member member) throws Exception;
}
