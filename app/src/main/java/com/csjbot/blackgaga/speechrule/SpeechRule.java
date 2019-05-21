package com.csjbot.blackgaga.speechrule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingwc on 2017/12/3.
 */

public class SpeechRule {

    // 全局话术规则
    GlobalSpeech globalSpeech;

    // 子页面话术规则集合
    List<ChildSpeech> childSpeeches = new ArrayList<>();

    static class GlobalSpeech {
        List<Content> contents = new ArrayList<>();
    }

    static class ChildSpeech{
        // 当前页
        String page;
        // 初始文本
        String initText;

        List<Content> contents = new ArrayList<>();

    }

    static class Content{
        // 用户问题
        String text;
        // 机器人答案
        String answerText;
        // 动作
        String action;
        // 动作的目标页
        String target;
    }

    public static class Action{
        // 返回
        public static final String BACK = "back";
        // 跳转
        public static final String GO = "go";
    }
}
