package com.csjbot.blackgaga.ai;

import com.csjbot.blackgaga.feature.navigation.NaviGuideCommentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiasuhuei321 on 2017/12/28.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class NaviGuideCommentAI extends AI<NaviGuideCommentActivity> {
    public static NaviGuideCommentAI newInstance() {
        return new NaviGuideCommentAI();
    }

    @Override
    public Intent getIntent(String text) {

        Intent intent = null;
        String pinyin = strConvertPinyin(text);

        boolean isStop = false;
        int size = AIParams.datas.size();
        for (int i = 0; i < size; i++) {
            int paramSize = AIParams.datas.get(i)[0].length;
            for (int j = 0; j < paramSize; j++) {
                if (pinyin.contains(AIParams.datas.get(i)[0][j].toString())) {
                    intent = (Intent) AIParams.datas.get(i)[1][0];
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

    @Override
    public void handleIntent(Enum e) {
        super.handleIntent(e);
        Intent intent = (Intent) e;

        switch (intent) {
            case MANYI:
                activity.well();
                break;
            case NOMANYI:
                activity.bad();
                break;
            case VERYMANYI:
                activity.veryWell();
                break;
            default:
                break;
        }
    }


    static class AIParams {

        static String[] MANYI = {"MANYI"};
        static String[] NOMANYI = {"BUMANYI"};
        static String[] VERYMANYI = {"FEICHANGMANYI"};
        static List<Object[][]> datas;

        static {
            datas = new ArrayList<>();
            datas.add(new Object[][]{NOMANYI, {Intent.NOMANYI}});
            datas.add(new Object[][]{VERYMANYI, {Intent.VERYMANYI}});
            datas.add(new Object[][]{MANYI, {Intent.MANYI}});
        }

    }

    public enum Intent {
        MANYI, NOMANYI, VERYMANYI
    }
}
