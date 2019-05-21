package com.csjbot.blackgaga.bean;

import java.io.Serializable;

/**
 * 新闻
 * Created by jingwc on 2017/12/13.
 */

public class NewsBean implements Serializable {

    public static final String service = "NEWS";

    // 内容
    private String content;

    // 标题
    private String title;

    // 图片
    private String picture;

    // 链接地址
    private String url;

    public static String getService() {
        return service;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
