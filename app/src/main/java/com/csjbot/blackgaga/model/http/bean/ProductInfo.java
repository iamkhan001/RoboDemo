package com.csjbot.blackgaga.model.http.bean;

import java.util.List;

public class ProductInfo {

    /**
     * message : ok
     * result : {"product":[{"imgName":"1501224598625.png","introduction":"好吃源于工艺好。\r\n采用独特工艺，历经二十多道工序而成。","money":0.01,"name":"鸭头","product_id":"D9MXSBQREVGLL4W9BNOFG8UHG5DJ86EG","unit":"个","url":"120.27.233.57:8001/pms/1501224598625.png"},{"imgName":"1501225161938.png","introduction":"精心卤制，快乐体验，精选酱料，分外挑剔，秘制酱心，唇齿留香。","money":0.01,"name":"鸭翅膀","product_id":"DJUAFE642BYSN6ZIHCX74GC3P9U6TYGT","unit":"个","url":"120.27.233.57:8001/pms/1501225161938.png"},{"imgName":"1501224439695.png","introduction":"好吃源于口感好。\r\n刚入口时有点甜，而后越吃越辣，越吃越麻，越吃越香，停不了口！","money":0.01,"name":"鸭锁骨","product_id":"LJ6CE6XT7UGC3V0FSPD7OHDWODZRAHVE","unit":"个","url":"120.27.233.57:8001/pms/1501224439695.png"},{"imgName":"1501224706488.png","introduction":"好吃源于配方好。\r\n甄选数十种香辛料，采用独特工艺卤制而成，醇香四溢。","money":0.01,"name":"鸭脖子","product_id":"YH8F80ZEIU21SUG5YCXR7RF05RKSRCNH","unit":"个","url":"120.27.233.57:8001/pms/1501224706488.png"}],"zipName":"Mj21.zip","zipUrl":"120.27.233.57:8001/zip/Mj21.zip"}
     * status : 200
     */

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
        /**
         * product : [{"imgName":"1501224598625.png","introduction":"好吃源于工艺好。\r\n采用独特工艺，历经二十多道工序而成。","money":0.01,"name":"鸭头","product_id":"D9MXSBQREVGLL4W9BNOFG8UHG5DJ86EG","unit":"个","url":"120.27.233.57:8001/pms/1501224598625.png"},{"imgName":"1501225161938.png","introduction":"精心卤制，快乐体验，精选酱料，分外挑剔，秘制酱心，唇齿留香。","money":0.01,"name":"鸭翅膀","product_id":"DJUAFE642BYSN6ZIHCX74GC3P9U6TYGT","unit":"个","url":"120.27.233.57:8001/pms/1501225161938.png"},{"imgName":"1501224439695.png","introduction":"好吃源于口感好。\r\n刚入口时有点甜，而后越吃越辣，越吃越麻，越吃越香，停不了口！","money":0.01,"name":"鸭锁骨","product_id":"LJ6CE6XT7UGC3V0FSPD7OHDWODZRAHVE","unit":"个","url":"120.27.233.57:8001/pms/1501224439695.png"},{"imgName":"1501224706488.png","introduction":"好吃源于配方好。\r\n甄选数十种香辛料，采用独特工艺卤制而成，醇香四溢。","money":0.01,"name":"鸭脖子","product_id":"YH8F80ZEIU21SUG5YCXR7RF05RKSRCNH","unit":"个","url":"120.27.233.57:8001/pms/1501224706488.png"}]
         * zipName : Mj21.zip
         * zipUrl : 120.27.233.57:8001/zip/Mj21.zip
         */

        private String zipName;
        private String zipUrl;
        private List<ProductBean> product;

        public String getZipName() {
            return zipName;
        }

        public void setZipName(String zipName) {
            this.zipName = zipName;
        }

        public String getZipUrl() {
            return zipUrl;
        }

        public void setZipUrl(String zipUrl) {
            this.zipUrl = zipUrl;
        }

        public List<ProductBean> getProduct() {
            return product;
        }

        public void setProduct(List<ProductBean> product) {
            this.product = product;
        }

        public static class ProductBean {
            /**
             * imgName : 1501224598625.png
             * introduction : 好吃源于工艺好。
             * 采用独特工艺，历经二十多道工序而成。
             * money : 0.01
             * name : 鸭头
             * product_id : D9MXSBQREVGLL4W9BNOFG8UHG5DJ86EG
             * unit : 个
             * url : 120.27.233.57:8001/pms/1501224598625.png
             */

            private String imgName;
            private String introduction;
            private double money;
            private String name;
            private String product_id;
            private String unit;
            private String url;

            public String getImgName() {
                return imgName;
            }

            public void setImgName(String imgName) {
                this.imgName = imgName;
            }

            public String getIntroduction() {
                return introduction;
            }

            public void setIntroduction(String introduction) {
                this.introduction = introduction;
            }

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof ProductBean)) {
                    return false;
                }

                if(product_id.equals(((ProductBean) obj).product_id)){
                    return true;
                }
                return false;
            }
        }
    }
}
