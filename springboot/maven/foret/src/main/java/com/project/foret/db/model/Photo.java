package com.project.foret.db.model;

import org.apache.ibatis.type.Alias;

@Alias("photo")
public class Photo {
    private int id;
    private int reference_id;
    private String dir;
    private String filename;
    private String originname;
    private int filesize;
    private String filetype;
    private String reg_date;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReference_id() {
        return this.reference_id;
    }

    public void setReference_id(int reference_id) {
        this.reference_id = reference_id;
    }

    public String getDir() {
        return this.dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getOriginname() {
        return this.originname;
    }

    public void setOriginname(String originname) {
        this.originname = originname;
    }

    public int getFilesize() {
        return this.filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public String getFiletype() {
        return this.filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getReg_date() {
        return this.reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

}
