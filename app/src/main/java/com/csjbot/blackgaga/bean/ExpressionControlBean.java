package com.csjbot.blackgaga.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/6/27.
 */

public class ExpressionControlBean implements Serializable{

    private List<String> happy;
    private List<String> normal;
    private List<String> smile;
    private List<String> sad;
    private List<String> angry;
    private List<String> surprise;

    public List<String> getHappy() {
        return happy;
    }

    public void setHappy(List<String> happy) {
        this.happy = happy;
    }

    public List<String> getNormal() {
        return normal;
    }

    public void setNormal(List<String> normal) {
        this.normal = normal;
    }

    public List<String> getSmile() {
        return smile;
    }

    public void setSmile(List<String> smile) {
        this.smile = smile;
    }

    public List<String> getSad() {
        return sad;
    }

    public void setSad(List<String> sad) {
        this.sad = sad;
    }

    public List<String> getAngry() {
        return angry;
    }

    public void setAngry(List<String> angry) {
        this.angry = angry;
    }

    public List<String> getSurprise() {
        return surprise;
    }

    public void setSurprise(List<String> surprise) {
        this.surprise = surprise;
    }
}
