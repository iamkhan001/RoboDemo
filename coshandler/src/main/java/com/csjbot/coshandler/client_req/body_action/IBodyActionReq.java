package com.csjbot.coshandler.client_req.body_action;


import com.csjbot.coshandler.client_req.base.IClientReq;

/**
 * 肢体动作接口
 * Created by jingwc on 2017/8/14.
 */

public interface IBodyActionReq extends IClientReq {

    /**
     * 重置
     */
    void reset();

    /**
     * 肢体动作
     * @param bodyPart 肢体部位
     * @param action 动作
     */
    void action(int bodyPart, int action);

    /**
     * 机器人开始左右摆手
     */
    void startWaveHands(int intervalTime);

    /**
     * 机器人停止左右摆手
     */
    void stopWaveHands();

    /**
     * 开始跳舞
     */
    void startDance();

    /**
     * 停止跳舞
     */
    void stopDance();
}
