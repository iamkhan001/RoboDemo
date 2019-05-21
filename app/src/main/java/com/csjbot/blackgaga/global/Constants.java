package com.csjbot.blackgaga.global;

import android.os.Environment;
import android.text.TextUtils;

import com.csjbot.blackgaga.bean.ExpressionControlBean;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.feature.clothing.bean.ClothTypeBean;
import com.csjbot.blackgaga.home.CSJBotHomeActivity;
import com.csjbot.blackgaga.home.ChaoShiHomeActivity;
import com.csjbot.blackgaga.home.FuZhuangHomeActivity;
import com.csjbot.blackgaga.home.HomeActivity;
import com.csjbot.blackgaga.home.JiChangHomeActivity;
import com.csjbot.blackgaga.home.JiaDianHomeActivity;
import com.csjbot.blackgaga.home.JiaoYu2HomeActivity;
import com.csjbot.blackgaga.home.JiuDianHomeActivity;
import com.csjbot.blackgaga.home.QiCheHomeActivity;
import com.csjbot.blackgaga.home.ShangShaHomeActivity;
import com.csjbot.blackgaga.home.ShiPinHomeActivity;
import com.csjbot.blackgaga.home.ShuiWuHomeActivity3;
import com.csjbot.blackgaga.home.YaoDianHomeActivity;
import com.csjbot.blackgaga.home.YinHangHomeActivity;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jingwc on 2017/10/14.
 */

public class Constants {

    // 上身板
    public static final String UP_PLATE = "up_sn";
    // 下身板
    public static final String DOWN_PLATE = "down_plate";
    // 上位机
    public static final String UP_COMPUTER = "up_computer";
    // 导航核心板
    public static final String NAV = "nav";
    // SN
    public static final String SN = "sn";
    // 与Linux连接状态
    public static final String CONNECT_LINUX_BROADCAST = "linux_broadcast";
    public static final String LINUX_CONNECT_STATE = "linux_connect_state";

    // 更新LOGO
    public static final String UPDATE_LOGO = "update_logo";

    public static final String PRODUCT_IMG_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "blackgaga" + File.separator + "product_image" + File.separator;//产品图片路径（包括轮播图片）
    public static final String PRODUCT_VIDEO_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "blackgaga" + File.separator + "product_video" + File.separator;//产品视频路径

    public static final String NAVI_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "blackgaga" + File.separator + "navi" + File.separator;//导航音乐背景存放路径

    public static final String SKIN_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "blackgaga" + File.separator + "skins" + File.separator;//皮肤包存放路径

    public static final String LOGO_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "blackgaga" + File.separator + "logo" + File.separator;//logo存放路径

    public static final String WEB_CACHE_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "blackgaga" + File.separator + "cache" + File.separator;//web缓存路径

    // u盘路径
    public static final String USB_PATH = "/mnt/usb_storage";
    // 导航数据存放路径
    public static String NAV_DATA_PATH = Environment.getExternalStorageDirectory() + File.separator + "blackgaga"
            + File.separator + "nav_data";
    public static String[] imageSuffixs = {
            ".jpg", ".jpeg", ".png", "gif"
    };
    public static String[] suffixs = {
            ".jpg", ".jpeg", ".png", "gif", "avi", "wma", "rmvb", "rm", "flash", "mp4", "mid", "3gp", "mov", "mkv"
    };
    //导航界面和导航设置界面，地图坐标系数
    public static final float MULTIPAL_DATA = 1.2f;//长
    public static final float MULTIPAL_DATA_WIDTH = 1.2f;//宽
    public static final float MULTIPAL_DATA_VERTICAL = 1.5f;//竖屏长
    public static final float MULTIPAL_DATA_WIDTH_VERTICAL = 1.23f;//竖屏宽
    // 搜索成功
    public static final int SEARCH_SUC = 0;
    // 复制成功
    public static final int COPY_SUC = 10;

    public static boolean isLoadMapSuccess = false;//地图是否是恢复成功
    public static long internalCheckLinux = 8000;//地图操作超时时间

