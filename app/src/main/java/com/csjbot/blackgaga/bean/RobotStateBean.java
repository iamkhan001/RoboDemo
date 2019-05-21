package com.csjbot.blackgaga.bean;

import java.io.Serializable;

/**
 * Created by jingwc on 2018/4/12.
 */

public class RobotStateBean implements Serializable{

    /**
     * type : mainboard
     * model : sw2315
     * serialnumber : 201800021001
     * state : ok
     * firmwareversion : 510
     */

    private String type;
    private String model;
    private String serialnumber;
    private String state;
    private String firmwareversion;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFirmwareversion() {
        return firmwareversion;
    }

    public void setFirmwareversion(String firmwareversion) {
        this.firmwareversion = firmwareversion;
    }
}
