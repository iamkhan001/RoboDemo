package com.csjbot.blackgaga.feature.navigation;

import android.text.TextUtils;

import com.csjbot.blackgaga.home.HomeActivity;
import com.csjbot.blackgaga.localbean.NaviBean;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.promeg.Pinyin;

import java.util.List;

/**
 * Created by xiasuhuei321 on 2017/12/15.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class NaviTextHandler {
    public static PointMatchListener listener;
    public static HomeActivity activity;

    public static void init(HomeActivity activity) {
        NaviTextHandler.activity = activity;
    }

    public static boolean handle(String text) {
        BlackgagaLogger.debug(text);
        String pinyin = strConvertPinyin(text);
        List<NaviBean> data = NaviAction.getInstance().getData();

        for (String s : NAVI) {
            if (pinyin.contains(s)) {
                if (data == null || data.size() == 0) {
                    return false;
                }
                for (NaviBean naviBean : data) {
                    String namePinyin = strConvertPinyin(naviBean.getName());
                    String nickPinyin = strConvertPinyin(naviBean.getNickName());
                    if ((pinyin.contains(namePinyin) || pinyin.contains(nickPinyin)) && listener != null) {
                        listener.match(naviBean, true);
                        return true;
                    }
                }

                // 没有匹配上
                if (listener != null) listener.noMatch();
                return true;
            }
        }

        for (String s : GO_NAVI) {
            if (pinyin.contains(s) && listener != null) {
                listener.goNavi();
                return true;
            }
        }

        return false;
    }

    /**
     * 汉字转拼音方法
     *
     * @param text
     * @return
     */
    static String strConvertPinyin(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        StringBuilder sbPinyin = new StringBuilder();
        char[] texts = text.toCharArray();
        for (int i = 0; i < texts.length; i++) {
            sbPinyin.append(Pinyin.toPinyin(texts[i]));
        }
        return sbPinyin.toString();
    }

    static final String[] NAVI = {"DAIWOQU", "WOXIANGQU", "ZAINA", "WOYAOQU", "WOXIANGQU"};
    static final String[] GO_NAVI = {"DAOHANG"};

    public interface PointMatchListener {
        void noMatch();

        void match(NaviBean naviBean, boolean flag);

        void goNavi();
    }
}
