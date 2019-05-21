package com.csjbot.blackgaga.ai;

import com.csjbot.blackgaga.feature.entertainment.EntertainmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingwc on 2017/10/26.
 */

public class EntertainmentAI extends AI<EntertainmentActivity>{

    public static EntertainmentAI newInstance(){
        return new EntertainmentAI();
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
            case MUSIC:
                activity.music();
                break;
            case STORY:
                activity.storytelling();
                break;
            case DANCE:
                activity.dance();
                break;
            default:
                break;
        }
    }

    static class AIParams{
        static final String[] MUSIC = {"YINYUE","CHANGSHOUGE","CHANGGE","LAISHOUGE"};
        static final String[] STORY = {"GUSHI"};
        static final String[] DANCE = {"TIAOWU","TIAOGEWU"};

        static List<Object[][]> datas;

        static {
            datas = new ArrayList<>();
            datas.add(new Object[][]{MUSIC,{Intent.MUSIC}});
            datas.add(new Object[][]{STORY,{Intent.STORY}});
            datas.add(new Object[][]{DANCE,{Intent.DANCE}});
        }

    }

    public enum Intent{
        MUSIC,STORY,DANCE
    }
}
