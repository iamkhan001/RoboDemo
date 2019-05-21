package com.csjbot.blackgaga.model.tcp.chassis;

import com.csjbot.blackgaga.model.tcp.base.BaseImpl;

/**
 * Created by jingwc on 2017/9/21.
 */

public class ChassisImpl extends BaseImpl implements IChassis {

    @Override
    public void getPosition() {
        robotManager.robot.reqProxy.getPosition();
    }

    @Override
    public void move(int direction) {
        robotManager.robot.reqProxy.move(direction);
    }

    @Override
    public void navi(String json) {
        robotManager.robot.reqProxy.navi(json);
    }

    @Override
    public void cancelNavi() {
        robotManager.robot.reqProxy.cancelNavi();
    }

    @Override
    public void goAngle(int rotation) {
        robotManager.robot.reqProxy.goAngle(rotation);
    }

    @Override
    public void goHome() {
        robotManager.robot.reqProxy.goHome();
    }

    @Override
    public void turnLeft() {
        robotManager.robot.turnLeft();
    }

    @Override
    public void turnRight() {
        robotManager.robot.turnRight();
    }

    @Override
    public void moveLeft() {
        robotManager.robot.moveLeft();
    }

    @Override
    public void moveRight() {
        robotManager.robot.moveRight();
    }

    @Override
    public void moveForward() {
        robotManager.robot.moveForward();
    }

    @Override
    public void moveBack() {
        robotManager.robot.moveBack();
    }

    @Override
    public void saveMap() {
        robotManager.robot.reqProxy.saveMap();
    }

    @Override
    public void loadMap() {
        robotManager.robot.reqProxy.loadMap();
    }

    @Override
    public void setSpeed(float speed) {
        robotManager.robot.reqProxy.setSpeed(speed);
    }
}