    //声音
    public final static String WAV_PATH = "wav/";
    public final static String VOLUME_PATH = WAV_PATH + "volume.wav";

    public final static String KB = "KB";
    public final static String MB = "MB";
    public final static String GB = "GB";
    public final static String TB = "TB";
    public final static int descLimit = 10;//导航到点讲解大小设置 10kb
    public final static int inRunningLimit = 10;//导航途中讲解大小设置 10kb
    public final static int musicLimit = 5;//导航背景音乐大小设置 5Mb
    public final static int videoLimit = 300;//导航过程视频  300Mb

    public static boolean ChangeLan = false;//是否正在切换语言

    public static class Language {
        // 中文
        public final static int CHINESE = 0;
        // 英语-美国
        public final static int ENGELISH_US = 1;
        // 英语-英国
        public final static int ENGELISH_UK = 2;
        // 英语-澳大利亚
        public final static int ENGELISH_AUSTRALIA = 3;
        public final static int ENGELISH_CANADA = 3;
        // 英语-印度
        public final static int ENGELISH_INDIA = 4;
        // 日语
        public final static int JAPANESE = 5;
        // 韩语
        public final static int KOREAN = 6;
        // 法语-法国
        public final static int FRANCH_FRANCE = 7;
        // 法语-加拿大
        public final static int FRANCH_CANADA = 8;
        // 西班牙语-西班牙
        public final static int SPANISH_SPAIN = 9;
        // 西班牙语-拉美
        public final static int SPANISH_LATAM = 10;
        // 葡萄牙语-葡萄牙
        public static final int PORTUGUESE_PORTUGAL = 11;
        // 葡萄牙语-巴西
        public static final int PORTUGUESE_BRAZIL = 12;
        // 印尼语
        public static final int INDONESIA = 13;
        // 俄语
        public static final int RUSSIAN = 14;

        public static int CURRENT_LANGUAGE = CHINESE;

        public static boolean isChinese() {
            if (CURRENT_LANGUAGE == CHINESE) {
                return true;
            }
            return false;
        }

        public static boolean isEnglish() {
            if(isEnglishUS() || isEnglishUK() || isEnglishAustralia() || isEnglishIndia()){
                return true;
            }
            return false;
        }

        public static boolean isEnglishUS() {
            if (CURRENT_LANGUAGE == ENGELISH_US) {
                return true;
            }
            return false;
        }

        public static boolean isEnglishUK() {
            if (CURRENT_LANGUAGE == ENGELISH_UK) {
                return true;
            }
            return false;
        }

        public static boolean isEnglishAustralia() {
            if (CURRENT_LANGUAGE == ENGELISH_AUSTRALIA) {
                return true;
            }
            return false;
        }

        public static boolean isEnglishIndia() {
            if (CURRENT_LANGUAGE == ENGELISH_INDIA) {
                return true;
            }
            return false;
        }

        public static boolean isJapanese() {
            if (CURRENT_LANGUAGE == JAPANESE) {
                return true;
            }
            return false;
        }

        public static boolean isKOREAN() {
            if (CURRENT_LANGUAGE == KOREAN) {
                return true;
            }
            return false;
        }

        public static boolean isFrenchFrance() {
            if (CURRENT_LANGUAGE == FRANCH_FRANCE) {
                return true;
            }
            return false;
        }

        public static boolean isFranchCanada() {
            if (CURRENT_LANGUAGE == FRANCH_CANADA) {
                return true;
            }
            return false;
        }

        public static boolean isSpanishSpain() {
            if (CURRENT_LANGUAGE == SPANISH_SPAIN) {
                return true;
            }
            return false;
        }

        public static boolean isSpanishLatAm() {
            if (CURRENT_LANGUAGE == SPANISH_LATAM) {
                return true;
            }
            return false;
        }

        public static boolean isPortuguesePortugal() {
            if (CURRENT_LANGUAGE == PORTUGUESE_PORTUGAL) {
                return true;
            }
            return false;
        }

        public static boolean isPortugueseBrazil() {
            if (CURRENT_LANGUAGE == PORTUGUESE_BRAZIL) {
                return true;
            }
            return false;
        }

