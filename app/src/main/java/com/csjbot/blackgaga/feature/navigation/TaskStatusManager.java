package com.csjbot.blackgaga.feature.navigation;

/**
 * Created by xiasuhuei321 on 2017/11/11.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class TaskStatusManager {
    // 开始
    public static final int START = 1;
    // 从暂停状态恢复到工作
    public static final int RESUME = 2;
    // 停止，随后就会转化为等待
    public static final int STOP = 3;
    // 暂停状态
    public static final int PAUSE = 4;
    // 等待，可以开始工作的状态
    public static final int AWAIT = 5;

    public volatile int workStatus = AWAIT;

    /**
     * 当工作开始时，工作状态变为开始
     */
    public void start(){
        workStatus = START;
    }

    /**
     * 当恢复工作时，工作状态变为开始
     */
    public void resume(){
        workStatus = START;
    }

    /**
     * 当暂停工作时，工作状态变为暂停
     */
    public void pause(){
        workStatus = PAUSE;
    }

    /**
     * 当停止工作时，工作状态变为等待，可以执行下一项工作
     */
    public void stop(){
        workStatus = AWAIT;
    }

    public void init(){
        stop();
    }

}