package com.csjbot.blackgaga.model.http.bean;

import java.util.List;

/**
 * Created by  Wql , 2018/3/1 17:55
 */
public class QrCodeBean {


    /**
     * message : ok
     * result : {"companyName":"2223","modelShown":{},"qrcodes":[{"desc":"2223","imageUrl":"http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/erweima/3bGafiiyY2rTrnHhPn5E.jpg","index":2},{"desc":"2525","imageUrl":"http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/erweima/JmdrK7EebxzDYyTFA3Tb.png","index":1}]}
     * status : 200
     */

    private String message;
    private ResultEntity result;
    private String status;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public ResultEntity getResult() {
        return result;
    }

    public String getStatus() {
        return status;
    }

    public static class ResultEntity {
        /**
         * companyName : 2223
         * modelShown : {}
         * qrcodes : [{"desc":"2223","imageUrl":"http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/erweima/3bGafiiyY2rTrnHhPn5E.jpg","index":2},{"desc":"2525","imageUrl":"http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/erweima/JmdrK7EebxzDYyTFA3Tb.png","index":1}]
         */

        private String companyName;
        private List<QrcodesEntity> qrcodes;

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public void setQrcodes(List<QrcodesEntity> qrcodes) {
            this.qrcodes = qrcodes;
        }

        public String getCompanyName() {
            return companyName;
        }

        public List<QrcodesEntity> getQrcodes() {
            return qrcodes;
        }

        public static class QrcodesEntity {
            /**
             * desc : 2223
             * imageUrl : http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/erweima/3bGafiiyY2rTrnHhPn5E.jpg
             * index : 2
             */

            private String imageUrl;
            private int index;
            private String desc;

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public String getDesc() {
                return desc;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public int getIndex() {
                return index;
            }
        }
    }
}