        public static boolean isIndonesia() {
            if (CURRENT_LANGUAGE == INDONESIA) {
                return true;
            }
            return false;
        }

        public static boolean isRussian() {
            return CURRENT_LANGUAGE == RUSSIAN;
        }


        public static String getLanguageStr() {
            String language = "cn";
            switch (Language.CURRENT_LANGUAGE) {
                case Language.CHINESE:
                    language = "cn";
                    break;
                case Language.ENGELISH_US:
                case Language.ENGELISH_UK:
                case Language.ENGELISH_AUSTRALIA:
                case Language.ENGELISH_INDIA:
                    language = "en";
                    break;
                case Language.JAPANESE:
                    language = "jp";
                    break;
                case Language.FRANCH_FRANCE:
                case Language.FRANCH_CANADA:
                    language = "fr";
                    break;
                case Language.SPANISH_SPAIN:
                case Language.SPANISH_LATAM:
                    language = "es";
                    break;
                case Language.PORTUGUESE_PORTUGAL:
                case Language.PORTUGUESE_BRAZIL:
                    language = "pt";
                    break;
                case Language.INDONESIA:
                    language = "in";
                    break;
                case Language.RUSSIAN:
                    language = "ru";
                    break;
            }
            return language;
        }


        public static String getISOLanguage() {
            String language = "zh";
            switch (Language.CURRENT_LANGUAGE) {
                case Language.CHINESE:
                    language = "zh";
                    break;
                case Language.ENGELISH_US:
                case Language.ENGELISH_UK:
                case Language.ENGELISH_AUSTRALIA:
                case Language.ENGELISH_INDIA:
                    language = "en";
                    break;
                case Language.JAPANESE:
                    language = "ja";
                    break;
                case Language.KOREAN:
                    language = "ko";
                    break;
                case Language.FRANCH_FRANCE:
                case Language.FRANCH_CANADA:
                    language = "fr";
                    break;
                case Language.SPANISH_SPAIN:
                case Language.SPANISH_LATAM:
                    language = "es";
                    break;
                case Language.PORTUGUESE_PORTUGAL:
                case Language.PORTUGUESE_BRAZIL:
                    language = "pt";
                    break;
                case Language.INDONESIA:
                    language = "in";
                    break;
                case Language.RUSSIAN:
                    language = "ru";
                    break;
            }
            return language;
        }

        public static String getLanguageStr2() {
            String language = "zh_CN";
            switch (Language.CURRENT_LANGUAGE) {
                case Language.CHINESE:
                    language = "zh_CN";
                    break;
                case Language.ENGELISH_US:
                    language = "en_US";
                    break;
                case Language.ENGELISH_UK:
                    language = "en_UK";
                    break;
                case Language.ENGELISH_AUSTRALIA:
                    language = "en_AU";
                    break;
                case Language.ENGELISH_INDIA:
                    language = "en_IN";
                    break;
                case Language.JAPANESE:
                    language = "ja_JP";
                    break;
                case Language.FRANCH_FRANCE:
                    language = "fr_FR";
                    break;
                case Language.FRANCH_CANADA:
                    language = "fr_CA";
                    break;
                case Language.SPANISH_SPAIN:
                    language = "es_ES";
                    break;
                case Language.SPANISH_LATAM:
                    language = "es_LA";
                    break;
                case Language.PORTUGUESE_PORTUGAL:
                    language = "pt_PT";
                    break;
                case Language.PORTUGUESE_BRAZIL:
                    language = "pt_BR";
                    break;
                case Language.INDONESIA:
                    language = "in_ID";
                    break;
                case Language.RUSSIAN:
                    language = "ru_RU";
                    break;
            }
            return language;
        }

    }

    // 是否是设置一键导览
    public static final String GUIDE_ALL = "guide_all";

    // 是否跳转其他界面
    public static final String JUMP_ACTIVITY_NAME = "jump_activity_name";

    public static final String WAKE_UP = "wake_up";

