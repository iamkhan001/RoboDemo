package com.csjbot.blackgaga.model.http.bean;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/1/5.
 * @Package_name: BlackGaGa
 */

public class SceneRepuestBean {
    /**
     * {
     * "sn": "12345678",
     * "version": 1
     * }
     */
    private String sn;

    private int version;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
