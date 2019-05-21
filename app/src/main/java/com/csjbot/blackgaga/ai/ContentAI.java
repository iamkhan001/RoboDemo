package com.csjbot.blackgaga.ai;

import android.text.TextUtils;

import com.csjbot.blackgaga.feature.content.ContentActivity;
import com.csjbot.promeg.Pinyin;

/**
 * Created by 孙秀艳 on 2018/1/26.
 */

public class ContentAI extends AI<ContentActivity> {
    public static ContentAI newInstance() {
        return new ContentAI();
    }

    @Override
    public Boolean dynamicHandle(Object... o) {
//        String pinyinText = strConvertPinyin(o[0].toString());
//        List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean> activityNames = (List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean>) o[1];
//        if (pinyinText.contains(strConvertPinyin(activityNames.get(0).getName()))) {
////            activity.onAIClickItem();
//            return true;
//        }
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
        for (int i = 0; i < texts.length; i++) {
            sbPinyin.append(Pinyin.toPinyin(texts[i]));
        }
        return sbPinyin.toString();
    }
}
