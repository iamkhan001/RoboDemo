package com.csjbot.blackgaga.model.http.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/3/21.
 * @Package_name: RobotAmyApplication
 */

public class AdvBean implements Serializable {

    private static final long serialVersionUID = -3587214520952093890L;
    /**
     * message : ok
     * result : [{"advname":"张碧晨&杨宗纬-凉凉-(电视剧《三生三世","advtype":"picture","advurl":"http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/entertaininfo/Q47btRwhQmFaRZ3Z22Qt.JPG"},{"advname":"1","advtype":"video","advurl":"http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/entertaininfo/ynTHAcRRXpBce5y3tDhw.mp4"}]
     * status : 200
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

    public static class ResultBean implements Serializable{
        private static final long serialVersionUID = -3989243743949329678L;
        /**
         * advname : 张碧晨&杨宗纬-凉凉-(电视剧《三生三世
         * advtype : picture
         * advurl : http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/entertaininfo/Q47btRwhQmFaRZ3Z22Qt.JPG
         */

        private String advname;
        private String advtype;
        private String advurl;

        public String getAdvname() {
            return advname;
        }

        public void setAdvname(String advname) {
            this.advname = advname;
        }

        public String getAdvtype() {
            return advtype;
        }

        public void setAdvtype(String advtype) {
            this.advtype = advtype;
        }

        public String getAdvurl() {
            return advurl;
        }

        public void setAdvurl(String advurl) {
            this.advurl = advurl;
        }
    }
}
