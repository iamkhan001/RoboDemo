package com.csjbot.blackgaga.localbean;


import com.csjbot.blackgaga.model.tcp.bean.Position;
import com.csjbot.blackgaga.util.GsonUtils;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jingwc on 2017/9/21.
 */

public class NaviBean implements Serializable {
    public String id;

    protected Position pos;

    protected String name;//导航点名称

    protected String nickName;//导航点别称

    protected String description;

    protected boolean isInputOrFile;//到点讲解是手动输入还是选择文件，true手动输入 false选中文件

    protected String descContent;//到点讲解内容

    protected String musicPath;

    protected String descInRunning;

    protected String descInRunningContent;//途中讲解内容

    protected boolean isOpenMusic = false;//是否选择背景音

    protected boolean isOpenSpeak = false;//是否选择途中讲解

    protected boolean isOpenImage = false;//是否选择到点后动画/图片

    protected boolean isPlayVideo = false;//导航过程中是否播放视频

    protected int waitTime;//讲解等待豪秒数

    protected boolean isInputOrFileStart;//出发前提示 手动输入还是选择文件

    protected String startTipContent;//出发前提示内容

    protected boolean isInputOrFileArrive;//到点后讲解 手动输入还是选择文件

    protected String arriveTipContent;//到点后讲解提示内容

    protected String startTip;

    protected String arriveTip;

    protected String imagePath;//图片路径

    protected float translationX;

    protected float translationY;

    public int left, right, bottom, top;

    public float getTranslationX() {
        return translationX;
    }

    public void setTranslationX(float translationX) {
        this.translationX = translationX;
    }

    public float getTranslationY() {
        return translationY;
    }

    public void setTranslationY(float translationY) {
        this.translationY = translationY;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isInputOrFile() {
        return isInputOrFile;
    }

    public void setInputOrFile(boolean inputOrFile) {
        isInputOrFile = inputOrFile;
    }

    public String getDescContent() {
        return descContent;
    }

    public void setDescContent(String descContent) {
        this.descContent = descContent;
    }

    public String getDescInRunningContent() {
        return descInRunningContent;
    }

    public void setDescInRunningContent(String descInRunningContent) {
        this.descInRunningContent = descInRunningContent;
    }

    public String toJson() {
        return pos.toJson();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescInRunning(String descInRunning) {
        this.descInRunning = descInRunning;
    }

    public String getDescInRunning() {
        return descInRunning;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public boolean isOpenMusic() {
        return isOpenMusic;
    }

    public boolean isOpenSpeak(){
        return isOpenSpeak;
    }

    public boolean isOpenImage() {
        return isOpenImage;
    }

    public void setOpenImage(boolean openImage) {
        isOpenImage = openImage;
    }

    public boolean isPlayVideo() {
        return isPlayVideo;
    }

    public void setPlayVideo(boolean playVideo) {
        isPlayVideo = playVideo;
    }

    public void setOpenMusic(boolean openMusic) {
        isOpenMusic = openMusic;
    }

    public void setOpenSpeak(boolean openSpeak) {
        isOpenSpeak = openSpeak;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public boolean isInputOrFileStart() {
        return isInputOrFileStart;
    }

    public void setInputOrFileStart(boolean inputOrFileStart) {
        isInputOrFileStart = inputOrFileStart;
    }

    public String getStartTipContent() {
        return startTipContent;
    }

    public void setStartTipContent(String startTipContent) {
        this.startTipContent = startTipContent;
    }

    public boolean isInputOrFileArrive() {
        return isInputOrFileArrive;
    }

    public void setInputOrFileArrive(boolean inputOrFileArrive) {
        isInputOrFileArrive = inputOrFileArrive;
    }

    public String getArriveTipContent() {
        return arriveTipContent;
    }

    public void setArriveTipContent(String arriveTipContent) {
        this.arriveTipContent = arriveTipContent;
    }

    public String getStartTip() {
        return startTip;
    }

    public void setStartTip(String startTip) {
        this.startTip = startTip;
    }

    public String getArriveTip() {
        return arriveTip;
    }

    public void setArriveTip(String arriveTip) {
        this.arriveTip = arriveTip;
    }

    public static List<NaviBean> getNaviBeanList() {
        String json = SharedPreUtil.getString(SharedKey.GUIDE_NAME, SharedKey.GUIDE_KEY);
        List<NaviBean> list = GsonUtils.jsonToObject(json, new TypeToken<List<NaviBean>>() {
        }.getType());

        if(list == null || list.size() == 0) return null;

        return list;
    }
}
