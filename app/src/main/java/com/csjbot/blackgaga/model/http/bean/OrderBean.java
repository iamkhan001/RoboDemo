package com.csjbot.blackgaga.model.http.bean;

/**
 * Created by 孙秀艳 on 2017/10/17.
 * 订单商品Bean
 */

public class OrderBean {
    private String product_id;//产品ID
    private String name;//产品名称
    private String introduction;//产品简介
    private double currentprice;//产品单价
    private String currency;//产品单价单位
    private int sell;//订单产品数量
    private String imgName;//图片名称
    private int stock;//库存剩余量
    private boolean isChecked;//是否选中

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public double getCurrentprice() {
        return currentprice;
    }

    public void setCurrentprice(double currentprice) {
        this.currentprice = currentprice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getSell() {
        return sell;
    }

    public void setSell(int sell) {
        this.sell = sell;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


}
