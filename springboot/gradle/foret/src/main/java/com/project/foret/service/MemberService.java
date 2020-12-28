package com.project.foret.service;

import java.util.ArrayList;
import java.util.List;

import com.project.foret.model.Member;
import com.project.foret.model.Region;
import com.project.foret.model.Tag;
import com.project.foret.repository.MemberRepository;
import com.project.foret.repository.RegionRepository;
import com.project.foret.repository.TagRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberService {

    private MemberRepository memberRepository;
    private TagRepository tagRepository;
    private RegionRepository regionRepository;

    public Member save(Member member) {

        List<Tag> tags = new ArrayList<>();
        List<Region> regions = new ArrayList<>();
        for (Tag tag : member.getTags()) {
            tags.add(tagRepository.findByTagName(tag.getTagName()));
        }
        for (Region region : member.getRegions()) {
            regions.add(regionRepository.findByRegionSiAndRegionGu(region.getRegionSi(), region.getRegionGu()));
        }
        member.setTags(tags);
        member.setRegions(regions);
        return memberRepository.save(member);
    }
}
