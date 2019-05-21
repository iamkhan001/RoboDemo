package com.csjbot.blackgaga.feature.clothing.bean;

import java.util.List;

/**
 * @author ShenBen
 * @date 2018/11/12 11:15
 * @email 714081644@qq.com
 */

public class ClothListBean {

    private String message;
    private ResultBean result;
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class ResultBean {
        private List<GoodsListBean> goodsList;

        public List<GoodsListBean> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<GoodsListBean> goodsList) {
            this.goodsList = goodsList;
        }

        public static class GoodsListBean {
            /**
             * color : A0,P1
             * createByDate : 1541862890000
             * currency : RMB
             * currentInventory :
             * deleted : 0
             * enable : 1
             * firstLevel : 服装
             * goodsCode : MAL171C0723
             * goodsName : 衬衫
             * goodsPicture : http://192.168.1.40/MASTER/BOARD/PICS/MA/MAL171C0723.jpg
             * goodsStyle : 加绒
             * id : 2552
             * language : zh
             * lockUpStock :
             * marketingType : 1
             * modifyByDate : 1541862890000
             * originalPrice : 2390
             * paymentButton : false
             * presentPrice : 717
             * quantitySold :
             * remark :
             * season : 春
             * secondLevel : 衬衫
             * shoppingTrolley : false
             * size : 810
             * totalStock :
             * unit : 件
             * year : 2017
             */

            private String color;
            private long createByDate;
            private String currency;
            private String currentInventory;
            private int deleted;
            private int enable;
            private String firstLevel;
            private String goodsCode;
            private String goodsName;
            private String goodsPicture;
            private String goodsStyle;
            private int id;
            private String language;
            private String lockUpStock;
            private String marketingType;
            private long modifyByDate;
            private String originalPrice;
            private String paymentButton;
            private String presentPrice;
            private String quantitySold;
            private String remark;
            private String season;
            private String secondLevel;
            private String shoppingTrolley;
            private String size;
            private String totalStock;
            private String unit;
            private String year;

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public long getCreateByDate() {
                return createByDate;
            }

            public void setCreateByDate(long createByDate) {
                this.createByDate = createByDate;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getCurrentInventory() {
                return currentInventory;
            }

            public void setCurrentInventory(String currentInventory) {
                this.currentInventory = currentInventory;
            }

            public int getDeleted() {
                return deleted;
            }

            public void setDeleted(int deleted) {
                this.deleted = deleted;
            }

            public int getEnable() {
                return enable;
            }

            public void setEnable(int enable) {
                this.enable = enable;
            }

            public String getFirstLevel() {
                return firstLevel;
            }

            public void setFirstLevel(String firstLevel) {
                this.firstLevel = firstLevel;
            }

            public String getGoodsCode() {
                return goodsCode;
            }

            public void setGoodsCode(String goodsCode) {
                this.goodsCode = goodsCode;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getGoodsPicture() {
                return goodsPicture;
            }

            public void setGoodsPicture(String goodsPicture) {
                this.goodsPicture = goodsPicture;
            }

            public String getGoodsStyle() {
                return goodsStyle;
            }

            public void setGoodsStyle(String goodsStyle) {
                this.goodsStyle = goodsStyle;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }

            public String getLockUpStock() {
                return lockUpStock;
            }

            public void setLockUpStock(String lockUpStock) {
                this.lockUpStock = lockUpStock;
            }

            public String getMarketingType() {
                return marketingType;
            }

            public void setMarketingType(String marketingType) {
                this.marketingType = marketingType;
            }

            public long getModifyByDate() {
                return modifyByDate;
            }

            public void setModifyByDate(long modifyByDate) {
                this.modifyByDate = modifyByDate;
            }

            public String getOriginalPrice() {
                return originalPrice;
            }

            public void setOriginalPrice(String originalPrice) {
                this.originalPrice = originalPrice;
            }

            public String getPaymentButton() {
                return paymentButton;
            }

            public void setPaymentButton(String paymentButton) {
                this.paymentButton = paymentButton;
            }

            public String getPresentPrice() {
                return presentPrice;
            }

            public void setPresentPrice(String presentPrice) {
                this.presentPrice = presentPrice;
            }

            public String getQuantitySold() {
                return quantitySold;
            }

            public void setQuantitySold(String quantitySold) {
                this.quantitySold = quantitySold;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getSeason() {
                return season;
            }

            public void setSeason(String season) {
                this.season = season;
            }

            public String getSecondLevel() {
                return secondLevel;
            }

            public void setSecondLevel(String secondLevel) {
                this.secondLevel = secondLevel;
            }

            public String getShoppingTrolley() {
                return shoppingTrolley;
            }

            public void setShoppingTrolley(String shoppingTrolley) {
                this.shoppingTrolley = shoppingTrolley;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getTotalStock() {
                return totalStock;
            }

            public void setTotalStock(String totalStock) {
                this.totalStock = totalStock;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }
        }
    }
}
