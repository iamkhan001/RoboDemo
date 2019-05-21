package com.csjbot.coshandler.client_req.expression;

import com.csjbot.coshandler.client_req.base.BaseClientReq;
import com.csjbot.coshandler.global.REQConstants;

/**
 * Created by jingwc on 2017/9/9.
 */

public class ExpressionReqImpl extends BaseClientReq implements IExpressionReq {

    @Override
    public void setExpression(int expression,int once,int time) {
        sendReq(getExpressionJson(REQConstants.SET_ROBOT_EXPRESSION_REQ,expression,once,time));
    }

    @Override
    public void getExpression() {
        sendReq(getJson(REQConstants.GET_ROBOT_EXPRESSION_REQ));
    }

    @Override
    public void updateExpression() {
        sendReq(getJson(REQConstants.UPGRADE_ROBOT_EXPRESSION_REQ));
    }
}
