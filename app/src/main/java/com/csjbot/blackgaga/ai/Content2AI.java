package com.csjbot.blackgaga.ai;

import android.text.TextUtils;

import com.csjbot.blackgaga.feature.content2.Content2Activity;
import com.csjbot.blackgaga.model.http.bean.ContentTypeBean;
import com.csjbot.promeg.Pinyin;


import java.util.List;

/**
 * Created by 孙秀艳 on 2018/1/26.
 */

public class Content2AI extends AI<Content2Activity> {
    public static Content2AI newInstance() {
        return new Content2AI();
    }

    @Override
    public Boolean dynamicHandle(Object... o) {
        String pinyinText = strConvertPinyin(o[0].toString());
        List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean> activityNames = (List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean>) o[1];
        for (int i = 0; i < activityNames.size(); i++) {
            if (pinyinText.contains(strConvertPinyin(activityNames.get(i).getName()))) {
                activity.onAIClickItem(i);
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
    String strConvertPinyin(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        StringBuilder sbPinyin = new StringBuilder();
        char[] texts = text.toCharArray();
        for (char text1 : texts) {
            sbPinyin.append(Pinyin.toPinyin(text1));
        }
        return sbPinyin.toString();
    }
}
