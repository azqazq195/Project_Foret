package com.project.foret.db.service;

import com.project.foret.db.dto.MemberDTO;
import com.project.foret.db.mapper.MemberMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    @Autowired
    MemberMapper memberMapper;

    public MemberDTO emailCheck(MemberDTO memberDTO) throws Exception {
        return memberMapper.emailCheck(memberDTO);
    }

    public MemberDTO login(MemberDTO memberDTO) throws Exception {
        return memberMapper.login(memberDTO);
    }

    public int insert(MemberDTO memberDTO) throws Exception {
        return memberMapper.insert(memberDTO);
    }
}
