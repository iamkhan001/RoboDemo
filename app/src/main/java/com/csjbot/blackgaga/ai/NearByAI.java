package com.csjbot.blackgaga.ai;

import com.csjbot.blackgaga.feature.nearbyservice.NearByActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingwc on 2017/10/26.
 */

public class NearByAI extends AI<NearByActivity>{

    public static NearByAI newInstance(){
        return new NearByAI();
    }

    @Override
    public Intent getIntent(String text) {

        String pinyin = strConvertPinyin(text);

        Intent intent = null;

        boolean isStop = false;
        int size = AIParams.datas.size();
        for (int i = 0; i < size;i++){
            int paramSize = AIParams.datas.get(i)[0].length;
            for (int j = 0;j < paramSize;j++){
                if(pinyin.contains(AIParams.datas.get(i)[0][j].toString())){
                    intent = (Intent) AIParams.datas.get(i)[1][0];
                    isStop = true;
                    break;
                }
            }
            if(isStop){
                break;
            }
        }

        return intent;
    }

    @Override
    public void handleIntent(Enum e) {
        super.handleIntent(e);
        Intent intent = (Intent) e;

        switch (intent){
            case FOOD:
                activity.goPoiSearchAct("美食");
                break;
            case SCENIC_SPOT:
                activity.goPoiSearchAct("景点");
                break;
            case HOTEL:
                activity.goPoiSearchAct("酒店");
                break;
            case ENTERTAINMENT:
                activity.goPoiSearchAct("休闲娱乐");
                break;
            case SHARING_BIKES:
                activity.goPoiSearchAct("共享单车");
                break;
            case SUPERMARKET:
                activity.goPoiSearchAct("超市");
                break;
            case ATM:
                activity.goPoiSearchAct("ATM");
                break;
            case TOILET:
                activity.goPoiSearchAct("厕所");
                break;
            case BUDGET_HOTEL:
                activity.goPoiSearchAct("快捷酒店");
                break;
            case INTERNET_BAR:
                activity.goPoiSearchAct("网吧");
                break;
            case METRO:
                activity.goPoiSearchAct("地铁");
                break;
            case GAS_STATION:
                activity.goPoiSearchAct("加油站");
                break;
            default:
                break;
        }
    }

    static class AIParams{
        static final String[] FOOD = {"MEISHI","SHIWU","FANDIAN"};
        static final String[] SCENIC_SPOT = {"JINGDIAN","JINGQU"};
        static final String[] HOTEL = {"JIUDIAN"};
        static final String[] ENTERTAINMENT = {"YULE","XIUXIAN"};
        static final String[] SHARING_BIKES = {"GONGXIANGDANCHE"};
        static final String[] SUPERMARKET = {"CHAOSHI","SHANGCHANG","SHANGDIAN"};
        static final String[] ATM = {"ATM","QUKUANJI","QUQIAN"};
        static final String[] TOILET = {"CESUO","WEISHENGJIAN"};
        static final String[] BUDGET_HOTEL = {"KUAIJIEJIUDIAN"};
        static final String[] INTERNET_BAR = {"WANGBA"};
        static final String[] METRO = {"DITIE"};
        static final String[] GAS_STATION = {"JIAYOUZHAN"};

        static List<Object[][]> datas;

        static {
            datas = new ArrayList<>();
            datas.add(new Object[][]{FOOD,{Intent.FOOD}});
            datas.add(new Object[][]{SCENIC_SPOT,{Intent.SCENIC_SPOT}});
            datas.add(new Object[][]{BUDGET_HOTEL,{Intent.BUDGET_HOTEL}});
            datas.add(new Object[][]{HOTEL,{Intent.HOTEL}});
            datas.add(new Object[][]{ENTERTAINMENT,{Intent.ENTERTAINMENT}});
            datas.add(new Object[][]{SHARING_BIKES,{Intent.SHARING_BIKES}});
            datas.add(new Object[][]{SUPERMARKET,{Intent.SUPERMARKET}});
            datas.add(new Object[][]{ATM,{Intent.ATM}});
            datas.add(new Object[][]{TOILET,{Intent.TOILET}});
            datas.add(new Object[][]{INTERNET_BAR,{Intent.INTERNET_BAR}});
            datas.add(new Object[][]{METRO,{Intent.METRO}});
            datas.add(new Object[][]{GAS_STATION,{Intent.GAS_STATION}});
        }

    }

    public enum Intent{
        FOOD,SCENIC_SPOT,HOTEL
        ,ENTERTAINMENT,SHARING_BIKES
        ,SUPERMARKET,ATM,TOILET
        ,BUDGET_HOTEL,INTERNET_BAR,METRO,GAS_STATION
    }
}
