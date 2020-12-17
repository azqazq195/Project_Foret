package com.project.foret.db.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.project.foret.db.model.Link;
import com.project.foret.db.model.Photo;
import com.project.foret.db.model.Region;
import com.project.foret.db.model.Tag;
import com.project.foret.db.service.LinkService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Helper {
    @Autowired
    LinkService linkService;

    public List<Tag> makeTagList(int reference_id, HttpServletRequest request) throws Exception {
        String tag[] = request.getParameterValues("tag");
        if (tag != null) {
            List<Tag> tagList = new ArrayList<>();
            for (int i = 0; i < tag.length; i++) {
                tagList.add(new Tag(reference_id, tag[i]));
            }
            return tagList;
        } else {
            return null;
        }
    }

    public List<Region> makeRegionList(int reference_id, HttpServletRequest request) throws Exception {
        String region_si[] = request.getParameterValues("region_si");
        String region_gu[] = request.getParameterValues("region_gu");

        if (region_si != null) {
            List<Region> regionList = new ArrayList<>();
            for (int i = 0; i < region_si.length; i++) {
                regionList.add(new Region(reference_id, region_si[i], region_gu[i]));
            }
            return regionList;
        } else {
            return null;
        }
    }

    public List<Photo> makePhotoList(int reference_id, HttpServletRequest request, MultipartFile[] photos)
            throws Exception {
        if (photos != null) {
            List<Photo> photoList = new ArrayList<>();
            for (int i = 0; i < photos.length; i++) {
                String dir = request.getSession().getServletContext().getRealPath("/storage");
                String originname = photos[i].getOriginalFilename();
                String filename = photos[i].getOriginalFilename();
                int lastIndex = originname.lastIndexOf(".");
                String filetype = originname.substring(lastIndex + 1);
                int filesize = (int) photos[i].getSize();
                File file = new File(dir, filename);
                FileCopyUtils.copy(photos[i].getInputStream(), new FileOutputStream(file));

                Photo photo = new Photo();
                photo.setDir(dir);
                photo.setOriginname(originname);
                photo.setFilename(filename);
                photo.setFiletype(filetype);
                photo.setFilesize(filesize);
                photo.setReference_id(reference_id);

                photoList.add(photo);
            }
            return photoList;
        } else {
            return null;
        }
    }

    public String linkInsert(Link link, int kind) throws Exception {
        int linkResult = linkService.linkInsert(link, kind);
        return linkResult > 0 ? "OK" : "FAIL";
    }

    public int isNum(String string) {
        return string == null ? 0 : Integer.parseInt(string);
    }

    public String isOK(int i) {
        return i > 0 ? "OK" : "FAIL";
    }

    public ModelAndView modelAndView(JSONObject json, String viewName) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("json", json);
        modelAndView.setViewName(viewName);
        return modelAndView;
    }
}
