package com.csjbot.blackgaga.feature.nearbyservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.ai.NearByAirportAI;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.router.BRouterKey;
import com.csjbot.blackgaga.router.BRouterPath;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by  Wql , 2018/2/27 15:58
 * 机场周边服务
 */

@Route(path = BRouterPath.NEAR_BY_MAIN_AIRPORT)
public class NearByAirportActivity extends BaseModuleActivity {
    NearByAirportAI mAI;
    @BindView(R.id.imageView2test)
    ImageView mImageView2test;
    @BindView(R.id.ll_taxitest)
    LinearLayout mLlTaxitest;
    @BindView(R.id.text)
    TextView mText;
    private boolean mFakeLocation;


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_nearby_airport,R.layout.vertical_activity_nearby_airport);
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        getTitleView().setBackVisibility(View.VISIBLE);
    }

    @Override
    public void init() {
        super.init();
        mAI = NearByAirportAI.newInstance();
        mAI.initAI(this);
    }

    @Override
    protected CharSequence initJapanSpeakText() {
        return getString(R.string.nearby_init_speak);
    }

    @Override
    protected CharSequence initChineseSpeakText() {
        return "您想了解周边什么信息呢？";
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
        if(intentCheck(text)){
            return true;
        }
        NearByAirportAI.Intent intent = mAI.getIntent(text);
        if (intent != null) {
            mAI.handleIntent(intent);
        } else {
            prattle(answerText);
        }
        return true;
    }

    private boolean intentCheck(String text){
        String foodRegex = "[\"附近\"|\"周边\"]有什么[\"餐馆\"|\"饭店\"|\"美食\"|\"小吃\"|\"吃的\"]+[。|.]";
        String superMarketRegex = "有[\"什么\"|\"哪些\"]特产[。|.]";
        String currencyExchangeRegex1 = "[\"附近\"|\"周边\"]有没有货币兑换[。|.]";
        String currencyExchangeRegex2 = "哪里有货币兑换[。|.]";
        String currencyExchangeRegex3 = "哪里可以[\"兑换货币\"|\"还钱\"]+[。|.]";
        String metroRegex = "地铁在[\"哪\"|\"什么地方\"]+[。|.]";
        String gasStationRegex = "[\"附近\"|\"周边\"]有没有加油站[。|.]";
        String atmRegex = "[\"附近\"|\"周边\"]有没有[\"取款机\"|\"ATM\"|\"存款机\"|\"存取款机\"]+[。|.]";
        String superMarketRegex2 = "[\"附近\"|\"周边\"]有没有[\"哪些\"|\"什么\"]超市[。|.]";
        String scenicSpotRegex = "有[\"什么\"|\"哪些\"]景点[。|.]";
        String scenicSpotRegex2 = "推荐景点[。|.]";
        if (text.matches(foodRegex)) {
            speak("为您找到以下附近的餐馆。");
            goPoiSearchAct("美食");
        }else if(text.matches(superMarketRegex)){
            speak("陕西特产丰富的很，您可以到附近的超市逛逛。");
            goPoiSearchAct("超市");
        }else if(text.matches(currencyExchangeRegex1) || text.matches(currencyExchangeRegex2) || text.matches(currencyExchangeRegex3)){
            speak("为您找到以下附近的酒店。");
            goPoiSearchAct("货币兑换");
        }else if(text.matches(metroRegex)){
            speak("附近的地铁站请见屏幕。");
            goPoiSearchAct("地铁");
        }else if(text.matches(gasStationRegex)){
            speak("加油站");
            goPoiSearchAct("加油站");
        }else if(text.matches(atmRegex)){
            speak("为您找到以下附近的ATM机。");
            goPoiSearchAct("ATM");
        }else if(text.matches(superMarketRegex2)){
            speak("为您找到以下附近的超市。");
            goPoiSearchAct("超市");
        }else if(text.matches(scenicSpotRegex) || text.matches(scenicSpotRegex2)){
            speak("六朝古都西安有很多著名的旅游景点。有兵马俑、华清池、钟鼓楼、古城墙、回民街、大雁塔、大唐芙蓉园、高冠瀑布、终南山古观音禅寺、太白山、南山温泉、八路军办事处、延安革命根据地等等。请说出您要了解的景点。");
            goPoiSearchAct("景点");
        }else{
            return false;
        }

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

    public void goPoiSearchAct(String text) {
        //        Intent intent = new Intent(NearByActivity.this, PoiSearchActivity.class);
        //        intent.putExtra(PoiSearchActivity.KEYWORD, text);
        //        startActivity(intent);
        BRouter.getInstance()
                .build(BRouterPath.NEAR_BY_SEARCH)
                .withString(BRouterKey.POISEARCH_KEYWORD, text)
                .navigation();
    }

    @OnClick({R.id.ll_food, R.id.ll_hotel, R.id.ll_currency_exchange, R.id.ll_information_desk, R.id.ll_duty_free_store, R.id.ll_scenic, R.id.ll_atm, R.id.ll_underground, R.id.ll_bus, R.id.ll_taxi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_food:
                goPoiSearchAct("美食");
                break;
            case R.id.ll_hotel:
                goPoiSearchAct("酒店");
                break;
            case R.id.ll_currency_exchange:
                goPoiSearchAct("货币兑换");
                break;
            case R.id.ll_information_desk:
                goPoiSearchAct("咨询台");
                break;
            case R.id.ll_duty_free_store:
                goPoiSearchAct("免税店");
                break;
            case R.id.ll_scenic:
                goPoiSearchAct("景点");
                break;
            case R.id.ll_atm:
                goPoiSearchAct("ATM");
                break;
            case R.id.ll_underground:
                goPoiSearchAct("地铁");
                break;
            case R.id.ll_bus:
                goPoiSearchAct("大巴");
                break;
            case R.id.ll_taxi:
                goPoiSearchAct("出租车");
                break;
        }
    }


    public void taxitest(View view) {
        mFakeLocation = true;
        SharedPreUtil.putBoolean(SharedKey.POISEARCH_FAKELOCATION, SharedKey.POISEARCH_FAKELOCATION, mFakeLocation);
        mText.setText("定位成功");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFakeLocation = false;
        SharedPreUtil.putBoolean(SharedKey.POISEARCH_FAKELOCATION, SharedKey.POISEARCH_FAKELOCATION, mFakeLocation);
        mText.setText("假装在定位");
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_VOICE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFakeLocation = false;
        SharedPreUtil.putBoolean(SharedKey.POISEARCH_FAKELOCATION, SharedKey.POISEARCH_FAKELOCATION, mFakeLocation);
    }
}
