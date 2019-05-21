package com.csjbot.blackgaga.ai;

import com.csjbot.blackgaga.feature.nearbyservice.NearByAirportActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  Wql , 2018/2/27 15:58
 */

public class NearByAirportAI extends AI<NearByAirportActivity> {

    public static NearByAirportAI newInstance() {
        return new NearByAirportAI();
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
            case FOOD:
                activity.goPoiSearchAct("美食");
                break;
            case SCENIC_SPOT:
                activity.goPoiSearchAct("景点");
                break;
            case HOTEL:
                activity.goPoiSearchAct("酒店");
                break;
            case CURRENCY_EXCHANGE:
                activity.goPoiSearchAct("货币兑换");
                break;
            case INFORMATION_DESK:
                activity.goPoiSearchAct("咨询台");
                break;
            case DUTY_FREE_STORE:
                activity.goPoiSearchAct("免税店");
                break;
            case ATM:
                activity.goPoiSearchAct("ATM");
                break;
            case BUS:
                activity.goPoiSearchAct("大巴");
                break;
            case TAXI:
                activity.goPoiSearchAct("出租车");
                break;
            case METRO:
                activity.goPoiSearchAct("地铁");
                break;
            default:
                break;
        }
    }

    static class AIParams {
        static final String[] FOOD = {"MEISHI", "SHIWU", "FANDIAN"};//美食
        static final String[] SCENIC_SPOT = {"JINGDIAN", "JINGQU"};//景点
        static final String[] HOTEL = {"JIUDIAN"};//酒店
        static final String[] CURRENCY_EXCHANGE = {"HUOBI", "DUIHUAN"};//货币兑换
        static final String[] INFORMATION_DESK = {"ZIXUNTAI"};//咨询台
        static final String[] DUTY_FREE_STORE = {"MIANSHUIDIAN"};//免税店
        static final String[] ATM = {"ATM", "QUKUANJI", "QUQIAN"};//ATM
        static final String[] BUS = {"DABA"};//大巴
        static final String[] TAXI = {"CHUZUCHE"};//出租车
        static final String[] METRO = {"DITIE"};//地铁


        static List<Object[][]> datas;

        static {
            datas = new ArrayList<>();
            datas.add(new Object[][]{FOOD, {Intent.FOOD}});
            datas.add(new Object[][]{SCENIC_SPOT, {Intent.SCENIC_SPOT}});
            datas.add(new Object[][]{HOTEL, {Intent.HOTEL}});
            datas.add(new Object[][]{CURRENCY_EXCHANGE, {Intent.CURRENCY_EXCHANGE}});
            datas.add(new Object[][]{INFORMATION_DESK, {Intent.INFORMATION_DESK}});
            datas.add(new Object[][]{DUTY_FREE_STORE, {Intent.DUTY_FREE_STORE}});
            datas.add(new Object[][]{ATM, {Intent.ATM}});
            datas.add(new Object[][]{BUS, {Intent.BUS}});
            datas.add(new Object[][]{TAXI, {Intent.TAXI}});
            datas.add(new Object[][]{METRO, {Intent.METRO}});

        }

    }

    public enum Intent {
        FOOD,
        SCENIC_SPOT,
        HOTEL,
        CURRENCY_EXCHANGE,
        INFORMATION_DESK,
        DUTY_FREE_STORE,
        ATM,
        BUS,
        TAXI,
        METRO,
    }
}
