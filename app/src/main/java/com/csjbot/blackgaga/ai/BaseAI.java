package com.csjbot.blackgaga.ai;

import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingwc on 2017/10/26.
 */

public class BaseAI extends AI<BaseFullScreenActivity> {

    public static BaseAI newInstance(){
        return new BaseAI();
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
            case HOME:
                activity.jumpActivity(HomeActivity.class);
                break;
            case BACK:
                activity.finish();
                break;
            default:
                break;
        }
    }

    static class AIParams{
        static final String[] HOME = {"SHOUYE","ZHUYE"};
        static final String[] BACK = {"FANHUI","TUICHU"};

        static List<Object[][]> datas;

        static {
            datas = new ArrayList<>();
            datas.add(new Object[][]{HOME,{Intent.HOME}});
            datas.add(new Object[][]{BACK,{Intent.BACK}});
        }

    }

    public enum Intent{
        HOME,BACK
    }
}
