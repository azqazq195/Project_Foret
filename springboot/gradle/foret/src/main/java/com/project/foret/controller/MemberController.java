package com.project.foret.controller;

import java.util.List;

import com.project.foret.model.Member;
import com.project.foret.repository.MemberRepository;
import com.project.foret.service.MemberService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private MemberService memberService;
    private MemberRepository memberRepository;

    @PostMapping(value = "")
    public Member insert(Member member, MultipartFile[] file) throws Exception {
        return memberService.save(member, file);
    }

    @GetMapping(value = "")
    public Member get(@RequestParam("id") Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    @GetMapping(value = "/all")
    public List<Member> getAll() {
        return memberRepository.findAll();
    }

    @DeleteMapping(value = "")
    public void delete(@RequestParam("id") Long id) {
        memberRepository.deleteById(id);
    }

}
