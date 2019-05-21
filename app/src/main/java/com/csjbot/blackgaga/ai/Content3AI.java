package com.csjbot.blackgaga.ai;

import android.text.TextUtils;

import com.csjbot.blackgaga.feature.content3.Content3Activity;
import com.csjbot.blackgaga.model.http.bean.ContentTypeBean;
import com.csjbot.promeg.Pinyin;

import java.util.List;

/**
 * Created by 孙秀艳 on 2018/1/26.
 */

public class Content3AI extends AI<Content3Activity> {
    public static Content3AI newInstance() {
        return new Content3AI();
    }

    @Override
    public Boolean dynamicHandle(Object... o) {
        String pinyinText = strConvertPinyin(o[0].toString());
        List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean> messageTypeList = (List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean>)o[1];
        for (int i = 0;i < messageTypeList.size();i++){
            if(pinyinText.contains(strConvertPinyin(messageTypeList.get(i).getName()))){
                activity.onClickItem(i, 0);
                return true;
            } else {
                List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean.MessagesBean> messageList = messageTypeList.get(i).getMessages();
                if (messageList != null && messageList.size() > 0) {
                    for (int j=0; j<messageList.size(); j++) {
                        if (pinyinText.contains(strConvertPinyin(messageList.get(j).getName()))) {
                            activity.onClickItem(i, j);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 汉字转拼音方法
     * @param text
     * @return
     */
    String strConvertPinyin(String text){
        if(TextUtils.isEmpty(text)){
            return "";
        }
        StringBuilder sbPinyin = new StringBuilder();
        char[] texts = text.toCharArray();
        for (int i = 0;i < texts.length;i++){
            sbPinyin.append(Pinyin.toPinyin(texts[i]));
        }
        return sbPinyin.toString();
    }
}
