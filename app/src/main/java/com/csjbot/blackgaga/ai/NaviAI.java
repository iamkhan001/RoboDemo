package com.csjbot.blackgaga.ai;

import android.util.Log;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.feature.navigation.NaviActivity;
import com.csjbot.blackgaga.feature.navigation.TaskStatusManager;
import com.csjbot.blackgaga.localbean.NaviBean;
import com.csjbot.blackgaga.util.BlackgagaLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingwc on 2017/10/26.
 */

public class NaviAI extends AI<NaviActivity> {

    public static NaviAI newInstance() {
        return new NaviAI();
    }

    public boolean isExistIntent(String text) {
        String pinyin = strConvertPinyin(text);
        Intent intent = null;
        int size = AIParams.datas.size();
        for (int i = 0; i < size; i++) {
            int paramSize = AIParams.datas.get(i)[0].length;
            for (int j = 0; j < paramSize; j++) {
                if (pinyin.contains(AIParams.datas.get(i)[0][j].toString())) {
                    intent = (Intent) AIParams.datas.get(i)[1][0];
                    if (intent == Intent.START_GUIDE || intent == Intent.CANCEL
                            || intent == Intent.BACK_WELCOME || intent == Intent.MAP_SETTING) {
                        return true;
                    }
                    if (intent == Intent.GO_SINGLE){
                        if (activity.naviBeanList == null || activity.naviBeanList.size() == 0)
                            return false;
                        for (NaviBean naviBean : activity.naviBeanList) {
                            String pinyinPointName = strConvertPinyin(naviBean.getName()).trim();
                            String pinyinNaviName = strConvertPinyin(naviBean.getNickName()).trim();
                            if (pinyin.contains(pinyinPointName) || pinyin.contains(pinyinNaviName)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public Intent getIntent(String text) {

        String pinyin = strConvertPinyin(text);

        Intent intent = null;

        boolean isStop = false;
        int size = AIParams.datas.size();
        for (int i = 0; i < size; i++) {
            int paramSize = AIParams.datas.get(i)[0].length;
            for (int j = 0; j < paramSize; j++) {
                if (pinyin.contains(AIParams.datas.get(i)[0][j].toString())) {
                    intent = (Intent) AIParams.datas.get(i)[1][0];
                    if (intent == Intent.GO_SINGLE) {
                        // 无法解决这种情况：
                        // 如果设置了 研发、研发部，而语音是我要去 研发部
                        // 那么 研发和研发部都能匹配上。。。
                        if (activity.naviBeanList == null || activity.naviBeanList.size() == 0)
                            break;
                        for (NaviBean naviBean : activity.naviBeanList) {
                            String pinyinPointName = strConvertPinyin(naviBean.getName()).trim();
                            String pinyinNaviName = strConvertPinyin(naviBean.getNickName()).trim();
                            if (pinyin.contains(pinyinPointName) || pinyin.contains(pinyinNaviName)) {
                                BlackgagaLogger.debug("语音匹配，单项导览匹配成功");
                                goSingle(naviBean);
                                break;
                            }
                        }
                    }
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }

        return intent;
    }

    private void goSingle(NaviBean naviBean) {
        if (activity.guideSingle.workStatus != TaskStatusManager.START &&
                activity.guideAllTask.workStatus != TaskStatusManager.START) {
            activity.naviAIConfirm(naviBean);
//            activity.showInfo(naviBean);
//            activity.isAITalk = true;
//            activity.guideImm();
        } else {
            activity.runOnUiThread(() -> activity.speak(R.string.working_ing, true));
        }
    }

    @Override
    public void handleIntent(Enum e) {
        super.handleIntent(e);
        Intent intent = (Intent) e;


        switch (intent) {
            case START_GUIDE:
                BlackgagaLogger.debug("语音匹配，开始一键导览");
                if (activity.guideAllTask.workStatus != TaskStatusManager.START &&
                        activity.guideSingle.workStatus != TaskStatusManager.START) {
                    activity.isAITalk = true;
                    activity.runOnUiThread(() -> activity.guide());
                }
                break;
            case BACK_WELCOME:
                BlackgagaLogger.debug("语音匹配，返回迎宾点");
                activity.backWelCome();
                break;
            case GO_SINGLE:
                break;
            case MAP_SETTING:
                BlackgagaLogger.debug("语音匹配，地图设置");
                activity.manageMap();
                break;
            case CANCEL:
                BlackgagaLogger.debug("语音匹配，取消任务");
                int workState = activity.workType;

                if (workState == NaviActivity.GUIDE_SINGLE) {
                    activity.guideImm();
                } else if (workState == NaviActivity.GUIDE_ALL) {
                    activity.cancelTask();
                }
                break;
            default:
                break;
        }
    }

    static class AIParams {
        static final String[] GUIDE = {"KAISHIDAOLAN", "DAOLAN", "KAISHI", "DAOYIN"};
        static final String[] BACK = {"FANHUIYING", "HUIQU"};
        static final String[] GO_SINGLE = {"DAO", "QU", "WOYAOQU", "WOYAODAO","ZAINA"};
        static final String[] MAP_SETTING = {"SHEZHIDITU", "DAORUDITU", "CHARUDITU", "GUANLIDITU", "DITUGUANLI", "SHEZHI"};
        static final String[] CANCEL = {"QUXIAODAOHANG,QUXIAO,FANHUI,JIESHU,TINGZHI,ZANTING"};

        static List<Object[][]> datas;

        static {
            datas = new ArrayList<>();
            datas.add(new Object[][]{GUIDE, {Intent.START_GUIDE}});
            datas.add(new Object[][]{BACK, {Intent.BACK_WELCOME}});
            datas.add(new Object[][]{GO_SINGLE, {Intent.GO_SINGLE}});
            datas.add(new Object[][]{MAP_SETTING, {Intent.MAP_SETTING}});
            datas.add(new Object[][]{CANCEL, {Intent.CANCEL}});
        }

    }

    public enum Intent {
        START_GUIDE, BACK_WELCOME, GO_SINGLE, MAP_SETTING, CANCEL
    }
}
