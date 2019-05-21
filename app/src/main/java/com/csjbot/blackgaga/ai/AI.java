package com.csjbot.blackgaga.ai;

import android.app.Activity;
import android.text.TextUtils;

import com.csjbot.promeg.Pinyin;


/**
 * Created by jingwc on 2017/10/25.
 */

public class AI<T extends Activity> {

    T activity;

    public Enum getIntent(String text){
        return null;
    }

    public void initAI(T activity){
       this.activity = activity;
    }

    public void handleIntent(Enum e){

    }

    public Object dynamicHandle(Object... o){
        return null;
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
