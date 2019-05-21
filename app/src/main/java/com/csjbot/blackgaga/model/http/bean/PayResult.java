package com.csjbot.blackgaga.model.http.bean;


public class PayResult {
    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";
//    // 订单号
//    private String orderId;
//    // 顾客下单时间
//    private String orderTime;
//    // 订单标价总金额
//    private String orderTotalFee;
//    // 实际现金支付金额
//    private String payCashFee;
//    // 实际代金券支付金额
//    private String payCouponFee;
//    // 实际退款金额
//    private int payRefundFee;
//    // 支付状态。可能值：等待支付`WAIT`,支付完成`SUCCESS`,支付失败`FAIL`,支付取消`CANCEL`
//    private String payStatus;
//    // 支付确认时间
//    private String payTimeEnd;
//    // 支付失效时间
//    private String payTimeExpire;
//    // 支付发起时间
//    private String payTimeStart;
//    private String remark;
//    // 支付者openid
//    private String wxOpenId;
//    // 支付交易单号
//    private String wxTransactionId;

    /**
     * orderId : 1502671-8638-000-001-986
     * orderTime : 2017-04-05 17:51:41.0
     * orderTotalFee : 0.01
     * payCashFee : 0.01
     * payCouponFee : 0.00
     * payRefundFee : 0
     * payStatus : SUCCESS
     * payTimeEnd : 2017-08-14 08:53:54
     * payTimeExpire :
     * payTimeStart : 2017-04-05 17:51:41.0
     * remark :
     * wxOpenId : 2088512335998889
     * wxTransactionId : 2017081421001004880218222428
     */

    private String orderId;
    private String orderTime;
    private String orderTotalFee;
    private String payCashFee;
    private String payCouponFee;
    private int payRefundFee;
    private String payStatus;
    private String payTimeEnd;
    private String payTimeExpire;
    private String payTimeStart;
    private String remark;
    private String wxOpenId;
    private String wxTransactionId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderTotalFee() {
        return orderTotalFee;
    }

    public void setOrderTotalFee(String orderTotalFee) {
        this.orderTotalFee = orderTotalFee;
    }

    public String getPayCashFee() {
        return payCashFee;
    }

    public void setPayCashFee(String payCashFee) {
        this.payCashFee = payCashFee;
    }

    public String getPayCouponFee() {
        return payCouponFee;
    }

    public void setPayCouponFee(String payCouponFee) {
        this.payCouponFee = payCouponFee;
    }

    public int getPayRefundFee() {
        return payRefundFee;
    }

    public void setPayRefundFee(int payRefundFee) {
        this.payRefundFee = payRefundFee;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayTimeEnd() {
        return payTimeEnd;
    }

    public void setPayTimeEnd(String payTimeEnd) {
        this.payTimeEnd = payTimeEnd;
    }

    public String getPayTimeExpire() {
        return payTimeExpire;
    }

    public void setPayTimeExpire(String payTimeExpire) {
        this.payTimeExpire = payTimeExpire;
    }

    public String getPayTimeStart() {
        return payTimeStart;
    }

    public void setPayTimeStart(String payTimeStart) {
        this.payTimeStart = payTimeStart;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getWxTransactionId() {
        return wxTransactionId;
    }

    public void setWxTransactionId(String wxTransactionId) {
        this.wxTransactionId = wxTransactionId;
    }
}
