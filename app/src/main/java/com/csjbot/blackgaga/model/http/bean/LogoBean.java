package com.csjbot.blackgaga.model.http.bean;

import java.util.List;

/**
 * Created by xiasuhuei321 on 2017/10/31.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class LogoBean {
    // 使用网络 logo
    public static final int TYPE_NET = 111;
    // 使用本地 logo
    public static final int TYPE_LOCAL = 222;
    /**
     * message : ok
     * result : [{"sn":"123456789","logourl":"120.27.233.57:8001/ams/1493714541691.jpg","logotitle":"武汉周黑鸭食品有限公司","isenable":"1","isdefault":"0"}]
     * state : 200
     */

    private String message;
    private String status;
    private List<ResultBean> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * sn : 040016160001
         * logourl : http%3A%2F%2Fcsj-robot-test.oss-cn-shanghai.aliyuncs.com%2Flogo%2FpnmWP4ZCR22sBCkPZxC6.jpg
         * logotitle : 真维斯
         * isenable : 1
         * isdefault : 1
         */

        private String sn;
        private String logourl;
        private String logotitle;
        private int isenable;
        private int isdefault;

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getLogourl() {
            return logourl;
        }

        public void setLogourl(String logourl) {
            this.logourl = logourl;
        }

        public String getLogotitle() {
            return logotitle;
        }

        public void setLogotitle(String logotitle) {
            this.logotitle = logotitle;
        }

        public int getIsenable() {
            return isenable;
        }

        public void setIsenable(int isenable) {
            this.isenable = isenable;
        }

        public int getIsdefault() {
            return isdefault;
        }

        public void setIsdefault(int isdefault) {
            this.isdefault = isdefault;
        }
    }


//    /**
//     * message : ok
//     * result : [{"sn":"123456789","logourl":"120.27.233.57:8001/ams/1493714541691.jpg","logotitle":"武汉周黑鸭食品有限公司","isenable":"1","isdefault":"0"}]
//     * state : 200
//     */
//
//    private String message;
//    private String state;
//    private List<ResultBean> result;
//
//    private int type = TYPE_NET;
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public String getState() {
//        return state;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }
//
//    public List<ResultBean> getResult() {
//        return result;
//    }
//
//    public void setResult(List<ResultBean> result) {
//        this.result = result;
//    }
//
//    public int getType() {
//        if (result == null) return TYPE_LOCAL;
//        if (result.get(0).isenable == 1) {
//            return TYPE_NET;
//        }
//
//        return TYPE_LOCAL;
//    }
//
//    public static class ResultBean {
//        /**
//         * sn : 123456789
//         * logourl : 120.27.233.57:8001/ams/1493714541691.jpg
//         * logotitle : 武汉周黑鸭食品有限公司
//         * isenable : 1
//         * isdefault : 0
//         */
//
//        // 机器人 sn 号
//        private String sn;
//        // logo url 地址
//        private String logourl;
//        // logo 标题名称
//        private String logotitle;
//        // 是否启用，1表示启用，0表示不启用
//        private int isenable;
//        // 是否默认，1表示默认，0表示不默认（什么鬼，和上面的没冲突？）
//        private int isdefault;
//
//        public String getSn() {
//            return sn;
//        }
//
//        public void setSn(String sn) {
//            this.sn = sn;
//        }
//
//        public String getLogourl() {
//            return logourl;
//        }
//
//        public void setLogourl(String logourl) {
//            this.logourl = logourl;
//        }
//
//        public String getLogotitle() {
//            return logotitle;
//        }
//
//        public void setLogotitle(String logotitle) {
//            this.logotitle = logotitle;
//        }
//
//        public int getIsenable() {
//            return isenable;
//        }
//
//        public void setIsenable(int isenable) {
//            this.isenable = isenable;
//        }
//
//        public int getIsdefault() {
//            return isdefault;
//        }
//
//        public void setIsdefault(int isdefault) {
//            this.isdefault = isdefault;
//        }
//    }
}
