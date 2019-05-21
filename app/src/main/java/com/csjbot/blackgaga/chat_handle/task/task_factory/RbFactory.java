package com.csjbot.blackgaga.chat_handle.task.task_factory;

import com.csjbot.blackgaga.chat_handle.constans.Constants;
import com.csjbot.blackgaga.chat_handle.task.RbCloud;
import com.csjbot.blackgaga.chat_handle.task.RbCustom;
import com.csjbot.blackgaga.chat_handle.task.RbZhichi;
import com.csjbot.blackgaga.chat_handle.task.base.RbTask;

import java.util.HashMap;
import java.util.Map;

/**
 * 对象实例化工厂
 * Created by jingwc on 2017/6/30.
 */

public class RbFactory {

    private static final Map<Integer,RbTask> prMap = new HashMap<>();

    public static <T extends RbTask> T createRbTask(int type) {
        RbTask rbTask = null;
        if (prMap.containsKey(type)) {
            rbTask = prMap.get(type);
        } else {
            boolean disable = false;
            switch (type) {
                case Constants.Scheme.CUSTOM:
                    rbTask = new RbCustom();
                    disable = Constants.SchemeStatus.CUSTOM_DISABLE;
                    break;
                case Constants.Scheme.CLOUD:
                    rbTask = new RbCloud();
                    disable = Constants.SchemeStatus.CLOUD_DISABLE;
                    break;
                case Constants.Scheme.PROFESSIONAL:
                    rbTask = new RbZhichi();
                    disable = Constants.SchemeStatus.PROFESSIONAL_DISABLE;
                    break;
//                case Constants.Scheme.GOSSIP:
//                    rbTask = new RbDumi();
//                    disable = Constants.SchemeStatus.GOSSIP_DISABLE;
//                    break;
                default:
                    break;
            }
            if (rbTask != null) {
                rbTask.setDisable(disable);
            }
        }
        return (T) rbTask;
    }
}
