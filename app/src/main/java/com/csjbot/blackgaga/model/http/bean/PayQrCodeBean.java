package com.csjbot.blackgaga.model.http.bean;

import java.util.List;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/5/30.
 * @Package_name: BlackGaGa
 */
public class PayQrCodeBean {

    /**
     * message : ok
     * result : {"companyName":"微信收款","qrcodes":[{"desc":"微信收款码","imageUrl":"http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/erweima/BSiaiN8YsGFZ8wrjhCHb.png","index":0},{"desc":"支付宝收款码","imageUrl":"http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/erweima/pbWchxyez6GESTQKMQie.png","index":2}]}
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
         * companyName : 微信收款
         * qrcodes : [{"desc":"微信收款码","imageUrl":"http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/erweima/BSiaiN8YsGFZ8wrjhCHb.png","index":0},{"desc":"支付宝收款码","imageUrl":"http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/erweima/pbWchxyez6GESTQKMQie.png","index":2}]
         */

        private String companyName;
        private List<QrcodesBean> qrcodes;

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public List<QrcodesBean> getQrcodes() {
            return qrcodes;
        }

        public void setQrcodes(List<QrcodesBean> qrcodes) {
            this.qrcodes = qrcodes;
        }

        public static class QrcodesBean {
            /**
             * desc : 微信收款码
             * imageUrl : http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/erweima/BSiaiN8YsGFZ8wrjhCHb.png
             * index : 0
             */

            private String desc;
            private String imageUrl;
            private int index;

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }
        }
    }
}
