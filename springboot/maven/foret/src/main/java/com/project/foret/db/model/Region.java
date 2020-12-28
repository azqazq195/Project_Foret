package com.project.foret.db.model;

import org.apache.ibatis.type.Alias;

@Alias("region")
public class Region {
    private int id;
    private String region_si;
    private String region_gu;

    public Region(int id, String region_si, String region_gu) {
        this.id = id;
        this.region_si = region_si;
        this.region_gu = region_gu;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegion_si() {
        return this.region_si;
    }

    public void setRegion_si(String region_si) {
        this.region_si = region_si;
    }

    public String getRegion_gu() {
        return this.region_gu;
    }

    public void setRegion_gu(String region_gu) {
        this.region_gu = region_gu;
    }

}
