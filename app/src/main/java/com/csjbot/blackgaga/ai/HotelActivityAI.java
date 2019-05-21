package com.csjbot.blackgaga.ai;


import com.csjbot.blackgaga.feature.recent.activity.HotelActivitiesActivity;
import com.csjbot.blackgaga.feature.recent.entity.HotelActivityBean;

import java.util.List;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/3/1.
 * @Package_name: BlackGaGa
 */

public class HotelActivityAI extends AI<HotelActivitiesActivity> {
    private List<HotelActivityBean> been;
    public boolean isGet;

    public void setBean(List<HotelActivityBean> list) {
        this.been = list;
    }

    @Override
    public Enum getIntent(String text) {
        isGet = false;
        String pinyin = strConvertPinyin(text.replaceAll("\\s*", ""));
        for (int i = 0; i < been.size(); i++) {
            List<HotelActivityBean.HotelResultBean> list = been.get(i).getBean();
            for (int j = 0; j < list.size(); j++) {
                String[] _arrays = list.get(j).getKeyWord().replaceAll("\\s*", "").split(",");
                for (int k = 0; k < _arrays.length; k++) {
                    String pingwood = strConvertPinyin(_arrays[k]);
                    if (pinyin.contains(pingwood)) {
                        activity.speakMsg(been.get(i), j);
                        isGet = true;
                        break;
                    }
                }
                if (isGet) {
                    break;
                }
            }
            if (isGet) {
                break;
            }
        }
        return null;
    }
}
