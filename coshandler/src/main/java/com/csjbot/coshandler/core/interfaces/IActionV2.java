package com.csjbot.coshandler.core.interfaces;

import android.support.annotation.IntRange;


public interface IActionV2 {
    /**
     * 爱丽丝头向上运动
     */
    void AliceHeadUp();

    /**
     * 爱丽丝头向下运动
     */
    void AliceHeadDown();


    /**
     * 爱丽丝头向左运动
     */
    void AliceHeadLeft();

    /**
     * 爱丽丝头向右运动
     */
    void AliceHeadRight();


    /**
     * 爱丽丝头水平重置
     */
    void AliceHeadHReset();

    /**
     * 爱丽丝左臂抬起
     */
    void AliceLeftArmUp();

    /**
     * 爱丽丝左臂放下
     */
    void AliceLeftArmDown();

    /**
     * 爱丽丝右臂抬起
     */
    void AliceRightArmUp();

    /**
     * 爱丽丝右臂放下
     */
    void AliceRightArmDown();

    /**
     * 小雪左臂挥动
     *
     * @param count 次数
     */
    void SnowLeftArmSwing(@IntRange(from = 0, to = 20) int count);

    /**
     * 小雪右臂挥动
     *
     * @param count 次数
     */
    void SnowRightArmSwing(@IntRange(from = 0, to = 20) int count);

    /**
     * 小雪双臂挥动
     *
     * @param count 次数
     */
    void SnowDoubleArmSwing(@IntRange(from = 0, to = 20) int count);

}
