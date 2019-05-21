package com.csjbot.blackgaga.cart.pactivity.evaluate;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 20/11/2017.
 */

public class AnswerResponse {

    /**
     * status : 500
     * message : 添加失败
     *
     * {"message":"添加成功","status":"200"}
     */

    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
