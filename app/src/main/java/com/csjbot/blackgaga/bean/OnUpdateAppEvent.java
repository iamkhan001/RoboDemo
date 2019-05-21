package com.csjbot.blackgaga.bean;

/**
 *
 * @author Ben
 * @date 2018/4/17
 */

public class OnUpdateAppEvent {

    private boolean isUpdateApp;

    public OnUpdateAppEvent(boolean isUpdateApp) {
        this.isUpdateApp = isUpdateApp;
    }

    public boolean isUpdateApp() {
        return isUpdateApp;
    }

    public void setUpdateApp(boolean updateApp) {
        isUpdateApp = updateApp;
    }
}
