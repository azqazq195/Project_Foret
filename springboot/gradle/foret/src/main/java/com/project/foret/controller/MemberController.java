package com.project.foret.controller;

import com.project.foret.model.Member;
import com.project.foret.service.MemberService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private MemberService memberService;

    @PostMapping(value = "/")
    public Member member(Member member, MultipartFile photo) {
        return memberService.save(member);
    }

    // @PostMapping(value = "/")
    // public Member member(@RequestBody Member member, MultipartFile photo) {
    // return memberService.save(member);
    // }

}
