package com.project.foret.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.foret.Model.Member.Member;
import com.project.foret.Repository.Member.MemberRepository;

import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@AllArgsConstructor
public class MemberController {

    private MemberRepository memberRepository;

    @GetMapping(value = "/member/getAll")
    public Map<String, Object> getAll() {
        Map<String, Object> map = new HashMap<>();
        List<Member> list = memberRepository.findAll();
        String result = list.size() > 0 ? "OK" : "FAIL";
        map.put("RT", result);
        map.put("member", list);
        return map;
    }

    @GetMapping(value = "/member/get")
    public Map<String, Object> get(@RequestParam("id") String id) {
        Map<String, Object> map = new HashMap<>();
        List<Member> list = new ArrayList<>();
        list.add(memberRepository.findMemberById(Integer.parseInt(id)));
        String result = list.size() > 0 ? "OK" : "FAIL";
        map.put("RT", result);
        map.put("member", list);
        return map;
    }

    @GetMapping(value = "/member/insert")
    public Member insert() {
        Member member = new Member("name", "email23d2", "password", "nickname", "1994-10-24", "device_token");

        return memberRepository.save(member);
    }

    @GetMapping(value = "/member/delete")
    public void delete(@RequestParam("id") String id) {
        memberRepository.deleteById(Integer.parseInt(id));
    }

}
