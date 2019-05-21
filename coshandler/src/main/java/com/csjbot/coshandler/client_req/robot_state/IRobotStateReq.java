package com.csjbot.coshandler.client_req.robot_state;

/**
 * Created by jingwc on 2017/10/25.
 */

public interface IRobotStateReq {

    /**
     * 机器人关机
     */
    void shutdown();

    /**
     * 机器人重启
     */
    void reboot();

    /**
     * 获取电量
     */
    void getBattery();

    /**
     * 自检
     */
    void checkSelf();

    /**
     * 获取硬件版本
     */
    void getRobotHWVersion();


    /**
     * 获取Linux储存的机器人硬件类型，默认是迎宾
     */
    void getLinuxRobotType();

    /**
     * 设置Linux储存的机器人硬件类型，默认是迎宾
     */
    void setLinuxRobotType(String type);
}
