package com.csjbot.blackgaga.chat_handle.constans;

import java.util.ArrayList;

/**
 * Created by jingwc on 2017/9/12.
 */

public class Constants {

    /**
     * 消息处理模式
     */
    public static final class Mode{
        /* 普通模式 */
        public static final int NORML_MODE = 10001;

        /* 自由模式 */
        public static final int FREE_MODE = 10002;
    }

    /**
     * 消息类型
     */
    public static final class Type{
        /* 超时消息类型 */
        public static final int TIMEOUT_MSG = 101;

        /* 普通消息类型 */
        public static final int NORML_MSG = 102;

        /* 初始化消息类型 */
        public static final int INIT_MSG = 103;
    }

    /**
     * 优先级
     */
    public static final class Priority{
        /* 最高优先级 */
        public static final int ONE = 1000;
        public static final int TWO = 999;
        public static final int THREE = 998;
        public static final int FOUR = 997;

        public static final ArrayList<Integer> prioritys = new ArrayList();

        static{
            if(prioritys != null) {
                prioritys.add(ONE);
                prioritys.add(TWO);
                prioritys.add(THREE);
                prioritys.add(FOUR);
            }
        }

        /**
         * 返回下一级
         * @param priority
         * @return
         */
        public static int getNextPriority(int priority){
            return --priority;
        }

        /**
         * 优先级是否存在
         * @param priority
         * @return
         */
        public static boolean hasPriority(int priority){
            for (int i = 0 ; i < prioritys.size();i++){
                if(priority == prioritys.get(i)){
                    return true;
                }
            }
            return false;
        }

        /**
         * 是否是最后一个优先级
         * @param priority
         * @return
         */
        public static boolean isLastPriority(int priority){
            return prioritys != null ? (prioritys.get(prioritys.size()-1) == priority) : false;
        }
    }

    /**
     * 消息处理方案
     */
    public static final class Scheme{
        /*  自定义问答方案(优先级:最大优先级,作为方案一标识) */
        public static final int CUSTOM = Priority.ONE;

        /*  云问答方案(优先级作为方案一标识) */
        public static final int CLOUD = Priority.TWO;

        /*  专业问答方案(优先级作为方案一标识) */
        public static final int PROFESSIONAL = Priority.THREE;

        /*  闲聊问答方案(优先级作为方案一标识) */
        public static final int GOSSIP = Priority.FOUR;


        /* 方案列表 */
        public static ArrayList<Integer> schemes = new ArrayList<>();

        static {
            if(schemes != null) {
                schemes.add(CUSTOM);
                schemes.add(CLOUD);
                schemes.add(PROFESSIONAL);
                schemes.add(GOSSIP);
            }
        }
    }

    /**
     * 消息处理方案状态
     */
    public static final class SchemeStatus{
        /*  自定义问答方案 */
        public static boolean CUSTOM_DISABLE;

        /*  云问答方案 */
        public static boolean CLOUD_DISABLE;

        /*  专业问答方案 */
        public static boolean PROFESSIONAL_DISABLE;

        /*  闲聊问答方案 */
        public static boolean GOSSIP_DISABLE;
    }
}
