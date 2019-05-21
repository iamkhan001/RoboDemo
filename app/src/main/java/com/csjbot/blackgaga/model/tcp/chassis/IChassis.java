package com.csjbot.blackgaga.model.tcp.chassis;


/**
 * Created by jingwc on 2017/9/20.
 */

public interface IChassis {
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
     * 回到充电点
     */
    void goHome();

    /**
     * 向左转
     */
    void turnLeft();

    /**
     * 向右转
     */
    void turnRight();


    void moveLeft();

    void moveRight();

    void moveForward();

    void moveBack();

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
