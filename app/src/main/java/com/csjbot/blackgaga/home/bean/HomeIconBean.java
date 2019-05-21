package com.csjbot.blackgaga.home.bean;

/**
 * @author ShenBen
 * @date 2018/11/12 16:41
 * @email 714081644@qq.com
 */

public class HomeIconBean {
    private String title;
    private int icon;

    public HomeIconBean(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
