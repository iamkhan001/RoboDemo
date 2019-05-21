package com.csjbot.blackgaga.bean;

import java.io.Serializable;

/**
 * Created by jingwc on 2018/3/29.
 */

public class SalesBean implements Serializable {

    /**
     * name :
     * activityImg : url
     * detailImg : url
     * locationImg : url
     */

    private String name;
    private String activityImg;
    private String detailImg;
    private String locationImg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivityImg() {
        return activityImg;
    }

    public void setActivityImg(String activityImg) {
        this.activityImg = activityImg;
    }

    public String getDetailImg() {
        return detailImg;
    }

    public void setDetailImg(String detailImg) {
        this.detailImg = detailImg;
    }

    public String getLocationImg() {
        return locationImg;
    }

    public void setLocationImg(String locationImg) {
        this.locationImg = locationImg;
    }
}
