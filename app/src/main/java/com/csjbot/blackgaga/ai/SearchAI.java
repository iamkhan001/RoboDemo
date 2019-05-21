package com.csjbot.blackgaga.ai;

import com.csjbot.blackgaga.feature.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingwc on 2017/10/26.
 */

public class SearchAI extends AI<SearchActivity> {

    public static SearchAI newInstance(){
        return new SearchAI();
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
            case CUSTOMER_SERVICE:
                activity.customerService();
                break;
            case ABOUT_VIP:
                activity.aboutVip();
                break;
            case DISCOUNT_SERVICE:
                activity.discountService();
                break;
            case HELP_CENTER:
                activity.helpCenter();
                break;
            case JOIN_SERVICE:
                activity.joinService();
                break;
            default:
                break;
        }
    }


    static class AIParams{

        static String[] CUSTOMER_SERVICE = {"SHOUHOU"};
        static String[] ABOUT_VIP = {"HUIYUAN","GUANYU"};
        static String[] DISCOUNT_SERVICE = {"YOUHUI"};
        static String[] HELP_CENTER = {"BANGZHU"};
        static String[] JOIN_SERVICE = {"JIAMENG"};

        static List<Object[][]> datas;

        static {
            datas = new ArrayList<>();
            datas.add(new Object[][]{CUSTOMER_SERVICE,{Intent.CUSTOMER_SERVICE}});
            datas.add(new Object[][]{ABOUT_VIP,{Intent.ABOUT_VIP}});
            datas.add(new Object[][]{DISCOUNT_SERVICE,{Intent.DISCOUNT_SERVICE}});
            datas.add(new Object[][]{HELP_CENTER,{Intent.HELP_CENTER}});
            datas.add(new Object[][]{JOIN_SERVICE,{Intent.JOIN_SERVICE}});
        }

    }

    public enum Intent{
        CUSTOMER_SERVICE, ABOUT_VIP, DISCOUNT_SERVICE, HELP_CENTER, JOIN_SERVICE
    }
}
