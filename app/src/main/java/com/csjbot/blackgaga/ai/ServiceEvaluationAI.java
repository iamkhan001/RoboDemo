package com.csjbot.blackgaga.ai;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.cart.pactivity.evaluate.ServiceEvaluationActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/11/24.
 */

public class ServiceEvaluationAI extends AI<ServiceEvaluationActivity> {
    public static ServiceEvaluationAI newInstance() {
        return new ServiceEvaluationAI();
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
                activity.sendToHttp(activity.getString(R.string.satisfied));
                break;
            case NOMANYI:
                activity.sendToHttp(activity.getString(R.string.dissatisfied));
                break;
            case VERYMANYI:
                activity.sendToHttp(activity.getString(R.string.very_satisfied));
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
            datas.add(new Object[][]{MANYI, {ServiceEvaluationAI.Intent.MANYI}});
            datas.add(new Object[][]{NOMANYI, {ServiceEvaluationAI.Intent.NOMANYI}});
            datas.add(new Object[][]{VERYMANYI, {ServiceEvaluationAI.Intent.VERYMANYI}});
        }

    }

    public enum Intent {
        MANYI, NOMANYI, VERYMANYI
    }
}