    public static final class Face {
        public static boolean person = false;
    }

    /**
     * 充电信息
     */
    public static final class Charging {

        /**
         * 低电量
         */
        private static int lowElectricity;

        /**
         * 是否自动充电
         */
        private static boolean isAutoCharging;

        /**
         * 是否使用充电桩
         */
        private static boolean isChargingPile;

        /**
         * 初始化本地电量信息
         */
        public static void initCharging() {
            isChargingPile = SharedPreUtil.getBoolean(SharedKey.CHARGING_NAME, SharedKey.CHARGING_PILE_KEY, false);
            isAutoCharging = SharedPreUtil.getBoolean(SharedKey.CHARGING_NAME, SharedKey.CHARGING_AUTO, true);
            lowElectricity = SharedPreUtil.getInt(SharedKey.CHARGING_NAME, SharedKey.CHARGING_LOW_ELECTRICITY, 20);
        }

        public static int getLowElectricity() {
            return lowElectricity;
        }

        public static void saveLowElectricity(int electricity) {
            SharedPreUtil.putInt(SharedKey.CHARGING_NAME, SharedKey.CHARGING_LOW_ELECTRICITY, electricity);
            lowElectricity = electricity;
        }

        public static void saveAutoCharging(boolean b) {
            SharedPreUtil.putBoolean(SharedKey.CHARGING_NAME, SharedKey.CHARGING_AUTO, b);
            isAutoCharging = b;
        }

        public static boolean getAutoCharging() {
            return isAutoCharging;
        }

        public static void saveChargingPile(boolean b) {
            SharedPreUtil.putBoolean(SharedKey.CHARGING_NAME, SharedKey.CHARGING_PILE_KEY, b);
            isChargingPile = b;
        }

        public static boolean getChargingPile() {
            return isChargingPile;
        }

        /**
         * 是否达到低电量回充的条件
         *
         * @param electricity
         * @return
         */
        public static boolean isGoCharging(int electricity) {
            if (lowElectricity >= electricity) {
                return true;
            } else {
                return false;
            }
        }

    }

    public static final class NearbyKeyWord {

        public static String[] intents = new String[]{
                "附近",
                "哪边",
                "哪里有"
        };

        public static String[] keyWords = new String[]{
                "美食",
                "景点",
                "休闲娱乐",
                "共享单车",
                "超市",
                "ATM",
                "厕所",
                "快捷酒店",
                "酒店",
                "网吧",
                "地铁",
                "加油站"
        };
    }

    public static final class ProductKeyWord {
        public static String[] intents = new String[]{
                "我想买", "我想要", "我要", "我买", "给我来", "给我拿"
        };

        public static List<RobotSpListBean.ResultBean.ProductBean> products;

        public static void initKeywords() {
            products = ProductProxy.newProxyInstance().getAllSpInformation();
            CsjlogProxy.getInstance().info("products:" + products.size());
        }

    }

    /**
     * 定位信息类
     */
    public static final class LocationInfo {

        // 纬度
        public static double latitude = 0;

        // 经度
        public static double longitude = 0;

        // 定位精度
        public static float radius = 0;

        // 地址
        public static String address;

        // 城市
        public static String city;
    }

    public static class Scene {

        public static String CurrentScene = "";

        public static String JiuDianScene = "JiuDianScene";

        public static String YinHangScene = "YinHangScene";

        public static String JiChangScene = "JiChangScene";

        public static String XianYangJiChang = "XianYangJiChang";

        public static String XingZheng = "XingZheng";

        public static String CheGuanSuo = "CheGuanSuo";

        public static String ShuiWu = "ShuiWu";

        public static String Fuzhuang = "Fuzhuang";
    }

    public static class HomePageClass {
        public static String[] homePage = new String[]{
                ChaoShiHomeActivity.class.getName(),
                CSJBotHomeActivity.class.getName(),
                FuZhuangHomeActivity.class.getName(),
                HomeActivity.class.getName(),
                JiaDianHomeActivity.class.getName(),
                JiaoYu2HomeActivity.class.getName(),
                JiChangHomeActivity.class.getName(),
                JiuDianHomeActivity.class.getName(),
                QiCheHomeActivity.class.getName(),
                ShangShaHomeActivity.class.getName(),
                ShiPinHomeActivity.class.getName(),
                ShuiWuHomeActivity3.class.getName(),
                YaoDianHomeActivity.class.getName(),
                YinHangHomeActivity.class.getName()
        };
    }

