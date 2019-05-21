package com.csjbot.blackgaga.speechrule;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.csjbot.blackgaga.MainActivity;
import com.csjbot.blackgaga.cart.pactivity.introduce.ProductIntruductionActivity;
import com.csjbot.blackgaga.cart.pactivity.introduce_list.ProductListActivity;
import com.csjbot.blackgaga.feature.aboutus.AboutUsActivity;
import com.csjbot.blackgaga.feature.consult.ConsultActivity;
import com.csjbot.blackgaga.feature.entertainment.EntertainmentActivity;
import com.csjbot.blackgaga.feature.navigation.NaviActivity;
import com.csjbot.blackgaga.feature.nearbyservice.NearByActivity;
import com.csjbot.blackgaga.feature.settings.SettingsActivity;
import com.csjbot.blackgaga.feature.content2.Content2Activity;
import com.csjbot.blackgaga.feature.vipcenter.VipCenterActivity;
import com.csjbot.blackgaga.home.CSJBotHomeActivity;
import com.csjbot.blackgaga.home.ChaoShiHomeActivity;
import com.csjbot.blackgaga.home.FuZhuangHomeActivity;
import com.csjbot.blackgaga.home.HomeActivity;
import com.csjbot.blackgaga.home.JiaDianHomeActivity;
import com.csjbot.blackgaga.home.JiaoYu2HomeActivity;
import com.csjbot.blackgaga.home.JiuDianHomeActivity;
import com.csjbot.blackgaga.home.QiCheHomeActivity;
import com.csjbot.blackgaga.home.ShangShaHomeActivity;
import com.csjbot.blackgaga.home.ShiPinHomeActivity;
import com.csjbot.blackgaga.home.ShuiWuHomeActivity3;
import com.csjbot.blackgaga.home.YaoDianHomeActivity;
import com.csjbot.blackgaga.home.YinHangHomeActivity;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.promeg.Pinyin;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingwc on 2017/11/3.
 */

public class ChatControl {

    public static String ACTION = "action";

    private Activity activity;

    private List<String[]> intercepts = new ArrayList<>();

    /**
     * 数据源
     */
    public static class DataSource{

        // 话术配置数据类
        private static SpeechRule data;

        // activity class 集合
        private static List<Class> activitys;

        static{
            activitys = new ArrayList<>();
            activitys.add(MainActivity.class);
            activitys.add(HomeActivity.class);
            activitys.add(VipCenterActivity.class);
            activitys.add(ProductListActivity.class);
            activitys.add(ProductIntruductionActivity.class);
            activitys.add(ConsultActivity.class);
            activitys.add(NaviActivity.class);
            activitys.add(NearByActivity.class);
            activitys.add(EntertainmentActivity.class);
            activitys.add(AboutUsActivity.class);
            activitys.add(SettingsActivity.class);
            activitys.add(ChaoShiHomeActivity.class);
            activitys.add(CSJBotHomeActivity.class);
            activitys.add(JiuDianHomeActivity.class);
            activitys.add(YinHangHomeActivity.class);
            activitys.add(JiaoYu2HomeActivity.class);
            activitys.add(FuZhuangHomeActivity.class);
            activitys.add(JiaDianHomeActivity.class);
            activitys.add(QiCheHomeActivity.class);
            activitys.add(ShangShaHomeActivity.class);
            activitys.add(ShiPinHomeActivity.class);
            activitys.add(ShuiWuHomeActivity3.class);
            activitys.add(YaoDianHomeActivity.class);
            activitys.add(Content2Activity.class);
        }
    }

