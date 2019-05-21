package com.csjbot.blackgaga.ai;

import com.csjbot.blackgaga.feature.search.SearchActivity2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingwc on 2017/10/26.
 *
 * 机场   咨询服务
 */

public class SearchAI2 extends AI<SearchActivity2> {

    public static SearchAI2 newInstance() {
        return new SearchAI2();
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
            case PASSENGER_SERVICE:
                activity.passengerservice();
                break;
            case OPENING_HOURS:
                activity.openinghours();
                break;
            case SERVICE_NOTICE:
                activity.servicenotice();
                break;
            case THE_CARD:
                activity.thecard();
                break;
            default:
                break;
        }
    }


    static class AIParams {

        static String[] PASSENGER_SERVICE = {"LUKEFUWU"};
        static String[] OPENING_HOURS = {"KAIFANGSHIJIAN"};
        static String[] SERVICE_NOTICE = {"FUWUXUZHI"};
        static String[] THE_CARD = {"XIANGGUANGKAPIAN"};


        static List<Object[][]> datas;

        static {
            datas = new ArrayList<>();
            datas.add(new Object[][]{PASSENGER_SERVICE, {Intent.PASSENGER_SERVICE}});
            datas.add(new Object[][]{OPENING_HOURS, {Intent.OPENING_HOURS}});
            datas.add(new Object[][]{SERVICE_NOTICE, {Intent.SERVICE_NOTICE}});
            datas.add(new Object[][]{THE_CARD, {Intent.THE_CARD}});

        }

    }

    public enum Intent {
        PASSENGER_SERVICE, OPENING_HOURS, SERVICE_NOTICE, THE_CARD
    }
}
