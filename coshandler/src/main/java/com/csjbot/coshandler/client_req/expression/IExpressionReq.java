package com.csjbot.coshandler.client_req.expression;


import com.csjbot.coshandler.client_req.base.IClientReq;

/**
 * 表情接口
 * Created by jingwc on 2017/8/14.
 */

public interface IExpressionReq extends IClientReq {

    void setExpression(int expression,int once,int time);

    void getExpression();

    void updateExpression();
}
