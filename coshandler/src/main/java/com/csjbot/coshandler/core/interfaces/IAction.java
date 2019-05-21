package com.csjbot.coshandler.core.interfaces;

/**
 * Created by jingwc on 2017/9/5.
 */

public interface IAction {

    /**
     * 左大臂向上
     */
    void leftLargeArmUp();

    /**
     * 左大臂向下
     */
    void leftLargeArmDown();


    /**
     * 左小臂向上
     */
    void leftSmallArmUp();

    /**
     * 左小臂向下
     */
    void leftSmallArmDown();

    /**
     * 右大臂向上
     */
    void righLargeArmUp();

    /**
     * 右大臂向下
     */
    void rightLargeArmDown();


    /**
     * 右小臂向上
     */
    void rightSmallArmUp();

    /**
     * 右小臂向下
     */
    void rightSmallArmDown();

    /**
     * 双大臂向上
     */
    void doubleLargeArmUp();

    /**
     * 双大臂向下
     */
    void doubleLargeArmDown();


    /**
     * 双小臂向上
     */
    void doubleSmallArmUp();

    /**
     * 双小臂向下
     */
    void doubleSmallArmDown();

    /**
     * 摇头动作
     */
    void denyAction();

    /**
     * 点头动作
     */
    void nodAction();

    /**
     * 重置动作
     */
    void resetAction();

    /**
     * 开始摆手
     */
    void startWave(int intervalTime);

    /**
     * 停止挥手
     */
    void stopWave();

    /**
     * 小雪右臂摆动
     */
    void snowRightArm();

    /**
     * 小雪左臂摆动
     */
    void snowLeftArm();

    /**
     * 小雪双臂摆动
     */
    void snowDoubleArm();
}
