package com.csjbot.blackgaga.jpush.constant;

/**
 * @author Ben
 * @date 2018/2/24
 */

public class JPushConstant {

    /**
     * 修改为项目包名
     */
    public static final String CATEGORY = "com.csjbot.blackgaga";

    /**
     * 事件类型为接收消息
     */
    public static final int JPUSH_MESSAGE = 0x01;
    /**
     * 事件类型为处理Tags、Alias
     */
    public static final int JPUSH_TAGS_ALIAS = 0x02;

    /**
     * 消息的类型：接受到推送下来的自定义消息
     */
    public static final int ACTION_MESSAGE_RECEIVED = 0x001;

    /**
     * 消息的类型：接受到用户REGISTRATION_ID
     */
    public static final int ACTION_REGISTRATION_ID = 0x002;


    /**
     * 处理Tags、Alias的类型：onTagOperatorResult
     */
    public static final int onTagOperatorResult = 0x0001;
    /**
     * 处理Tags、Alias的类型：onCheckTagOperatorResult
     */
    public static final int onCheckTagOperatorResult = 0x0002;
    /**
     * 处理Tags、Alias的类型：onAliasOperatorResult
     */
    public static final int onAliasOperatorResult = 0x0003;
    /**
     * 处理Tags、Alias的类型：onMobileNumberOperatorResult
     */
    public static final int onMobileNumberOperatorResult = 0x0004;
}
