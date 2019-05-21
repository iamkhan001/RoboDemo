package com.csjbot.blackgaga.feature.clothing.bean;

import java.util.List;

/**
 * @author ShenBen
 * @date 2018/11/13 10:06
 * @email 714081644@qq.com
 */

public class ClothTypeBean {

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
         * goodsPicture : http://218.241.163.6:8085/MASTER/BOARD/PICS/LF18/LF232T605.png
         * secondLevel : T恤
         */

        private String goodsPicture;
        private String secondLevel;

        public String getGoodsPicture() {
            return goodsPicture;
        }

        public void setGoodsPicture(String goodsPicture) {
            this.goodsPicture = goodsPicture;
        }

        public String getSecondLevel() {
            return secondLevel;
        }

        public void setSecondLevel(String secondLevel) {
            this.secondLevel = secondLevel;
        }
    }
}
