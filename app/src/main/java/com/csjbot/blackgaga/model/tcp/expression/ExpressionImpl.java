package com.csjbot.blackgaga.model.tcp.expression;

import com.csjbot.blackgaga.model.tcp.base.BaseImpl;

/**
 * Created by jingwc on 2017/9/21.
 */

public class ExpressionImpl extends BaseImpl implements IExpression {

    @Override
    public void getExpression() {
        robotManager.robot.reqProxy.getExpression();
    }

    @Override
    public void happy() {
        robotManager.robot.happy();
    }

    @Override
    public void sadness() {
        robotManager.robot.sadness();
    }

    @Override
    public void surprised() {
        robotManager.robot.surprised();
    }

    @Override
    public void smile() {
        robotManager.robot.smile();
    }

    @Override
    public void normal() {
        robotManager.robot.normal();
    }

    @Override
    public void angry() {
        robotManager.robot.angry();
    }

    @Override
    public void updateExpression() {
        robotManager.robot.reqProxy.updateExpression();
    }

    @Override
    public void lightning() {
        robotManager.robot.lightning();
    }

    @Override
    public void sleepiness() {
        robotManager.robot.sleepiness();
    }
}
