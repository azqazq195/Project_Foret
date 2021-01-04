// package com.project.foret.service;

// import java.io.File;
// import java.io.FileOutputStream;
// import java.util.List;

// import com.project.foret.model.Foret;
// import com.project.foret.model.ForetPhoto;
// import com.project.foret.model.Member;
// import com.project.foret.model.Region;
// import com.project.foret.model.Tag;
// import com.project.foret.repository.ForetRepository;
// import com.project.foret.repository.MemberRepository;
// import com.project.foret.repository.RegionRepository;
// import com.project.foret.repository.TagRepository;

// import org.springframework.stereotype.Service;
// import org.springframework.util.FileCopyUtils;
// import org.springframework.web.multipart.MultipartFile;

// import lombok.AllArgsConstructor;

// @Service
// @AllArgsConstructor
// public class ForetService {

// private ForetRepository foretRepository;
// private MemberRepository memberRepository;
// private TagRepository tagRepository;
// private RegionRepository regionRepository;

// public Foret save(Foret newForet, MultipartFile[] newFile) throws Exception {
// List<Tag> tagList = newForet.getTags();
// List<Region> regionList = newForet.getRegions();

// Foret foret = newForet;
// foret.setTags(null);
// foret.setRegions(null);
// foret.setPhotos(null);
// foret.setMembers(null);
// foret.addMember(memberRepository.findById(foret.getLeader_id()).orElse(null));

// if (tagList != null) {
// for (Tag tag : tagList) {
// foret.addTag(tagRepository.findByTagName(tag.getTagName()));
// }
// }
// if (regionList != null) {
// for (Region region : regionList) {
// foret.addRegion(regionRepository.findByRegionSiAndRegionGu(region.getRegionSi(),
// region.getRegionGu()));
// }
// }

// if (newFile != null) {
// for (MultipartFile photo : newFile) {
// String dir = System.getProperty("user.dir") + "/src/main/resources/storage";
// String originname = photo.getOriginalFilename();
// String filename = photo.getOriginalFilename();
// int lastIndex = originname.lastIndexOf(".");
// String filetype = originname.substring(lastIndex + 1);
// int filesize = (int) photo.getSize();
// File file = new File(dir, filename);
// FileCopyUtils.copy(photo.getInputStream(), new FileOutputStream(file));

// ForetPhoto foretPhoto = new ForetPhoto();
// foretPhoto.setDir(dir);
// foretPhoto.setOriginname(originname);
// foretPhoto.setFilename(filename);
// foretPhoto.setFiletype(filetype);
// foretPhoto.setFilesize(filesize);
// foret.addPhoto(foretPhoto);
// }
// }
// return foretRepository.save(foret);
// }
// }