    public static class VirtualKey {
        public static boolean isShow = false;
    }

    public static class HomePage {
        public static boolean isHomePageLoadSuccess;
    }

    public static class Path {
        public static final String EXPRESSION_CONFIG_PATH = Environment.getExternalStorageDirectory() +
                File.separator + "blackgaga" + File.separator + "expression" + File.separator;//表情配置文件路径

        public static final String LOGO_PATH = Environment.getExternalStorageDirectory() +
                File.separator + "blackgaga" + File.separator + "logo" + File.separator;//logo存放路径

        public static final String EXPRESSION_CONFIG_FILE_NAME = "expression.txt";

        public static final String LOGO_FILE_NAME = "logo.jpg";
    }

    public static ExpressionControlBean expressionControlBean;


    public static class UnknownProblemAnswer {

        public static boolean isUseDefaultAnswer = true;

        static List<String> defaultAnswers = new ArrayList<>();

        static {
            defaultAnswers.add("今天天气不错哟!");
            defaultAnswers.add("呀,这个问题我还不知道呢!");
            defaultAnswers.add("快看,有飞碟!");
            defaultAnswers.add("别问我,我什么都不知道");
            defaultAnswers.add("我不听我不听");
            defaultAnswers.add("不想跟你说话,哼!");
            defaultAnswers.add("不知道怎么回答呢");
            defaultAnswers.add("你刚才说什么?");
            defaultAnswers.add("好了好了,我知道了");
            defaultAnswers.add("我要睡觉了");
        }

        static String getDefaultAnser() {
            CsjlogProxy.getInstance().info("getDefaultAnser:");
            int random = (int) (Math.random() * defaultAnswers.size());
            if (random >= 0 && random < defaultAnswers.size()) {
                return defaultAnswers.get(random);
            }
            return defaultAnswers.get(0);
        }

        static String getCustomerAnser() {
            if (customerAnswers.size() == 0) {
                return getDefaultAnser();
            }
            CsjlogProxy.getInstance().info("getCustomerAnser:");
            int random = (int) (Math.random() * customerAnswers.size());
            if (random >= 0 && random < customerAnswers.size()) {
                return customerAnswers.get(random);
            }
            return getDefaultAnser();
        }

        public static String getAnswer() {
            CsjlogProxy.getInstance().info("isUseDefaultAnswer:" + isUseDefaultAnswer);
            if (isUseDefaultAnswer) {
                return getDefaultAnser();
            } else {
                return getCustomerAnser();
            }
        }


        static List<String> customerAnswers = new ArrayList<>();

        public static void refreshCustomerAnswers(List<String> answers) {
            customerAnswers.clear();
            customerAnswers.addAll(answers);
        }

        public static void initUnknownProblemAnswer() {
            isUseDefaultAnswer = SharedPreUtil.getBoolean(SharedKey.UNKNOWN_PROBLEM_ANSWER, SharedKey.UNKNOWN_PROBLEM_ANSWER_IS_USE_DEFAULT, true);
            String customerAnswerJson = SharedPreUtil.getString(SharedKey.UNKNOWN_PROBLEM_ANSWER, SharedKey.UNKNOWN_PROBLEM_ANSWER_CUSTOMER_ANSWER);
            if (!TextUtils.isEmpty(customerAnswerJson)) {
                customerAnswers = new Gson().fromJson(customerAnswerJson, new TypeToken<List<String>>() {
                }.getType());
            }
        }
    }

    public static class ClothProduct {
        public static ClothTypeBean clothTypeBean;
    }

    public static boolean isOpenChatView = true;

    /**
     * Nuance语音是否被激活
     */
    public static boolean isOpenNuance = false;
}
