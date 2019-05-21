package com.csjbot.blackgaga.ai;

import com.csjbot.blackgaga.feature.product.order.OrderActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingwc on 2017/10/26.
 */

public class OrderAI extends AI<OrderActivity>{

    public static OrderAI newInstance(){
        return new OrderAI();
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
            case BACK:
            case HOME:
            case CANCEL_ORDER:
                activity.getOrderFragment().clickCancel();
                break;
            default:
                break;
        }
    }

    static class AIParams{
        static final String[] CANCEL_ORDER = {"QUXIAODINGDAN"};
        static final String[] BACK = {"FANHUI","TUICHU"};
        static final String[] HOME = {"ZHUYE","SHOUYE"};

        static List<Object[][]> datas;

        static {
            datas = new ArrayList<>();
            datas.add(new Object[][]{CANCEL_ORDER,{Intent.CANCEL_ORDER}});
            datas.add(new Object[][]{BACK,{Intent.BACK}});
            datas.add(new Object[][]{HOME,{Intent.HOME}});
        }

    }

    public enum Intent{
        CANCEL_ORDER,BACK,HOME
    }
}
