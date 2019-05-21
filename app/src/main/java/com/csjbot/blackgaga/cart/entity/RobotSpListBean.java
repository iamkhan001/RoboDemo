package com.csjbot.blackgaga.cart.entity;

import java.util.List;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/24.
 */

public class RobotSpListBean {

    /**
     * message : ok
     * result : {"product":[{"botDataSource":"api","botFunction":"send","contentURL":" 简介","currency":"RMB","currentprice":0.01,"introduction":"好吃源于工艺好。\n采用独特工艺，历经二十多道工序而成。","isshowbtnpay":true,"isshowbtnshopcatr":true,"marketingtype":2,"name":"鸭头","originalprice":0.02,"product_id":"D9MXSBQREVGLL4W9BNOFG8UHG5DJ86EG","producttype":10,"sell":200,"serviceDataSource":"API","serviceFunction":"service","sn":"66666666","stock":23,"todoURL":"url","unit":"个","viewUrl":[]},{"botDataSource":"api","botFunction":"send","contentURL":" 简介","currency":"RMB","currentprice":0.01,"imgName":"1509778142364.jpg","introduction":"鸭舌","isshowbtnpay":true,"isshowbtnshopcatr":true,"marketingtype":2,"name":"鸭舌","originalprice":0.02,"product_id":"FGHBHE5AQWYZ0XOV7HJ611QE3S1G2T8X","producttype":10,"sell":200,"serviceDataSource":"API","serviceFunction":"service","sn":"66666666","stock":666,"todoURL":"url","unit":"个","url":"/opt/pkg/pms/1509778142364.jpg","viewType":2,"viewUrl":["/opt/pkg/pms/1509778142364.jpg"]},{"botDataSource":"api","botFunction":"send","contentURL":" 简介","currency":"RMB","currentprice":0.01,"imgName":"1509690067863.png","introduction":"美味不容错过","isshowbtnpay":true,"isshowbtnshopcatr":true,"marketingtype":2,"name":"鸭胗","originalprice":0.02,"product_id":"H621GPWPVYSCYEDLXUXX25ZA69CYGWXW","producttype":10,"sell":200,"serviceDataSource":"API","serviceFunction":"service","sn":"66666666","stock":20,"todoURL":"url","unit":"个","url":"/opt/pkg/pms/1509690067863.png","viewType":2,"viewUrl":["/opt/pkg/pms/1509690067863.png"]}]}
     * status : 200
     */

    private String message;
    private ResultBean result;
    private String status;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private String timestamp;

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
        private List<ProductBean> product;

        public List<ProductBean> getProduct() {
            return product;
        }

        public void setProduct(List<ProductBean> product) {
            this.product = product;
        }

        public static class ProductBean {
            /**
             * botDataSource : api
             * botFunction : send
             * contentURL :  简介
             * currency : RMB
             * currentprice : 0.01
             * introduction : 好吃源于工艺好。
             采用独特工艺，历经二十多道工序而成。
             * isshowbtnpay : true
             * isshowbtnshopcatr : true
             * marketingtype : 2
             * name : 鸭头
             * originalprice : 0.02
             * product_id : D9MXSBQREVGLL4W9BNOFG8UHG5DJ86EG
             * producttype : 10
             * sell : 200
             * serviceDataSource : API
             * serviceFunction : service
             * sn : 66666666
             * stock : 23
             * todoURL : url
             * unit : 个
             * viewUrl : []
             * imgName : 1509778142364.jpg
             * url : /opt/pkg/pms/1509778142364.jpg
             * viewType : 2
             */

            private String botDataSource;
            private String botFunction;
            private String contentURL;
            private String currency;
            private double currentprice;
            private String introduction;
            private boolean isshowbtnpay;
            private boolean isshowbtnshopcatr;
            private int marketingtype;
            private String name;
            private double originalprice;
            private String product_id;
            private int producttype;
            private int sell;
            private String serviceDataSource;
            private String serviceFunction;
            private String sn;
            private int stock;
            private String todoURL;
            private String unit;
            private String imgName;
            private String url;
            private int viewType;
            private List<String> viewUrl;

            public String getBotDataSource() {
                return botDataSource;
            }

            public void setBotDataSource(String botDataSource) {
                this.botDataSource = botDataSource;
            }

            public String getBotFunction() {
                return botFunction;
            }

            public void setBotFunction(String botFunction) {
                this.botFunction = botFunction;
            }

            public String getContentURL() {
                return contentURL;
            }

            public void setContentURL(String contentURL) {
                this.contentURL = contentURL;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public double getCurrentprice() {
                return currentprice;
            }

            public void setCurrentprice(double currentprice) {
                this.currentprice = currentprice;
            }

            public String getIntroduction() {
                return introduction;
            }

            public void setIntroduction(String introduction) {
                this.introduction = introduction;
            }

            public boolean isIsshowbtnpay() {
                return isshowbtnpay;
            }

            public void setIsshowbtnpay(boolean isshowbtnpay) {
                this.isshowbtnpay = isshowbtnpay;
            }

            public boolean isIsshowbtnshopcatr() {
                return isshowbtnshopcatr;
            }

            public void setIsshowbtnshopcatr(boolean isshowbtnshopcatr) {
                this.isshowbtnshopcatr = isshowbtnshopcatr;
            }

            public int getMarketingtype() {
                return marketingtype;
            }

            public void setMarketingtype(int marketingtype) {
                this.marketingtype = marketingtype;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public double getOriginalprice() {
                return originalprice;
            }

            public void setOriginalprice(double originalprice) {
                this.originalprice = originalprice;
            }

            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

            public int getProducttype() {
                return producttype;
            }

            public void setProducttype(int producttype) {
                this.producttype = producttype;
            }

            public int getSell() {
                return sell;
            }

            public void setSell(int sell) {
                this.sell = sell;
            }

            public String getServiceDataSource() {
                return serviceDataSource;
            }

            public void setServiceDataSource(String serviceDataSource) {
                this.serviceDataSource = serviceDataSource;
            }

            public String getServiceFunction() {
                return serviceFunction;
            }

            public void setServiceFunction(String serviceFunction) {
                this.serviceFunction = serviceFunction;
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public int getStock() {
                return stock;
            }

            public void setStock(int stock) {
                this.stock = stock;
            }

            public String getTodoURL() {
                return todoURL;
            }

            public void setTodoURL(String todoURL) {
                this.todoURL = todoURL;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public String getImgName() {
                return imgName;
            }

            public void setImgName(String imgName) {
                this.imgName = imgName;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getViewType() {
                return viewType;
            }

            public void setViewType(int viewType) {
                this.viewType = viewType;
            }

            public List<String> getViewUrl() {
                return viewUrl;
            }

            public void setViewUrl(List<String> viewUrl) {
                this.viewUrl = viewUrl;
            }
        }
    }
}