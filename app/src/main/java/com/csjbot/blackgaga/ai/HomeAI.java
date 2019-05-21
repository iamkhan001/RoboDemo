package com.csjbot.blackgaga.ai;

import com.csjbot.blackgaga.feature.navigation.NaviAction;
import com.csjbot.blackgaga.home.HomeActivity;
import com.csjbot.blackgaga.localbean.NaviBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 负责HomeActivity的语音识别结果的处理分析
 * Created by jingwc on 2017/10/25.
 */

public class HomeAI extends AI<HomeActivity> {

    public static HomeAI newInstance() {
        return new HomeAI();
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
                    if (intent == Intent.NAVI) {
                        List<NaviBean> data = NaviAction.getInstance().getData();
                        if (data == null || data.size() == 0) break;
                        for (NaviBean naviBean : data) {
                            String name = naviBean.getName();
                            if (pinyin.contains(name)) {
                                activity.goNaviActAI(naviBean, true);
                                intent = Intent.NULL;
                                return intent;
                            }
                        }

                        // 没有匹配到点
                        activity.goNaviActAI(null, false);
                    }
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


    /**
     * 对语音的意图进行相对应的操作
     *
     * @param e 意图
     */
    @Override
    public void handleIntent(Enum e) {
        super.handleIntent(e);
        Intent intent = (Intent) e;
        switch (intent) {
            case PRODUCT:
                activity.goProductAct();
                break;
            case ENTERTAINMENT:
//                activity.goEntertainmentAct();
                break;
            case VIPCENTER:
                activity.goVIPCenterAct();
                break;
            case NAVI:
//                activity.goNaviAct();
                break;
            case QUERY:
//                activity.goSearchAct();
                break;
            case ABOUT:
//                activity.goAboutAct();
                break;
            case COUPON:
//                activity.goCouponAct();
                break;
            case NEARBY:
                activity.goNearByAct();
                break;
            default:
                break;
        }
    }

    static class AIParams {

        static final String[] PRODUCT = {"CHANPIN", "MAIDONGXI", "WOXIANGMAI", "WOYAOMAI"};
        static final String[] ENTERTAINMENT = {"YULE", "TIAOWU", "CHANGGE", "GUSHI"};
        static final String[] NAVI = {"DAOHANG", "DAIWOQU", "WOXIANGQU", "DAON", "QU", "ZAINA"};
        static final String[] NEARBY = {"ZHOUBIAN", "FUJIN"};
        static final String[] VIPCENTER = {"HUIYUANZHONGXIN"};
        static final String[] QUERY = {"CHAXUN"};
        static final String[] COUPON = {"YOUHUIQUAN"};
        static final String[] ABOUT = {"GUANYU"};

        static List<Object[][]> datas;

        static {
            datas = new ArrayList<>();
            datas.add(new Object[][]{PRODUCT, {Intent.PRODUCT}});
//            datas.add(new Object[][]{ENTERTAINMENT,{Intent.ENTERTAINMENT}});
            datas.add(new Object[][]{NAVI, {Intent.NAVI}});
            datas.add(new Object[][]{NEARBY, {Intent.NEARBY}});
            datas.add(new Object[][]{VIPCENTER, {Intent.VIPCENTER}});
//            datas.add(new Object[][]{QUERY,{Intent.QUERY}});
//            datas.add(new Object[][]{COUPON,{Intent.COUPON}});
//            datas.add(new Object[][]{ABOUT,{Intent.ABOUT}});
        }
    }

    public enum Intent {
        PRODUCT, ENTERTAINMENT, NAVI, NEARBY, VIPCENTER, QUERY, COUPON, ABOUT, NULL
    }
}
