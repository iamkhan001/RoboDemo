package com.csjbot.blackgaga.feature.nearbyservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.ai.NearByAI;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.router.BRouterKey;
import com.csjbot.blackgaga.router.BRouterPath;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiasuhuei321 on 2017/10/17.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

@Route(path = BRouterPath.NEAR_BY_MAIN)
public class NearByActivity extends BaseModuleActivity {

    @BindView(R.id.ll_food)
    LinearLayout mLlFood;
    @BindView(R.id.ll_scenic)
    LinearLayout mLlScenic;
    @BindView(R.id.ll_hotel)
    LinearLayout mLlHotel;
    @BindView(R.id.ll_relax)
    LinearLayout mLlRelax;
    @BindView(R.id.ll_share_bike)
    LinearLayout mLlShareBike;
    @BindView(R.id.ll_supermarket)
    LinearLayout mLlSupermarket;
    @BindView(R.id.ll_atm)
    LinearLayout mLlAtm;
    @BindView(R.id.ll_wc)
    LinearLayout mLlWc;
    @BindView(R.id.ll_fast_hotel)
    LinearLayout mLlFastHotel;
    @BindView(R.id.ll_cyber_bar)
    LinearLayout mLlCyberBar;
    @BindView(R.id.ll_underground)
    LinearLayout mLlUnderground;
    @BindView(R.id.ll_gas_station)
    LinearLayout ll_gas_station;

    NearByAI mAI;


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        //获取当前场景
        if (Constants.Scene.CurrentScene.equals(Constants.Scene.XingZheng)
                || Constants.Scene.CurrentScene.equals(Constants.Scene.CheGuanSuo)
                ) {
            if((BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                    || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))){
                return R.layout.activity_vertical_nearby;
            }else{
                return R.layout.activity_xingzheng_nearby;
            }
        } else {
            return getCorrectLayoutId(R.layout.activity_nearby, R.layout.activity_vertical_nearby);
        }
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        getTitleView().setBackVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_VOICE));
    }

    @Override
    public void init() {
        super.init();
        mAI = NearByAI.newInstance();
        mAI.initAI(this);
    }

    @Override
    protected CharSequence initJapanSpeakText() {
        return getString(R.string.nearby_init_speak);
    }

    @Override
    protected CharSequence initChineseSpeakText() {
        if(Constants.Scene.CurrentScene.equals(Constants.Scene.JiuDianScene)){
            String[] texts = new String[]{"您好，您是想查询些什么内容呢？"
                    ,"哇，发现了很多有趣的东西呢！您想看哪一个？"};
            int index = new Random().nextInt(2);
            if(index >= texts.length){
                index = 0;
            }
            return texts[index];
        }else if(Constants.Scene.CurrentScene.equals(Constants.Scene.YinHangScene)){
//            return "你想了解周边什么信息呢？";
        }
        return getString(R.string.nearby_init_speak);
    }

    @Override
    protected CharSequence initEnglishSpeakText() {
        return getString(R.string.nearby_init_speak);
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        NearByAI.Intent intent = mAI.getIntent(text);
        if (intent != null) {
            mAI.handleIntent(intent);
            return true;
        }
        prattle(answerText);
        return true;
    }



    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }


    @OnClick({R.id.ll_food, R.id.ll_scenic, R.id.ll_hotel, R.id.ll_relax, R.id.ll_share_bike, R.id.ll_supermarket, R.id.ll_atm, R.id.ll_wc, R.id.ll_fast_hotel, R.id.ll_cyber_bar, R.id.ll_underground, R.id.ll_gas_station})
    public void onViewClicked(View view) {
        Intent intent = new Intent(NearByActivity.this, PoiSearchActivity.class);
        switch (view.getId()) {
            case R.id.ll_food:
                goPoiSearchAct("美食");
                break;
            case R.id.ll_scenic:
                goPoiSearchAct("景点");
                break;
            case R.id.ll_hotel:
                goPoiSearchAct("酒店");
                break;
            case R.id.ll_relax:
                goPoiSearchAct("休闲娱乐");
                break;
            case R.id.ll_share_bike:
                goPoiSearchAct("共享单车");
                break;
            case R.id.ll_supermarket:
                goPoiSearchAct("超市");
                break;
            case R.id.ll_atm:
                goPoiSearchAct("ATM");
                break;
            case R.id.ll_wc:
                goPoiSearchAct("厕所");
                break;
            case R.id.ll_fast_hotel:
                goPoiSearchAct("快捷酒店");
                break;
            case R.id.ll_cyber_bar:
                goPoiSearchAct("网吧");
                break;
            case R.id.ll_underground:
                goPoiSearchAct("地铁");
                break;
            case R.id.ll_gas_station:
                goPoiSearchAct("加油站");
                break;
            default:
                break;
        }
    }

    public void goPoiSearchAct(String text) {
//        Intent intent = new Intent(NearByActivity.this, PoiSearchActivity.class);
//        intent.putExtra(PoiSearchActivity.KEYWORD, text);
//        startActivity(intent);
        BRouter.getInstance()
                .build(BRouterPath.NEAR_BY_SEARCH)
                .withString(BRouterKey.POISEARCH_KEYWORD, text)
                .navigation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
