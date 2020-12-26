package com.project.foret.Controller;

import java.util.List;

import com.project.foret.Model.Member;
import com.project.foret.Model.MemberTag;
import com.project.foret.Repository.MemberRepository;

import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@AllArgsConstructor
public class MemberController {

    private MemberRepository memberRepository;

    @GetMapping(value = "/member/getAll")
    public List<Member> getAll() {
        return memberRepository.findAll();
    }

    @GetMapping(value = "/member/get")
    public Member get(@RequestParam("id") String id) {
        return memberRepository.findMemberById(Integer.parseInt(id));
    }

    @GetMapping(value = "/member/insert")
    public Member insert() {
        Member member = new Member("name", "email", "password", "nickname", "1994-10-24", "device_token");

        MemberTag tag1 = new MemberTag("태그1");
        MemberTag tag2 = new MemberTag("태그2");
        MemberTag tag3 = new MemberTag("태그3");

        member.getTag().add(tag1);
        member.getTag().add(tag2);
        member.getTag().add(tag3);

        return memberRepository.save(member);
    }

}
