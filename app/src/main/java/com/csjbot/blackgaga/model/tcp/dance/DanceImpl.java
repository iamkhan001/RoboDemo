package com.csjbot.blackgaga.model.tcp.dance;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.feature.navigation.ArmLooper;
import com.csjbot.blackgaga.model.tcp.body_action.IAction;
import com.csjbot.blackgaga.model.tcp.chassis.IChassis;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;

/**
 * 跳舞
 * Created by jingwc on 2017/9/26.
 */

public class DanceImpl implements IDance {

    IAction action;
    IChassis chassis;

    ArmLooper armLooper;

    volatile boolean isDance;

    public DanceImpl(){
        action = ServerFactory.getActionInstance();
        chassis = ServerFactory.getChassisInstance();
        armLooper = new ArmLooper();
    }

    @Override
    public synchronized void dance() {
        isDance = true;

        new Thread(() -> {
            while(isDance){
                chassis.moveLeft();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        if(BuildConfig.robotType.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE)) {
            action.righLargeArmUp();
            armLooper.startLooper();
//            new Thread(()->{
//                while(isDance){
//                    action.denyAction();
//                    action.nodAction();
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
        }else if(BuildConfig.robotType.equals(BuildConfig.ROBOT_TYPE_DEF_SNOW)){
            new Thread(()->{
                while(isDance){
                    action.snowDoubleArm();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public synchronized void stopDance(){
        isDance = false;
        if(BuildConfig.robotType.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE)) {
            armLooper.stopLooper();
        }
    }
}