    /**
     * 工具
     */
    public static class Utils{
        /**
         * 汉字转拼音方法
         * @param text
         * @return
         */
        public static String strConvertPinyin(String text){
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

    public static ChatControl newInstance(Activity activity){
        return new ChatControl(activity);
    }

    ChatControl(Activity activity){
        this.activity = activity;
        readCustomData();
    }

    /**
     * 如果数据源为空则读取数据
     */
    private void readCustomData() {
        new Thread(() -> {
            if(DataSource.data == null) {
                Gson gson = new Gson();
                DataSource.data = gson.fromJson(getJson("speech.json"), SpeechRule.class);
                CsjlogProxy.getInstance().info(new Gson().toJson(DataSource.data));
            }
        }).start();
    }

    /**
     * 从Assets目录中读取speech.json数据
     * @param fileName
     * @return
     */
    public String getJson(String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = activity.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 处理全局问答
     * @param userText
     * @return
     */
    public String handleGlobal(String userText){
        if (TextUtils.isEmpty(userText)) {
            return null;
        }
        String text = Utils.strConvertPinyin(userText);
        CsjlogProxy.getInstance().info("text:"+text);
        if(DataSource.data == null ||
                DataSource.data.globalSpeech == null
                || DataSource.data.globalSpeech.contents == null){
            return null;
        }
        List<SpeechRule.Content> contents = DataSource.data.globalSpeech.contents;
        if(contents != null && contents.size()>0){
            for (SpeechRule.Content content : contents){
                if(text.contains(Utils.strConvertPinyin(content.text))){
                    if(!TextUtils.isEmpty(content.answerText)){
                        return content.answerText;
                    }
                    if(!TextUtils.isEmpty(content.action)){
                        execGlobalAction(content.action,content.target);
                        return ACTION;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 执行动作
     * @param action
     * @param target
     */
    private void execGlobalAction(String action,String target){
        // 是否拦截动作
        if(isInterceptAction(action,target)){
            return;
        }
        switch (action){
            case SpeechRule.Action.BACK:
                // 返回
                activity.finish();
                break;
            case SpeechRule.Action.GO:
                // 跳转
                activity.startActivity(new Intent(activity,getActClass(target)));
                break;
        }
    }

    /**
     * 设置拦截动作信息
     * @param page 页面
     * @param action 动作
     * @param target 目标
     */
    public void addIntercept(String page,String action,String target){
        intercepts.add(new String[]{page,action,target});
    }

    /**
     * 判断是否拦截方法
     * @param action 动作
     * @param target 目标
     * @return
     */
    private boolean isInterceptAction(String action,String target){
        if(intercepts == null || intercepts.size() == 0){
            return false;
        }
        for (String[] strings : intercepts){
            if(strings[2] != null){
                if (strings[0].equals(activity.getClass().getSimpleName())
                        && strings[1].equals(action) && strings[2].equals(target)) {
                    CsjlogProxy.getInstance().info("已拦截改动作:" + action);
                    return true;
                }
            }else{
                if (strings[0].equals(activity.getClass().getSimpleName())
                        && strings[1].equals(action) && strings[2] == target) {
                    CsjlogProxy.getInstance().info("已拦截改动作:" + action);
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 获取当前子页面初始化文本
     * @return
     */
    public String initChild(){
        if(DataSource.data == null || DataSource.data.childSpeeches == null){
            return null;
        }
        List<SpeechRule.ChildSpeech> childSpeeches = DataSource.data.childSpeeches;
        if(childSpeeches != null && childSpeeches.size()>0) {
            for (SpeechRule.ChildSpeech childSpeech : childSpeeches) {
                if(childSpeech.page.equals(activity.getClass().getSimpleName())){
                    return childSpeech.initText;
                }
            }
        }
        return null;
    }

    /**
     * 处理子页面语音问答
     * @param userText
     * @return
     */
    public String handleChild(String userText){
        if (TextUtils.isEmpty(userText)) {
            return null;
        }
        String text = Utils.strConvertPinyin(userText);
        if(DataSource.data == null && DataSource.data.childSpeeches == null){
            return null;
        }
        List<SpeechRule.ChildSpeech> childSpeeches = DataSource.data.childSpeeches;
        if(childSpeeches != null && childSpeeches.size()>0){
            for (SpeechRule.ChildSpeech childSpeech : childSpeeches){
                if(childSpeech.page.equals(activity.getClass().getSimpleName())){
                    for (SpeechRule.Content content : childSpeech.contents){
                        if(text.contains(Utils.strConvertPinyin(content.text))){
                            if(!TextUtils.isEmpty(content.answerText)){
                                return content.answerText;
                            }
                            if(!TextUtils.isEmpty(content.action)){
                                execChildAction(content.action,content.target);
                                return ACTION;
                            }
                            break;
                        }
                    }
                    break;
                }

            }
        }
        return null;
    }

    /**
     * 执行子页面语音控制动作
     * @param action
     * @param target
     */
    private void execChildAction(String action,String target){
        switch (action){
            case SpeechRule.Action.BACK:
                // 返回
                activity.finish();
                break;
            case SpeechRule.Action.GO:
                // 跳转
                activity.startActivity(new Intent(activity,getActClass(target)));
                break;
        }
    }


    /**
     * 获取匹配的Activity信息
     * @param target
     * @return
     */
    private Class getActClass(String target){
        Class c = null;
        for (Class className : DataSource.activitys){
            if(className.getSimpleName().equals(target)){
                c = className;
                break;
            }
        }
        return c;
    }

}
