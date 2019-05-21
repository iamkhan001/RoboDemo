package com.csjbot.blackgaga.feature.settings.change_skin;

/**
 * Created by 孙秀艳 on 2017/12/19.
 */

public class SkinBean {
    String skinName;//皮肤名称 比如官方红
    int drawableId;//换肤主题图片
    String skinPackage;//皮肤包名
    boolean isChecked;//该套换肤主题是否被选中

    public String getSkinName() {
        return skinName;
    }

    public void setSkinName(String skinName) {
        this.skinName = skinName;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getSkinPackage() {
        return skinPackage;
    }

    public void setSkinPackage(String skinPackage) {
        this.skinPackage = skinPackage;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
