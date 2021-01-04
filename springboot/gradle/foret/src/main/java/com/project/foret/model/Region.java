package com.project.foret.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "region_si")
    private String regionSi;
    @Column(name = "region_gu")
    private String regionGu;

    // 멤버에 있는 regions변수에 선언된것을 그대로 사용하겠다.
    @ManyToMany(mappedBy = "regions")
    @JsonIgnore
    private List<Member> members;

    public void addMember(Member member) {
        if (members == null) {
            members = new ArrayList<>();
        }
        members.add(member);
    }

    public void removeMember(Member member) {
        members.remove(member);
    }

}