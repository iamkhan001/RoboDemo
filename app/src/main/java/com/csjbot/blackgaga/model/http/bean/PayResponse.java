package com.csjbot.blackgaga.model.http.bean;

public class PayResponse {
    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";
    // id 机器人端生成id
    private String id;
    // 后台生成的正式订单流水号、全局唯一、成功时必填
    private String orderId;
    // 返回设备请求中上传的虚拟订单号
    private String orderPseudoNo;
    // 下单请求结果，只可能为成功`SUCCESS`或者失败`FAIL`
    private String orderStatus;
    private int code;
    // 扫码支付时用于生成二维码的链接
    private String codeUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderPseudoNo() {
        return orderPseudoNo;
    }

    public void setOrderPseudoNo(String orderPseudoNo) {
        this.orderPseudoNo = orderPseudoNo;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }
}
