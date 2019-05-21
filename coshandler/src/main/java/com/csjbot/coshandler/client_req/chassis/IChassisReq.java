package com.csjbot.coshandler.client_req.chassis;


/**
 * Created by jingwc on 2017/9/20.
 */

public interface IChassisReq extends IStatusSearch {
    /**
     * 获取当前位置
     */
    void getPosition();

    /**
     * 移动
     * @param direction
     * direction:方向
     *  0 前
        1 后
        2 左
        3 右
     */
    void move(int direction);

    /**
     * 导航
     */
    void navi(String json);

    /**
     * 取消导航
     */
    void cancelNavi();

    /**
     * 转至特定角度
     * @param rotation 角度
     */
    void goAngle(int rotation);

    /**
     * 步进角度
     * @param rotation
     *  请求参数说明
        Rotation>0:向左转，Rotation<0:向右转
     */
    void moveAngle(int rotation);

    /**
     * 回到充电点
     */
    void goHome();

    /**
     * 存储地图
     */
    void saveMap();

    /**
     * 加载地图
     */
    void loadMap();

    /**
     * 速度设置
     * @param speed
     */
    void setSpeed(float speed);
}
