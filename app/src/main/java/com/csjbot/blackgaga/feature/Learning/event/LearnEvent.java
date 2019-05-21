package com.csjbot.blackgaga.feature.Learning.event;

/**
 *
 * @author Ben
 * @date 2018/3/15
 */

public class LearnEvent {

    private boolean isCloseApp;

    public LearnEvent(boolean isCloseApp) {
        this.isCloseApp = isCloseApp;
    }

    public boolean isCloseApp() {
        return isCloseApp;
    }

    public void setCloseApp(boolean closeApp) {
        isCloseApp = closeApp;
    }

    @Override
    public String toString() {
        return "LearnEvent{" +
                "isCloseApp=" + isCloseApp +
                '}';
    }
}
