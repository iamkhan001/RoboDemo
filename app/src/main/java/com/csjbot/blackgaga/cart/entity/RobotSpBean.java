package com.csjbot.blackgaga.cart.entity;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/11/24.
 */

public class RobotSpBean {
    private RobotSpListBean bean;

    public RobotSpBean(RobotSpListBean bean, String menuID) {
        this.bean = bean;
        this.menuID = menuID;
    }

    public RobotSpListBean getBean() {
        return bean;
    }

    public void setBean(RobotSpListBean bean) {
        this.bean = bean;
    }

    public String getMenuID() {
        return menuID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }

    private String menuID;
}
