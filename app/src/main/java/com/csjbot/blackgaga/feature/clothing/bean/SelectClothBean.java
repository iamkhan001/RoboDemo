package com.csjbot.blackgaga.feature.clothing.bean;

/**
 * @author ShenBen
 * @date 2018/11/12 19:20
 * @email 714081644@qq.com
 */

public class SelectClothBean {
    private String type;
    private boolean isChecked = false;

    public SelectClothBean(String type) {
        this.type = type;
    }

    public SelectClothBean(String type, boolean isChecked) {
        this.type = type;
        this.isChecked = isChecked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
