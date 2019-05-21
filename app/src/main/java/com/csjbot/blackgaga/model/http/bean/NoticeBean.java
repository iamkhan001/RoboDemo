package com.csjbot.blackgaga.model.http.bean;

/**
 * Created by jingwc on 2018/3/10.
 */

public class NoticeBean {

    /**
     * message : ok
     * result : {"text":"今天天气晴朗","showTime":123124124,"enabled":false,"startTime":"2018-03-09 17:30:00","endTime":"2018-03-09 18:35:00"}
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
         * text : 今天天气晴朗
         * showTime : 123124124
         * enabled : false
         * startTime : 2018-03-09 17:30:00
         * endTime : 2018-03-09 18:35:00
         */

        private String text;
        private int showTime;
        private boolean enabled;
        private String startTime;
        private String endTime;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getShowTime() {
            return showTime;
        }

        public void setShowTime(int showTime) {
            this.showTime = showTime;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }
}
