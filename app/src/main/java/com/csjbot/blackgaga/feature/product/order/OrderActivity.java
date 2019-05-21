package com.csjbot.blackgaga.feature.product.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.ai.OrderAI;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.util.ShopcartUtil;


/**
 * Created by 孙秀艳 on 2017/10/25.
 */

public class OrderActivity extends BaseModuleActivity {

    OrderFragment mOrderFragment;

    OrderAI mAI;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
    }

    public OrderFragment getOrderFragment() {
        return mOrderFragment;
    }

    @Override
    public void init() {
        super.init();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mOrderFragment = new OrderFragment());
        transaction.commit();
        ShopcartUtil.setOrderIsBack(false);
        initTitle();

        mAI = OrderAI.newInstance();
        mAI.initAI(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    @Override
    protected CharSequence initChineseSpeakText() {
        String text = getString(R.string.order_native_payment) + "\r\n";
        if (BuildConfig.robotType.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE)) {
            text += getString(R.string.order_not_native_payment);
        }
        return text;
    }

    @Override
    protected CharSequence initEnglishSpeakText() {
        String text = getString(R.string.order_native_payment) + "\r\n";
        if (BuildConfig.robotType.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE)) {
            text += getString(R.string.order_not_native_payment);
        }
        return text;
    }

    private void initTitle() {
        getTitleView().setBackVisibility(View.VISIBLE);
        getTitleView().setBackListener(() -> {
            ShopcartUtil.setOrderIsBack(true);
            ShopcartUtil.clearShopcart();
            BRouter.toHome();
        });
        getTitleView().setHomePageListener(() -> {
            ShopcartUtil.clearShopcart();
            BRouter.toHome();
        });
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)|| BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))                ?  R.layout.vertical_activity_product_order : R.layout.activity_product_order;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ShopcartUtil.setOrderIsBack(true);
            ShopcartUtil.clearShopcart();
            BRouter.toHome();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
//        // 拦截语音控制返回动作
//        getChatControl().addIntercept(this.getClass().getSimpleName(), SpeechRule.Action.BACK, null);
//        // 拦截主页的语音控制返回主页动作
//        getChatControl().addIntercept(this.getClass().getSimpleName(), SpeechRule.Action.GO, this.getClass().getSimpleName());

//        if(super.onSpeechMessage(text,answerText)){
//            return false;
//        }
        OrderAI.Intent intent = mAI.getIntent(text);
        if (intent != null) {
            mAI.handleIntent(intent);
        } else {
            prattle(answerText);
        }
        return true;
    }
}
