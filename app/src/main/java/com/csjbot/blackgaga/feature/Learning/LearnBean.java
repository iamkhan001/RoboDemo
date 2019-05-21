package com.csjbot.blackgaga.feature.Learning;

import android.graphics.drawable.Drawable;

/**
 * Created by  Wql , 2018/3/7 12:00
 */
public class LearnBean {
    public String pakageName;
    public String appName;
    public Drawable icon;
    public String activityName;

    public LearnBean() {
    }

    public LearnBean(String pakageName, String appName, Drawable icon, String activityName) {
        this.pakageName = pakageName;
        this.appName = appName;
        this.icon = icon;
        this.activityName = activityName;
    }

    public String getPakageName() {
        return pakageName;
    }

    public void setPakageName(String pakageName) {
        this.pakageName = pakageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
