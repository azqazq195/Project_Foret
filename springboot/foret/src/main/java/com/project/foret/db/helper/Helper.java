package com.project.foret.db.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.project.foret.db.model.Photo;
import com.project.foret.db.model.Region;
import com.project.foret.db.model.Tag;
import com.project.foret.db.service.LinkService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

public class Helper {
    @Autowired
    LinkService linkService;

    public String linkTagInsert(int kind, int reference_id, HttpServletRequest request) throws Exception {
        System.out.println("--- linkTagInsert 실행 ---");
        String tag[] = request.getParameterValues("tag");
        String linkTagRT = "EMPTY";

        if (tag != null) {
            List<Tag> tagList = new ArrayList<>();
            for (int i = 0; i < tag.length; i++) {
                tagList.add(new Tag(reference_id, tag[i]));
            }
            int linkTagResult = linkService.linkTagInsert(tagList, kind);
            linkTagRT = linkTagResult > 0 ? "OK" : "FAIL";
        }

        System.out.println("--- linkTagInsert 종료 ---\n");
        return linkTagRT;
    }

    public String linkRegionInsert(int kind, int reference_id, HttpServletRequest request) throws Exception {
        System.out.println("--- linkRegionInsert 실행 ---");
        String region_si[] = request.getParameterValues("region_si");
        String region_gu[] = request.getParameterValues("region_gu");
        String linkRegionRT = "EMPTY";

        if (region_si != null) {
            List<Region> regionList = new ArrayList<>();
            for (int i = 0; i < region_si.length; i++) {
                regionList.add(new Region(reference_id, region_si[i], region_gu[i]));
            }
            int linkRegionResult = linkService.linkRegionInsert(regionList, kind);
            linkRegionRT = linkRegionResult > 0 ? "OK" : "FAIL";
        }

        System.out.println("--- linkRegionInsert 종료 ---\n");
        return linkRegionRT;
    }

    public String linkPhotoInsert(int kind, int reference_id, HttpServletRequest request, MultipartFile photo)
            throws Exception {
        System.out.println("--- linkPhotoInsert 실행 ---");
        String linkPhotoRT = "EMPTY";

        if (photo != null) {
            String dir = request.getSession().getServletContext().getRealPath("/storage");
            String originname = photo.getOriginalFilename();
            String filename = photo.getOriginalFilename();
            int lastIndex = originname.lastIndexOf(".");
            String filetype = originname.substring(lastIndex + 1);
            int filesize = (int) photo.getSize();
            File file = new File(dir, filename);
            FileCopyUtils.copy(photo.getInputStream(), new FileOutputStream(file));

            Photo photoVO = new Photo();
            photoVO.setDir(dir);
            photoVO.setOriginname(originname);
            photoVO.setFilename(filename);
            photoVO.setFiletype(filetype);
            photoVO.setFilesize(filesize);
            photoVO.setReference_id(reference_id);

            int linkPhotoResult = linkService.linkPhotoInsert(photoVO, kind);
            linkPhotoRT = linkPhotoResult > 0 ? "OK" : "FAIL";
        }
        System.out.println("--- linkPhotoInsert 종료 ---\n");
        return linkPhotoRT;
    }

    public void linkTagDelete(int reference_id, int kind) throws Exception {
        System.out.println("--- linkTagDelete 실행 ---");
        linkService.linkTagDelete(reference_id, kind);
        System.out.println("--- linkTagDelete 종료 ---\n");
    }

    public void linkRegionDelete(int reference_id, int kind) throws Exception {
        System.out.println("--- linkRegionDelete 실행 ---");
        linkService.linkRegionDelete(reference_id, kind);
        System.out.println("--- linkRegionDelete 종료 ---\n");
    }

    public void linkPhotoDelete(int reference_id, int kind) throws Exception {
        System.out.println("--- linkPhotoDelete 실행 ---");
        linkService.linkPhotoDelete(reference_id, kind);
        System.out.println("--- linkPhotoDelete 종료 ---\n");
    }

    public int isNum(String string) {
        return string == null ? 0 : Integer.parseInt(string);
    }

    public ModelAndView modelAndView(JSONObject json) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("json", json);
        modelAndView.setViewName("member");
        return modelAndView;
    }
}
