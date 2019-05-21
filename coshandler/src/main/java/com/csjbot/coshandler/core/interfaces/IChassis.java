package com.csjbot.coshandler.core.interfaces;

/**
 * Created by jingwc on 2017/9/20.
 */

public interface IChassis {
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
}
