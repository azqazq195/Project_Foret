package com.project.foret.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.project.foret.model.Member;
import com.project.foret.model.MemberPhoto;
import com.project.foret.model.Region;
import com.project.foret.model.Tag;
import com.project.foret.repository.MemberRepository;
import com.project.foret.repository.RegionRepository;
import com.project.foret.repository.TagRepository;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberService {

    private MemberRepository memberRepository;
    private TagRepository tagRepository;
    private RegionRepository regionRepository;

    public Member save(Member newMember, MultipartFile[] newFile) throws Exception {
        List<Tag> tagList = newMember.getTags();
        List<Region> regionList = newMember.getRegions();

        Member member = newMember;
        member.setTags(null);
        member.setRegions(null);
        member.setPhotos(null);

        if (tagList != null) {
            for (Tag tag : tagList) {
                member.addTag(tagRepository.findByTagName(tag.getTagName()));
            }
        }
        if (regionList != null) {
            for (Region region : regionList) {
                member.addRegion(
                        regionRepository.findByRegionSiAndRegionGu(region.getRegionSi(), region.getRegionGu()));
            }
        }

        if (newFile != null) {
            for (MultipartFile photo : newFile) {
                String dir = System.getProperty("user.dir") + "/src/main/resources/storage";
                String originname = photo.getOriginalFilename();
                String filename = photo.getOriginalFilename();
                int lastIndex = originname.lastIndexOf(".");
                String filetype = originname.substring(lastIndex + 1);
                int filesize = (int) photo.getSize();
                File file = new File(dir, filename);
                FileCopyUtils.copy(photo.getInputStream(), new FileOutputStream(file));

                MemberPhoto memberPhoto = new MemberPhoto();
                memberPhoto.setDir(dir);
                memberPhoto.setOriginname(originname);
                memberPhoto.setFilename(filename);
                memberPhoto.setFiletype(filetype);
                memberPhoto.setFilesize(filesize);
                member.addPhoto(memberPhoto);
            }
        }
        return memberRepository.save(member);
    }
}
