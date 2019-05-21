package com.csjbot.blackgaga.feature.product.shopcart;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.ai.ShoppingCartAI;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.widget.ChatView;

/**
 * Created by 孙秀艳 on 2017/10/18.
 * 购物车界面
 */

public class ShoppingCartActivity extends BaseModuleActivity {
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)|| BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))                ?  R.layout.vertical_activity_shoppingcart : R.layout.activity_shoppingcart;
    }

    ShoppingCartFragment mFragment;

    ShoppingCartAI mAI;

    public ShoppingCartFragment getShoppingCartFragment() {
        return mFragment;
    }

    @Override
    public void init() {
        super.init();
        initView();

        mAI = ShoppingCartAI.newInstance();
        mAI.initAI(this);
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        ShoppingCartAI.Intent intent = mAI.getIntent(text);
        if (intent != null) {
            mAI.handleIntent(intent);
        } else {
            prattle(answerText);
        }
        return true;
    }

    public void replaceFragment(Fragment fragment, boolean isBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        if (isBackStack) {
            fragmentTransaction.addToBackStack(null);//返回栈
        }
        fragmentTransaction.commit();
    }

    public void setChatView(String content) {
        getChatView().clearChatMsg();
        getChatView().addChatMsg(ChatView.ChatMsgType.ROBOT_MSG, content);
        speak(content);
    }

    private void initView() {
        getTitleView().setBackVisibility(View.VISIBLE);
        replaceFragment(mFragment = new ShoppingCartFragment(), false);
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

}
