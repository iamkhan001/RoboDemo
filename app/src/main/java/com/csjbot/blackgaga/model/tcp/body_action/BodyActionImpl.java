package com.csjbot.blackgaga.model.tcp.body_action;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.model.tcp.base.BaseImpl;

/**
 * Created by jingwc on 2017/9/21.
 */

public class BodyActionImpl extends BaseImpl implements IAction {

    public BodyActionImpl() {
        robotManager = RobotManager.getInstance();
    }

    @Override
    public void leftLargeArmUp() {
        robotManager.robot.leftLargeArmUp();
    }

    @Override
    public void leftLargeArmDown() {
        robotManager.robot.leftLargeArmDown();
    }

    @Override
    public void leftSmallArmUp() {
        robotManager.robot.leftSmallArmUp();
    }

    @Override
    public void leftSmallArmDown() {
        robotManager.robot.leftSmallArmDown();
    }

    @Override
    public void righLargeArmUp() {
        robotManager.robot.righLargeArmUp();
    }

    @Override
    public void rightLargeArmDown() {
        robotManager.robot.rightLargeArmDown();
    }

    @Override
    public void rightSmallArmUp() {
        robotManager.robot.rightSmallArmUp();
    }

    @Override
    public void rightSmallArmDown() {
        robotManager.robot.rightSmallArmDown();
    }

    @Override
    public void doubleLargeArmUp() {
        robotManager.robot.doubleLargeArmUp();
    }

    @Override
    public void doubleLargeArmDown() {
        robotManager.robot.doubleLargeArmDown();
    }

    @Override
    public void doubleSmallArmUp() {
        robotManager.robot.doubleSmallArmUp();
    }

    @Override
    public void doubleSmallArmDown() {
        robotManager.robot.doubleSmallArmDown();
    }

    /**
     * 摇头动作
     */
    @Override
    public void denyAction() {
        if (BuildConfig.robotType.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE)) {
            robotManager.robot.denyAction();
        }
    }

    /**
     * 点头动作
     */
    @Override
    public void nodAction() {
        if (BuildConfig.robotType.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE)
                ||
                BuildConfig.robotType.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)) {
            robotManager.robot.nodAction();
        }
    }

    @Override
    public void resetAction() {
        robotManager.robot.resetAction();
    }

    @Override
    public void snowRightArm() {
        robotManager.robot.snowRightArm();
    }

    @Override
    public void snowLeftArm() {
        robotManager.robot.snowLeftArm();
    }

    @Override
    public void snowDoubleArm() {
        robotManager.robot.snowDoubleArm();
    }

    @Override
    public void startDance() {
        robotManager.robot.reqProxy.startDance();
    }

    @Override
    public void stopDance() {
        robotManager.robot.reqProxy.stopDance();
    }
}
