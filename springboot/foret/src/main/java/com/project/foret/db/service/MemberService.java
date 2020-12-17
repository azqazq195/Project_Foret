package com.project.foret.db.service;

import com.project.foret.db.model.Member;

import com.project.foret.db.mapper.MemberMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    @Autowired
    MemberMapper memberMapper;

    public Member emailCheck(Member member) throws Exception {
        System.out.println("--- member emailCheck");
        return memberMapper.emailCheck(member);
    }

    public Member login(Member member) throws Exception {
        System.out.println("--- member login");
        return memberMapper.login(member);
    }

    public int memberInsert(Member member) throws Exception {
        System.out.println("--- memberInsert");
        return memberMapper.memberInsert(member);
    }

    public int memberUpdate(Member member) throws Exception {
        System.out.println("--- memberUpdate");
        return memberMapper.memberUpdate(member);
    }

    public int memberDelete(Member member) throws Exception {
        System.out.println("--- memberDelete");
        return memberMapper.memberDelete(member);
    }

}
