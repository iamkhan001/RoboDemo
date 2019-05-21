package com.csjbot.blackgaga.jpush.diaptch.constants;

/**
 * 所有推送消息MSG_ID
 * Created by jingwc on 2018/3/2.
 */

public class ConstantsId {

    /**
     * 全局公告功能的消息MSG_ID
     */
    public static class GlobalNoticeId {

        public static final String TAG = "NOTICE_GLOBAL";

        // 打开全局通告
        public static final String NOTICE_GLOBAL_OPEN = "NOTICE_GLOBAL_OPEN";
        // 关闭全局通告
        public static final String NOTICE_GLOBAL_CLOSE = "NOTICE_GLOBAL_CLOSE";
        // 全局通告显示消息
        public static final String NOTICE_GLOBAL_MESSAGE = "NOTICE_GLOBAL_MESSAGE";
    }

    public static class ExpressionId {
        public static final String TAG = "EXPRESSION";

        public static final String EXPRESSION_UPDATE = "EXPRESSION_UPDATE";
    }

    /**
     * 会员信息变更
     */
    public static class UserInformationId {

        public static final String TAG = "NOTICE_FACE_INFO";
        /**
         * 会员信息删除
         */
        public static final String NOTICE_FACE_INFO_DELETE = "NOTICE_FACE_INFO_DELETE";
        /**
         * 会员信息修改
         */
        public static final String NOTICE_FACE_INFO_CHANGED = "NOTICE_FACE_INFO_CHANGED";

    }

    /**
     * 主页内容变化通知
     */
    public static class AllContentTypesChanged {
        /**
         * 主页内容变化
         */
        public static final String NOTICE_ALL_CONTENT_TYPES_CHANGED = "NOTICE_ALL_CONTENT_TYPES_CHANGED";
    }

    /**
     * 广告内容变化通知
     */
    public static class AdvertisementChanged {
        public static final String NOTICE_ADVERTISEMENT_CHANGED = "NOTICE_ADVERTISEMENT_CHANGED";
    }

    /**
     * 场景
     */
    public static class Scene{

        public static final String TAG = "SCENE";

        // 场景切换通知
        public static final String SCENE_CHANGE = "SCENE_CHANGE";
    }
}
