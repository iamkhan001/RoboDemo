package com.csjbot.blackgaga.util;

/**
 * Created by xiasuhuei321 on 2017/8/14.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class SharedKey {
    /**
     * 各种key
     */
    public static final String PRODUCT_INFO = "product_info";

    /**
     * sp的name
     */
    public static final String PRODUCT_SP = "product_sp";

    public static final String  SCENE_LIST = "scene_list";

    public static final String SCENE_KEY = "scene_key";

    /**
     * 今日促销
     */
    public static String SALES = "sales";
    public static String SALES_KEY = "sales_key";

    /**
     * 今日活动
     */
    public static String ACTIVITIES = "activities";
    public static String ACTIVITIES_KEY = "activities_key";

    /**
     * 公司简介
     */
    public static String COMPANY = "company";
    public static String COMPANY_KEY = "company_key";


    /**
     * 内容名
     */
    public static String CONTENT_NAME = "content_name";
    /**
     * scene的名字
     */
    public static class CONTENT_LAN{
        public static final String CONTENT_CN = "content_cn";

        public static final String CONTENT_JP = "content_jp";

        public static final String CONTENT_EN = "content_en";

        public static String getLanguageStr(String lan){
            String name = CONTENT_CN;
            switch (lan){
                case "cn":
                    name = CONTENT_CN;
                    break;
                case "en":
                    name = CONTENT_JP;
                    break;
                case "jp":
                    name = CONTENT_EN;
                    break;
            }
            return name;
        }
    }

    /**
     * sp的名字
     */
    @Deprecated
    public static class SP_LAN{
        public static final String PRODUCT_SP_CN = "product_sp_cn";

        public static final String PRODUCT_SP_JP = "product_sp_jp";

        public static final String PRODUCT_SP_EN = "product_sp_en";

        public static String getLanguageStr(String lan){
            String name = PRODUCT_SP_CN;
            switch (lan){
                case "cn":
                    name =  PRODUCT_SP_CN;
                    break;
                case "en":
                    name = PRODUCT_SP_JP;
                    break;
                case "jp":
                    name = PRODUCT_SP_EN;
                    break;
            }
            return name;
        }
    }

    /**
     * menu列表的名字
     */

    public static final String PRODUCT_MENU_LIST = "product_list";

    public static class MENU_LAN{
        public static final String PRODUCT_MENU_CN = "product_menu_cn";

        public static final String PRODUCT_MENU_EN = "product_menu_en";

        public static final String PRODUCT_MENU_JP= "product_menu_jp";

        public static String getLanguageStr(String lan){
            String name = PRODUCT_MENU_CN;
            switch (lan){
                case "cn":
                    name =  PRODUCT_MENU_CN;
                    break;
                case "en":
                    name = PRODUCT_MENU_EN;
                    break;
                case "jp":
                    name = PRODUCT_MENU_JP;
                    break;
            }
            return name;
        }
    }


    /**
     * apk 信息的配置文件名
     */
    public static final String APK_INFO = "apk_info";

    /**
     * apk md5存储键值
     */
    public static final String APK_MD5 = "apk_md5";

    public static final String NAVI_KEY = "NAVI_KEY";
    public static final String NAVI_NAME = "NAVI_NAME";
    public static final String YINGBIN_KEY = "YINGBIN_KEY";
    public static final String YINGBIN_NAME = "YINGBIN_NAME";
    public static final String GUIDE_KEY = "GUIDE_KEY";
    public static final String GUIDE_NAME = "GUIDE_NAME";
    public static final String SPEED_KEY = "SPEED_KEY";//系统设置速度
    public static final String SPEED_NAME = "SPEED_NAME";
    public static final String ELECTRICQUANTITY_NAME = "ELECTRICQUANTITY_NAME";//电量设置
    public static String DEFAULT_ADRESS = "DEFAULT_ADRESS";//阿里云
    public static String TEST_ADRESS = "TEST_ADRESS";//测试地址
    public static final String LANGUAGEMODE_KEY= "LANGUAGEMODE_KEY";
    public static final String LANGUAGEMODE_NAME= "LANGUAGEMODE_NAME";
    public static final String VOICEKEY= "VOICEKEY";
    public static final String VOICENAME= "VOICENAME";
    //当前sp保存文件名（主页）
    public static final String MAINPAGE= "MAINPAGE";
    //保存当前的主页名称
    public static final String MAINPAGE_KEY= "MAINPAGE_KEY";

    //判断是否是第一个主页（主页）
    public static final String ISFIRSTCOMING= "ISFIRSTCOMING";
    //保存当前的主页名称
    public static final String ISFIRSTCOMING_KEY= "ISFIRSTCOMING_KEY";

    /**
     * 导航模式
     */
    public static final String NAVIMODE_KEY = "NAVIMODE_KEY";
    public static final String NAVIMODE_NAME = "NAVIMODE_NAME";

    /**
     * 电量
     */
    public static final String CHARGING_NAME= "CHARGING";
    public static final String CHARGING_AUTO = "CHARGING_AUTO";
    public static final String CHARGING_LOW_ELECTRICITY = "CHARGING_LOW_ELECTRICITY";
    public static final String CHARGING_PILE_KEY = "CHARGING_PILE_KEY";


    // 导航地图图片路径
    public static final String MAP_PATH = "MAP_PATH";
    // 默认logo
    public static final String LOGO = "logo";
    public static final String LOGONAME = "logoname";
    public static final String LOGOURL = "logourl";

    // logo type
    public static final String LOGOTYPE = "logotype";
    //开机自启动
    public static final String ISLOADMAP= "ISLOADMAP";

    public static final String STARTMODE = "STARTMODE";//0 冷启动  1 热启动

    public static final String POISEARCH_FAKELOCATION ="fakelocation";

    public static final String YINGBINGSETTING = "setting_yingbin";


    public static final String ISACTIVE = "active_or_not";


    public static final String SPEAKERVOICE = "settting_speaker";
    public static final String SPEAKER_KEY = "speaker";
    public static final String DEFAULT_SPEAKER = "jiajia";
    public static final String DEFAULT_ENGLISH_SPEAKER = "catherine";


    //更新App
    public static final String UPDATE_APP="update_app";

    /**
     * 位置信息
     */
    public static final String LOCATION = "LOCATION";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String ADDRESS = "ADDRESS";
    public static final String CITY = "CITY";

    /**
     * 评价设置
     */
    public static final String EVALUATE = "EVALUATE";
    public static final String EVALUATE_SWITCH = "EVALUATE_SWITCH";
    public static final String EVALUATE_TIME = "EVALUATE_TIME";


    /**
     * 导航声控设置
     */
    public static final String NAVI_SOUND_CONTROL = "NAVI_SOUND_CONTROL";
    public static final String NAVI_SOUND_CONTROL_PAUSE = "NAVI_SOUND_CONTROL_PAUSE";
    public static final String NAVI_SOUND_CONTROL_RESUME = "NAVI_SOUND_CONTROL_RESUME";
    public static final String NAVI_SOUND_CONTROL_END = "NAVI_SOUND_CONTROL_END";

    /**
     * 虚拟键设置
     */
    public static final String VIRTUAL_KEY = "VIRTUAL_KEY";

    public static final String NAVI_AUTO_EXIT = "NAVI_AUTO_EXIT";

    public static final String CUSTOMER_SERVICE_VOICE_TYPE = "CUSTOMER_SERVICE_VOICE_TYPE";

    public static final String AUTO_SPEECH_RECOGNITION_SWITCH = "AUTO_SPEECH_RECOGNITION_SWITCH";

    public static final String AUTO_SPEECH_RECOGNITION_CLOSE_TIME = "AUTO_SPEECH_RECOGNITION_CLOSE_TIME";


    /**
     * 未知问题答案
     */
    public static final String UNKNOWN_PROBLEM_ANSWER = "UNKNOWN_PROBLEM_ANSWER";
    public static final String UNKNOWN_PROBLEM_ANSWER_IS_USE_DEFAULT = "UNKNOWN_PROBLEM_ANSWER_IS_USE_DEFAULT";
    public static final String UNKNOWN_PROBLEM_ANSWER_CUSTOMER_ANSWER = "UNKNOWN_PROBLEM_ANSWER_CUSTOMER_ANSWER";

    /**
     * 聊天框
     */
    public static final String CHAT_VIEW = "CHAT_VIEW";
    public static final String CHAT_VIEW_IS_OPEN = "CHAT_VIEW_IS_OPEN";

    public static final String CHINESE_LANGUAGE_TYPE = "CHINESE_LANGUAGE_TYPE";


    public static final String WAKEUP_STOP = "WAKEUP_STOP";

    public static final String INTERNATIONALIZATION = "INTERNATIONALIZATION";

    public static final String TTS_VOLUME = "TTS_VOLUME";

}
